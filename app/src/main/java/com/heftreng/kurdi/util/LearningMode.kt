package com.heftreng.kurdi.util

enum class LearningMode {
    KURMANCI_TO_SORANI,  // Kurmancî biliyor → Soranî öğreniyor
    SORANI_TO_KURMANCI;  // Soranî biliyor → Kurmancî öğreniyor

    fun isKurmanci() = this == KURMANCI_TO_SORANI
}
