package com.heftreng.kurdi.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UniteMeta(
    @SerialName("unit_id")       val unitId: String,
    @SerialName("unit_order")    val unitOrder: Int,
    @SerialName("unit_title")    val unitTitle: MultiLangText,
    val icon: String,
    @SerialName("kelime_sayisi") val kelimeSayisi: Int,
    @SerialName("soru_sayisi")   val soruSayisi: Int,
    val dosya: String
)

@Serializable
data class IndexResponse(
    val versiyon: String,
    @SerialName("son_guncelleme")    val sonGuncelleme: String,
    @SerialName("cdn_base_primary")  val cdnBasePrimary: String,
    @SerialName("cdn_base_fallback") val cdnBaseFallback: String,
    val units: List<UniteMeta>
)

@Serializable
data class UniteDetay(
    @SerialName("unit_id")    val unitId: String,
    @SerialName("unit_order") val unitOrder: Int,
    @SerialName("unit_title") val unitTitle: MultiLangText,
    val vocabulary: List<Kelime>,
    val questions: List<Soru>
)
