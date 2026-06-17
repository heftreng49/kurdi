package com.heftreng.kurdi.data.model

import kotlinx.serialization.Serializable

@Serializable
data class MultiLangText(
    val kurmanci: String,
    val sorani: String
)
