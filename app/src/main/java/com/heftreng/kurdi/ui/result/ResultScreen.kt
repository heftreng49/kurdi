package com.heftreng.kurdi.ui.result

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ResultScreen(
    puan: Int,
    yildiz: Int,
    onAnaSayfaClick: () -> Unit
) {
    Column(
        Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Ders Tamamlandı!", style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(24.dp))

        // Yıldız gösterimi
        Text(
            text = "★".repeat(yildiz) + "☆".repeat(3 - yildiz),
            fontSize = 48.sp,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(Modifier.height(8.dp))
        Text("$puan doğru cevap", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(40.dp))

        val mesaj = when (yildiz) {
            3    -> "Mükemmel! Pekala!" // Kurmancî: "Pir baş!"
            2    -> "Aferin! Devam et!"
            1    -> "İyi başlangıç, tekrar dene!"
            else -> "Tekrar çalış, yapabilirsin!"
        }
        Text(mesaj, style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant)

        Spacer(Modifier.height(40.dp))
        Button(onClick = onAnaSayfaClick, modifier = Modifier.fillMaxWidth()) {
            Text("Ana Sayfaya Dön")
        }
    }
}
