package com.heftreng.kurdi.ui.settings

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
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
import com.heftreng.kurdi.util.LearningMode
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(app: Application) : AndroidViewModel(app) {
    private val tercih = TercihYoneticisi(app)

    val mode = tercih.learningModeAkisi.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), LearningMode.KURMANCI_TO_SORANI
    )

    fun modDegistir(yeniMod: LearningMode) {
        viewModelScope.launch { tercih.modKaydet(yeniMod) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onGeri: () -> Unit,
    vm: SettingsViewModel = viewModel()
) {
    val mevcutMod by vm.mode.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ayarlar") },
                navigationIcon = {
                    IconButton(onClick = onGeri) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Geri")
                    }
                }
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding).padding(16.dp)) {
            Text("Öğrenme Modu", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            Column(Modifier.selectableGroup()) {
                ModSecenegi(
                    secili = mevcutMod == LearningMode.KURMANCI_TO_SORANI,
                    baslik = "Kurmancî → Soranî",
                    aciklama = "Kurmancî biliyorum, Soranî öğrenmek istiyorum",
                    onClick = { vm.modDegistir(LearningMode.KURMANCI_TO_SORANI) }
                )
                Spacer(Modifier.height(8.dp))
                ModSecenegi(
                    secili = mevcutMod == LearningMode.SORANI_TO_KURMANCI,
                    baslik = "Soranî → Kurmancî",
                    aciklama = "Soranî biliyorum, Kurmancî öğrenmek istiyorum",
                    onClick = { vm.modDegistir(LearningMode.SORANI_TO_KURMANCI) }
                )
            }
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
