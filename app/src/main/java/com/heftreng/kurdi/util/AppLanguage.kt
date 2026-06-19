package com.heftreng.kurdi.util

enum class AppLanguage {
    TR,        // Türkçe
    KURMANCI,  // Kurmancî
    SORANI;    // Soranî (Arap alfabesi)

    fun isRtl() = this == SORANI
}
