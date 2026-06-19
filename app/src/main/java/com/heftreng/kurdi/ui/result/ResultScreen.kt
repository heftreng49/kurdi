package com.heftreng.kurdi.ui.result

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.heftreng.kurdi.data.local.TercihYoneticisi
import com.heftreng.kurdi.util.AppLanguage
import com.heftreng.kurdi.util.Strings.t

class ResultViewModel(app: Application) : AndroidViewModel(app) {
    private val tercih = TercihYoneticisi(app)
    val langAkisi = tercih.appLanguageAkisi
}

@Composable
fun ResultScreen(
    puan: Int,
    yildiz: Int,
    onAnaSayfaClick: () -> Unit,
    vm: ResultViewModel = viewModel()
) {
    val lang by vm.langAkisi.collectAsState(initial = AppLanguage.TR)

    Column(
        Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(t(lang, "result_title"), style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(24.dp))

        Text(
            text = "★".repeat(yildiz) + "☆".repeat(3 - yildiz),
            fontSize = 48.sp,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(Modifier.height(8.dp))
        Text("$puan ${t(lang, "result_correct_answers")}", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(40.dp))

        val mesajKey = when (yildiz) {
            3    -> "result_perfect"
            2    -> "result_good"
            1    -> "result_okay"
            else -> "result_retry"
        }
        Text(t(lang, mesajKey), style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant)

        Spacer(Modifier.height(40.dp))
        Button(onClick = onAnaSayfaClick, modifier = Modifier.fillMaxWidth()) {
            Text(t(lang, "result_home_button"))
        }
    }
}
