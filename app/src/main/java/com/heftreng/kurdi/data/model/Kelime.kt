package com.heftreng.kurdi.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Kelime(
    @SerialName("word_id")           val wordId: String,
    @SerialName("kurmanci_latin")    val kurmanciLatin: String,
    @SerialName("sorani_arami")      val soraniArami: String,
    @SerialName("phonetic_kurmanci") val phoneticKurmanci: String,
    @SerialName("phonetic_sorani")   val phoneticSorani: String,
    @SerialName("meaning_tr")        val meaningTr: String,
    @SerialName("audio_url")         val audioUrl: AudioUrl? = null,
    val description: MultiLangText
)
