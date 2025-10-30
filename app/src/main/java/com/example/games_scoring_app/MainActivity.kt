 package com.example.games_scoring_app

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.games_scoring_app.Data.AppDatabase
import com.example.games_scoring_app.Pages.GamePage
import com.example.games_scoring_app.Pages.HomePage
import com.example.games_scoring_app.Pages.SavedGamesPage
import com.example.games_scoring_app.Pages.SettingsPage
import com.example.games_scoring_app.Pages.SetupPage
import com.example.games_scoring_app.Pages.UtilitiesPage
import com.example.games_scoring_app.Theme.LeagueGothic
import com.example.games_scoring_app.Theme.black
import com.example.games_scoring_app.Theme.blue
import com.example.games_scoring_app.Theme.white
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
        AppDatabase.getDatabase(applicationContext, lifecycleScope)
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
                Log.d(TAG, "isDatabaseReady called")
                val isDatabaseReady by AppDatabase.isDatabaseReady.collectAsState()
                Log.d(TAG, "isDatabaseReady: $isDatabaseReady")
                if (isDatabaseReady) {
                    MainScreen()
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize().background(black),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.logobig),
                                contentDescription = "App Image",
                                modifier = Modifier.size(100.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "LOADING",
                                style = TextStyle(
                                    fontFamily = LeagueGothic,
                                    fontSize = 60.sp,
                                    color = white
                                ),
                                textAlign = TextAlign.Center
                            )
                            CircularProgressIndicator(color = white)
                        }
                    }
                }
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
                        navArgument("gameId") { type = NavType.IntType },
                        navArgument("gameTypeId") { type = NavType.IntType }
                    )
                ) { backStackEntry ->
                    // ADDED: Extract the gameId from the backStackEntry
                    val gameId = backStackEntry.arguments?.getInt("gameId") ?: 0
                    val gameTypeId = backStackEntry.arguments?.getInt("gameTypeId") ?: 0
                    
                    GamePage(
                        navController = navController,
                        // ADDED: Pass the extracted gameId to the composable
                        gameId = gameId,
                        gameTypeId = gameTypeId
                    )
                }
                composable(
                    route = Screen.SetUp.route,
                    arguments = listOf(
                        navArgument("gameType") { type = NavType.IntType },
                        navArgument("gameColor") { type = NavType.StringType },
                        navArgument("playerNames") {
                            type = NavType.StringType
                            nullable = true
                            defaultValue = null
                        }
                    )
                ) { backStackEntry ->
                    val gameType = backStackEntry.arguments?.getInt("gameType") ?: 0
                    val hex = backStackEntry.arguments?.getString("gameColor") ?: blue.value.toString(16)
                    val color = Color(hex.toULong(16))
                    val playerNamesString = backStackEntry.arguments?.getString("playerNames")
                    SetupPage(
                        navController = navController,
                        gameType = gameType,
                        gameColor = color,
                        existingPlayerNames = playerNamesString
                    )
                }
                composable(Screen.SavedGames.route) {
                    SavedGamesPage(
                        navController = navController,
                    )
                }
                composable(Screen.Settings.route) {
                    SettingsPage(navController = navController)
                }
                composable(
                    route = Screen.Utilities.route,
                    arguments = listOf(
                        navArgument("utilityId") { type = NavType.IntType },
                    )
                ) { backStackEntry ->
                    val utilityId = backStackEntry.arguments?.getInt("utilityId") ?: 0
                    UtilitiesPage(
                        navController = navController,
                        utilityId = utilityId
                    )
                }
            }
        }
    }
}

