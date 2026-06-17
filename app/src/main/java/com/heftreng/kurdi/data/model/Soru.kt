package com.heftreng.kurdi.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@Serializable data class TargetWord(
    @SerialName("kurmanci_user_sees") val kurmanciUserSees: String,
    @SerialName("sorani_user_sees")   val soraniUserSees: String
)

@Serializable data class Options(
    @SerialName("for_kurmanci_user") val forKurmanciUser: List<String>,
    @SerialName("for_sorani_user")   val forSoraniUser: List<String>
)

@Serializable data class CorrectIndex(val kurmanci: Int, val sorani: Int)

@Serializable data class SentenceHalf(val prefix: String, val suffix: String)

@Serializable data class BilingualSentence(
    val kurmanci: SentenceHalf,
    val sorani: SentenceHalf
)

@Serializable data class CorrectBlank(val kurmanci: String, val sorani: String)

@Serializable data class WordPair(val kurmanci: String, val sorani: String)

@JsonClassDiscriminator("type")
@Serializable
sealed class Soru {
    abstract val questionId: String
    abstract val level: String
    abstract val yonege: MultiLangText

    @Serializable @SerialName("multiple_choice")
    data class MultipleChoice(
        @SerialName("question_id") override val questionId: String,
        override val level: String,
        override val yonege: MultiLangText,
        @SerialName("target_word")   val targetWord: TargetWord,
        val options: Options,
        @SerialName("correct_index") val correctIndex: CorrectIndex
    ) : Soru()

    @Serializable @SerialName("true_false")
    data class TrueFalse(
        @SerialName("question_id")    override val questionId: String,
        override val level: String,
        override val yonege: MultiLangText,
        val statement: MultiLangText,
        @SerialName("correct_answer") val correctAnswer: Boolean
    ) : Soru()

    @Serializable @SerialName("fill_in_the_blank")
    data class FillInTheBlank(
        @SerialName("question_id")   override val questionId: String,
        override val level: String,
        override val yonege: MultiLangText,
        val sentence: BilingualSentence,
        @SerialName("correct_blank") val correctBlank: CorrectBlank
    ) : Soru()

    @Serializable @SerialName("word_matching")
    data class WordMatching(
        @SerialName("question_id") override val questionId: String,
        override val level: String,
        override val yonege: MultiLangText,
        val pairs: List<WordPair>
    ) : Soru()
}
