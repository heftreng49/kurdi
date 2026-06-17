package com.heftreng.kurdi.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AudioUrl(
    val kurmanci: String? = null,
    val sorani: String? = null
)
