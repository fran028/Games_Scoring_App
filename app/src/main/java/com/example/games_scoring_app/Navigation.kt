package com.example.games_scoring_app

import android.net.Uri
import androidx.compose.ui.graphics.Color

sealed class Screen(val route: String) {
    object Home : Screen("Home")
    object Game : Screen("Game/{gameId}/{gameTypeId}/{playerNames}"){
        fun createRoute(
            gameId: Int,
            gameTypeId: Int,
            playerNames: Array<String>
        ): String {
            val playerNamesString = playerNames.joinToString(separator = "‚‗‚") // Using a less common separator
            val encodedPlayerNames = Uri.encode(playerNamesString)
            return "Game/$gameId/$gameTypeId/$encodedPlayerNames"
        }
    }
    object SetUp : Screen("SetUp/{gameType}/{gameColor}"){
        fun createRoute(gameType: Int?, gameColor: Color) = "SetUp/$gameType/${gameColor.value.toString(16)}"
    }
    object SavedGames : Screen("SavedGames")
    object RollDice : Screen("RollDice")
    object Settings : Screen("Settings")

}
