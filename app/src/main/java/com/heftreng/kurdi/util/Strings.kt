package com.heftreng.kurdi.util

/**
 * Uygulama arayüzü için üç dilli metin merkezi.
 * Kullanım: t(AppLanguage.KURMANCI, "home_title")
 */
object Strings {

    private val keys: Map<String, Map<AppLanguage, String>> = mapOf(
        // ── Genel ─────────────────────────────────────────────────────────
        "app_name" to mapOf(
            AppLanguage.TR to "Kurdî",
            AppLanguage.KURMANCI to "Kurdî",
            AppLanguage.SORANI to "کوردی"
        ),
        "retry" to mapOf(
            AppLanguage.TR to "Tekrar Dene",
            AppLanguage.KURMANCI to "Dîsa Biceribîne",
            AppLanguage.SORANI to "دووبارە هەوڵبدەرەوە"
        ),
        "connection_error" to mapOf(
            AppLanguage.TR to "Bağlantı hatası",
            AppLanguage.KURMANCI to "Xeletiya girêdanê",
            AppLanguage.SORANI to "هەڵەی پەیوەندی"
        ),

        // ── Ana Sayfa ─────────────────────────────────────────────────────
        "home_mode_kurmanci_sorani" to mapOf(
            AppLanguage.TR to "Kurmancî → Soranî öğreniyorsun",
            AppLanguage.KURMANCI to "Tu Kurmancî → Soranî hîn dibî",
            AppLanguage.SORANI to "تۆ کورمانجی → سۆرانی فێر دەبیت"
        ),
        "home_mode_sorani_kurmanci" to mapOf(
            AppLanguage.TR to "Soranî → Kurmancî öğreniyorsun",
            AppLanguage.KURMANCI to "Tu Soranî → Kurmancî hîn dibî",
            AppLanguage.SORANI to "تۆ سۆرانی → کورمانجی فێر دەبیت"
        ),
        "home_words_questions" to mapOf(  // "%d kelime · %d soru" formatı için prefix/suffix
            AppLanguage.TR to "kelime",
            AppLanguage.KURMANCI to "peyv",
            AppLanguage.SORANI to "وشە"
        ),
        "home_questions_label" to mapOf(
            AppLanguage.TR to "soru",
            AppLanguage.KURMANCI to "pirs",
            AppLanguage.SORANI to "پرسیار"
        ),

        // ── Ders Ekranı ───────────────────────────────────────────────────
        "lesson_load_error" to mapOf(
            AppLanguage.TR to "Ünite yüklenemedi",
            AppLanguage.KURMANCI to "Yekîne nehat barkirin",
            AppLanguage.SORANI to "یەکە بارنەکرا"
        ),
        "lesson_continue" to mapOf(
            AppLanguage.TR to "Devam →",
            AppLanguage.KURMANCI to "Bidomîne →",
            AppLanguage.SORANI to "بەردەوامبە →"
        ),
        "lesson_check" to mapOf(
            AppLanguage.TR to "Kontrol Et",
            AppLanguage.KURMANCI to "Kontrol Bike",
            AppLanguage.SORANI to "پشکنین بکە"
        ),
        "lesson_true" to mapOf(
            AppLanguage.TR to "✓ Doğru",
            AppLanguage.KURMANCI to "✓ Rast",
            AppLanguage.SORANI to "✓ ڕاست"
        ),
        "lesson_false" to mapOf(
            AppLanguage.TR to "✗ Yanlış",
            AppLanguage.KURMANCI to "✗ Şaş",
            AppLanguage.SORANI to "✗ هەڵە"
        ),
        "lesson_matched" to mapOf(
            AppLanguage.TR to "eşleşti",
            AppLanguage.KURMANCI to "li hev hat",
            AppLanguage.SORANI to "گونجا"
        ),
        "lesson_done" to mapOf(
            AppLanguage.TR to "Tamamlandı →",
            AppLanguage.KURMANCI to "Qediya →",
            AppLanguage.SORANI to "تەواوبوو →"
        ),

        // ── Sonuç Ekranı ──────────────────────────────────────────────────
        "result_title" to mapOf(
            AppLanguage.TR to "Ders Tamamlandı!",
            AppLanguage.KURMANCI to "Ders Qediya!",
            AppLanguage.SORANI to "وانە تەواوبوو!"
        ),
        "result_correct_answers" to mapOf(
            AppLanguage.TR to "doğru cevap",
            AppLanguage.KURMANCI to "bersiva rast",
            AppLanguage.SORANI to "وەڵامی ڕاست"
        ),
        "result_perfect" to mapOf(
            AppLanguage.TR to "Mükemmel! Pekala!",
            AppLanguage.KURMANCI to "Pir baş!",
            AppLanguage.SORANI to "زۆر باش!"
        ),
        "result_good" to mapOf(
            AppLanguage.TR to "Aferin! Devam et!",
            AppLanguage.KURMANCI to "Aferîn! Bidomîne!",
            AppLanguage.SORANI to "ئاوات! بەردەوامبە!"
        ),
        "result_okay" to mapOf(
            AppLanguage.TR to "İyi başlangıç, tekrar dene!",
            AppLanguage.KURMANCI to "Destpêkek baş e, dîsa biceribîne!",
            AppLanguage.SORANI to "دەستپێکی باشە، دووبارە هەوڵبدەرەوە!"
        ),
        "result_retry" to mapOf(
            AppLanguage.TR to "Tekrar çalış, yapabilirsin!",
            AppLanguage.KURMANCI to "Dîsa hewl bide, tu dikarî!",
            AppLanguage.SORANI to "دووبارە هەوڵبدە، دەتوانیت!"
        ),
        "result_home_button" to mapOf(
            AppLanguage.TR to "Ana Sayfaya Dön",
            AppLanguage.KURMANCI to "Vegere Rûpela Sereke",
            AppLanguage.SORANI to "بگەرێوە بۆ پەرەی سەرەکی"
        ),

        // ── Ayarlar ───────────────────────────────────────────────────────
        "settings_title" to mapOf(
            AppLanguage.TR to "Ayarlar",
            AppLanguage.KURMANCI to "Mîheng",
            AppLanguage.SORANI to "ڕێکخستن"
        ),
        "settings_learning_mode" to mapOf(
            AppLanguage.TR to "Öğrenme Modu",
            AppLanguage.KURMANCI to "Moda Fêrbûnê",
            AppLanguage.SORANI to "شێوازی فێربوون"
        ),
        "settings_mode_k2s_title" to mapOf(
            AppLanguage.TR to "Kurmancî → Soranî",
            AppLanguage.KURMANCI to "Kurmancî → Soranî",
            AppLanguage.SORANI to "کورمانجی → سۆرانی"
        ),
        "settings_mode_k2s_desc" to mapOf(
            AppLanguage.TR to "Kurmancî biliyorum, Soranî öğrenmek istiyorum",
            AppLanguage.KURMANCI to "Ez Kurmancî dizanim, dixwazim Soranî hîn bibim",
            AppLanguage.SORANI to "من کورمانجی دەزانم، دەمەوێت سۆرانی فێربم"
        ),
        "settings_mode_s2k_title" to mapOf(
            AppLanguage.TR to "Soranî → Kurmancî",
            AppLanguage.KURMANCI to "Soranî → Kurmancî",
            AppLanguage.SORANI to "سۆرانی → کورمانجی"
        ),
        "settings_mode_s2k_desc" to mapOf(
            AppLanguage.TR to "Soranî biliyorum, Kurmancî öğrenmek istiyorum",
            AppLanguage.KURMANCI to "Ez Soranî dizanim, dixwazim Kurmancî hîn bibim",
            AppLanguage.SORANI to "من سۆرانی دەزانم، دەمەوێت کورمانجی فێربم"
        ),
        "settings_interface_language" to mapOf(
            AppLanguage.TR to "Arayüz Dili",
            AppLanguage.KURMANCI to "Zimanê Navîgehê",
            AppLanguage.SORANI to "زمانی ڕووکار"
        ),
        "settings_lang_tr" to mapOf(
            AppLanguage.TR to "Türkçe",
            AppLanguage.KURMANCI to "Tirkî",
            AppLanguage.SORANI to "تورکی"
        ),
        "settings_lang_kurmanci" to mapOf(
            AppLanguage.TR to "Kurmancî",
            AppLanguage.KURMANCI to "Kurmancî",
            AppLanguage.SORANI to "کورمانجی"
        ),
        "settings_lang_sorani" to mapOf(
            AppLanguage.TR to "Soranî",
            AppLanguage.KURMANCI to "Soranî",
            AppLanguage.SORANI to "سۆرانی"
        )
    )

    fun t(lang: AppLanguage, key: String): String =
        keys[key]?.get(lang) ?: keys[key]?.get(AppLanguage.TR) ?: key
}
