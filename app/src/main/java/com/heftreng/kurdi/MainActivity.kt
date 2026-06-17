package com.heftreng.kurdi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.heftreng.kurdi.ui.KurdiNavHost
import com.heftreng.kurdi.ui.theme.KurdiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KurdiTheme {
                KurdiNavHost()
            }
        }
    }
}
