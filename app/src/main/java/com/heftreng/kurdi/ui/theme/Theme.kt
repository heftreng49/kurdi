package com.heftreng.kurdi.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Kürtçe yeşil — doğa ve dil teması
private val Yesil     = Color(0xFF2E7D32)
private val YesilAcik = Color(0xFF66BB6A)
private val Altin     = Color(0xFFFFB300)

private val AcikRenkSemasi = lightColorScheme(
    primary         = Yesil,
    onPrimary       = Color.White,
    secondary       = Altin,
    onSecondary     = Color.Black,
    background      = Color(0xFFF5F5F5),
    surface         = Color.White,
    onBackground    = Color(0xFF1A1A1A),
    onSurface       = Color(0xFF1A1A1A)
)

private val KoyuRenkSemasi = darkColorScheme(
    primary         = YesilAcik,
    onPrimary       = Color.Black,
    secondary       = Altin,
    onSecondary     = Color.Black,
    background      = Color(0xFF121212),
    surface         = Color(0xFF1E1E1E),
    onBackground    = Color(0xFFE0E0E0),
    onSurface       = Color(0xFFE0E0E0)
)

@Composable
fun KurdiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) KoyuRenkSemasi else AcikRenkSemasi,
        content     = content
    )
}
