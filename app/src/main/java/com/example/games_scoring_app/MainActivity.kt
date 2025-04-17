package com.example.games_scoring_app

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.navigation.NavType
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.games_scoring_app.Pages.GamePage
import com.example.games_scoring_app.Pages.HomePage
import com.example.games_scoring_app.Pages.SavedGamesPage
import com.example.games_scoring_app.Pages.SetupPage
import com.example.games_scoring_app.ui.theme.Games_Scoring_AppTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController


class MainActivity : ComponentActivity() {


    private val TAG = "MainActivity"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate called")
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            Games_Scoring_AppTheme {
                val systemUiController = rememberSystemUiController()
                val useDarkIcons = !isSystemInDarkTheme()

                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = Color.Transparent,
                        darkIcons = useDarkIcons
                    )
                    systemUiController.setNavigationBarColor(
                        color = Color.Transparent,
                        darkIcons = useDarkIcons
                    )
                }
                MainScreen()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets(0, 0, 0, 0)
        )
        { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                composable(Screen.Home.route) {
                    HomePage(
                        navController = navController,
                    )
                }
                composable(
                    route = Screen.Game.route,
                    arguments = listOf(
                        navArgument("game_id") { type = NavType.IntType }
                    )
                ) { backStackEntry ->
                    val gameid = backStackEntry.arguments?.getInt("game_id") ?: 0
                    GamePage(
                        navController = navController,
                        match_id = gameid
                    )
                }
                composable(
                    route = Screen.SetUp.route,
                    arguments = listOf(
                        navArgument("game_type") { type = NavType.IntType }
                    )
                ) { backStackEntry ->
                    val gameType = backStackEntry.arguments?.getInt("game_type") ?: 0
                    SetupPage(
                        navController = navController,
                        match_type = gameType
                    )
                }
                composable(Screen.SavedGames.route) {
                    SavedGamesPage(
                        navController = navController,
                    )
                }
            }
        }
    }
}

