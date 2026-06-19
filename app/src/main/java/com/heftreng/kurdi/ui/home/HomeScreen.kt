package com.heftreng.kurdi.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.heftreng.kurdi.data.model.UniteMeta
import com.heftreng.kurdi.util.AppLanguage
import com.heftreng.kurdi.util.LearningMode
import com.heftreng.kurdi.util.Strings.t

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onUniteClick: (String) -> Unit,
    onSettingsClick: () -> Unit,
    vm: HomeViewModel = viewModel()
) {
    val state by vm.uiState.collectAsState()
    val lang = state.appLanguage

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(t(lang, "app_name"), fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, contentDescription = t(lang, "settings_title"))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when {
                state.yukleniyorMu -> CircularProgressIndicator(Modifier.align(Alignment.Center))

                state.hataMesaji != null -> Column(
                    Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(t(lang, "connection_error"), style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = vm::uniteleriYukle) { Text(t(lang, "retry")) }
                }

                else -> LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        ModBilgiBandı(state.learningMode, lang)
                        Spacer(Modifier.height(8.dp))
                    }
                    items(state.uniteler, key = { it.unitId }) { unite ->
                        val ilerleme = state.ilerlemeMap[unite.unitId]
                        UniteKart(
                            unite        = unite,
                            mode         = state.learningMode,
                            lang         = lang,
                            tamamlandi   = ilerleme?.tamamlandiMi ?: false,
                            yildiz       = ilerleme?.yildiz ?: 0,
                            onClick      = { onUniteClick(unite.dosya) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ModBilgiBandı(mode: LearningMode, lang: AppLanguage) {
    val metin = if (mode.isKurmanci())
        t(lang, "home_mode_kurmanci_sorani")
    else
        t(lang, "home_mode_sorani_kurmanci")

    Surface(
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = metin,
            modifier = Modifier.padding(12.dp),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

@Composable
private fun UniteKart(
    unite: UniteMeta,
    mode: LearningMode,
    lang: AppLanguage,
    tamamlandi: Boolean,
    yildiz: Int,
    onClick: () -> Unit
) {
    val baslik = if (mode.isKurmanci()) unite.unitTitle.kurmanci else unite.unitTitle.sorani

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(unite.icon, style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(baslik, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(4.dp))
                Text(
                    "${unite.kelimeSayisi} ${t(lang, "home_words_questions")} · ${unite.soruSayisi} ${t(lang, "home_questions_label")}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (tamamlandi) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "★".repeat(yildiz) + "☆".repeat(3 - yildiz),
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
            if (tamamlandi) {
                Icon(
                    imageVector = Icons.Default.Settings, // checkmark placeholder
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
