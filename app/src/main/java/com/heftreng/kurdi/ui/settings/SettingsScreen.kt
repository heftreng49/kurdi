package com.heftreng.kurdi.ui.settings

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.heftreng.kurdi.data.local.TercihYoneticisi
import com.heftreng.kurdi.util.AppLanguage
import com.heftreng.kurdi.util.LearningMode
import com.heftreng.kurdi.util.Strings.t
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SettingsUiState(
    val mode: LearningMode = LearningMode.KURMANCI_TO_SORANI,
    val lang: AppLanguage = AppLanguage.TR
)

class SettingsViewModel(app: Application) : AndroidViewModel(app) {
    private val tercih = TercihYoneticisi(app)

    val uiState = combine(
        tercih.learningModeAkisi,
        tercih.appLanguageAkisi
    ) { mode, lang -> SettingsUiState(mode, lang) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SettingsUiState())

    fun modDegistir(yeniMod: LearningMode) {
        viewModelScope.launch { tercih.modKaydet(yeniMod) }
    }

    fun dilDegistir(yeniDil: AppLanguage) {
        viewModelScope.launch { tercih.dilKaydet(yeniDil) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onGeri: () -> Unit,
    vm: SettingsViewModel = viewModel()
) {
    val state by vm.uiState.collectAsState()
    val lang = state.lang

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(t(lang, "settings_title")) },
                navigationIcon = {
                    IconButton(onClick = onGeri) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Geri")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // ── Arayüz Dili ───────────────────────────────────────────────
            Text(t(lang, "settings_interface_language"), style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            Column(Modifier.selectableGroup()) {
                DilSecenegi(
                    secili = lang == AppLanguage.TR,
                    baslik = t(lang, "settings_lang_tr"),
                    onClick = { vm.dilDegistir(AppLanguage.TR) }
                )
                Spacer(Modifier.height(8.dp))
                DilSecenegi(
                    secili = lang == AppLanguage.KURMANCI,
                    baslik = t(lang, "settings_lang_kurmanci"),
                    onClick = { vm.dilDegistir(AppLanguage.KURMANCI) }
                )
                Spacer(Modifier.height(8.dp))
                DilSecenegi(
                    secili = lang == AppLanguage.SORANI,
                    baslik = t(lang, "settings_lang_sorani"),
                    onClick = { vm.dilDegistir(AppLanguage.SORANI) }
                )
            }

            Spacer(Modifier.height(32.dp))
            HorizontalDivider()
            Spacer(Modifier.height(24.dp))

            // ── Öğrenme Modu ──────────────────────────────────────────────
            Text(t(lang, "settings_learning_mode"), style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            Column(Modifier.selectableGroup()) {
                ModSecenegi(
                    secili = state.mode == LearningMode.KURMANCI_TO_SORANI,
                    baslik = t(lang, "settings_mode_k2s_title"),
                    aciklama = t(lang, "settings_mode_k2s_desc"),
                    onClick = { vm.modDegistir(LearningMode.KURMANCI_TO_SORANI) }
                )
                Spacer(Modifier.height(8.dp))
                ModSecenegi(
                    secili = state.mode == LearningMode.SORANI_TO_KURMANCI,
                    baslik = t(lang, "settings_mode_s2k_title"),
                    aciklama = t(lang, "settings_mode_s2k_desc"),
                    onClick = { vm.modDegistir(LearningMode.SORANI_TO_KURMANCI) }
                )
            }
        }
    }
}

@Composable
private fun DilSecenegi(
    secili: Boolean,
    baslik: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().selectable(
            selected = secili,
            onClick  = onClick,
            role     = Role.RadioButton
        ),
        colors = if (secili)
            CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        else
            CardDefaults.cardColors()
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            RadioButton(selected = secili, onClick = onClick)
            Spacer(Modifier.width(12.dp))
            Text(baslik, style = MaterialTheme.typography.titleSmall)
        }
    }
}

@Composable
private fun ModSecenegi(
    secili: Boolean,
    baslik: String,
    aciklama: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().selectable(
            selected = secili,
            onClick  = onClick,
            role     = Role.RadioButton
        ),
        colors = if (secili)
            CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        else
            CardDefaults.cardColors()
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            RadioButton(selected = secili, onClick = onClick)
            Spacer(Modifier.width(12.dp))
            Column {
                Text(baslik, style = MaterialTheme.typography.titleSmall)
                Text(aciklama, style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
