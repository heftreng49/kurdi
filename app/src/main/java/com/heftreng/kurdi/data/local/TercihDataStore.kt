package com.heftreng.kurdi.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.heftreng.kurdi.util.AppLanguage
import com.heftreng.kurdi.util.LearningMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.tercihDataStore: DataStore<Preferences> by preferencesDataStore(name = "kurdi_prefs")

object TercihAnahtarlari {
    val LEARNING_MODE = stringPreferencesKey("learning_mode")
    val APP_LANGUAGE  = stringPreferencesKey("app_language")
}

class TercihYoneticisi(private val context: Context) {

    val learningModeAkisi: Flow<LearningMode> = context.tercihDataStore.data
        .map { prefs ->
            LearningMode.valueOf(
                prefs[TercihAnahtarlari.LEARNING_MODE] ?: LearningMode.KURMANCI_TO_SORANI.name
            )
        }

    // Varsayılan arayüz dili Türkçe — kullanıcı isterse Kurmancî/Soranî'ye geçebilir
    val appLanguageAkisi: Flow<AppLanguage> = context.tercihDataStore.data
        .map { prefs ->
            AppLanguage.valueOf(
                prefs[TercihAnahtarlari.APP_LANGUAGE] ?: AppLanguage.TR.name
            )
        }

    suspend fun modKaydet(mode: LearningMode) {
        context.tercihDataStore.edit { prefs ->
            prefs[TercihAnahtarlari.LEARNING_MODE] = mode.name
        }
    }

    suspend fun dilKaydet(lang: AppLanguage) {
        context.tercihDataStore.edit { prefs ->
            prefs[TercihAnahtarlari.APP_LANGUAGE] = lang.name
        }
    }
}
