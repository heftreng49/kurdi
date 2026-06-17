package com.heftreng.kurdi.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.heftreng.kurdi.ui.home.HomeScreen
import com.heftreng.kurdi.ui.lesson.LessonScreen
import com.heftreng.kurdi.ui.result.ResultScreen
import com.heftreng.kurdi.ui.settings.SettingsScreen

object Route {
    const val HOME     = "home"
    const val LESSON   = "lesson/{uniteDosya}"
    const val RESULT   = "result/{puan}/{yildiz}"
    const val SETTINGS = "settings"

    fun lesson(uniteDosya: String)       = "lesson/$uniteDosya"
    fun result(puan: Int, yildiz: Int)   = "result/$puan/$yildiz"
}

@Composable
fun KurdiNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Route.HOME) {

        composable(Route.HOME) {
            HomeScreen(
                onUniteClick   = { dosya -> navController.navigate(Route.lesson(dosya)) },
                onSettingsClick = { navController.navigate(Route.SETTINGS) }
            )
        }

        composable(
            route = Route.LESSON,
            arguments = listOf(navArgument("uniteDosya") { type = NavType.StringType })
        ) { back ->
            val dosya = back.arguments?.getString("uniteDosya") ?: return@composable
            LessonScreen(
                uniteDosya = dosya,
                onBitti    = { puan, yildiz ->
                    navController.navigate(Route.result(puan, yildiz)) {
                        popUpTo(Route.HOME)
                    }
                },
                onGeri = { navController.popBackStack() }
            )
        }

        composable(
            route = Route.RESULT,
            arguments = listOf(
                navArgument("puan")   { type = NavType.IntType },
                navArgument("yildiz") { type = NavType.IntType }
            )
        ) { back ->
            val puan   = back.arguments?.getInt("puan")   ?: 0
            val yildiz = back.arguments?.getInt("yildiz") ?: 0
            ResultScreen(
                puan    = puan,
                yildiz  = yildiz,
                onAnaSayfaClick = { navController.popBackStack(Route.HOME, inclusive = false) }
            )
        }

        composable(Route.SETTINGS) {
            SettingsScreen(onGeri = { navController.popBackStack() })
        }
    }
}
