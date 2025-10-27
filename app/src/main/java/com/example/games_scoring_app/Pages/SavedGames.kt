package com.example.games_scoring_app.Pages

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.games_scoring_app.Components.ButtonDateBar
import com.example.games_scoring_app.Components.GameBox
import com.example.games_scoring_app.Components.LastGameBox
import com.example.games_scoring_app.Components.PageTitle
import com.example.games_scoring_app.Components.WidgetTitle
import com.example.games_scoring_app.Data.AppDatabase
import com.example.games_scoring_app.Data.GameTypesRepository
import com.example.games_scoring_app.Data.GamesRepository
import com.example.games_scoring_app.Data.SettingsRepository
import com.example.games_scoring_app.R
import com.example.games_scoring_app.Screen
import com.example.games_scoring_app.Theme.LeagueGothic
import com.example.games_scoring_app.Theme.black
import com.example.games_scoring_app.Theme.blue
import com.example.games_scoring_app.Theme.darkgray
import com.example.games_scoring_app.Theme.green
import com.example.games_scoring_app.Theme.white
import com.example.games_scoring_app.Theme.yellow
import com.example.games_scoring_app.Viewmodel.GameTypesViewModel
import com.example.games_scoring_app.Viewmodel.GameTypesViewModelFactory
import com.example.games_scoring_app.Viewmodel.GamesViewModel
import com.example.games_scoring_app.Viewmodel.GamesViewModelFactory
import com.example.games_scoring_app.Viewmodel.SettingsViewModel
import com.example.games_scoring_app.Viewmodel.SettingsViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@Composable
fun SavedGamesPage(navController: NavController) {
    var noGames = true
    val scrollState = rememberScrollState()

    val applicationScope = CoroutineScope(SupervisorJob())
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context, applicationScope)

    val gamesRepository = GamesRepository(database.gamesDao())
    val gamesViewModelFactory = GamesViewModelFactory(gamesRepository)
    val gamesViewModel: GamesViewModel = viewModel(factory = gamesViewModelFactory)
    val gameTypesRepository = GameTypesRepository(database.gameTypesDao())
    val gameTypesViewModelFactory = GameTypesViewModelFactory(gameTypesRepository)
    val gameTypesViewModel: GameTypesViewModel = viewModel(factory = gameTypesViewModelFactory)

    // --- MODIFIED --- Use the new StateFlow that includes players
    val allGamesWithPlayers by gamesViewModel.allGamesWithPlayers.collectAsState()
    val gameTypes by gameTypesViewModel.allGameTypes.collectAsState()

    val settingsRepository = SettingsRepository(database.settingsDao())
    val settingsViewModelFactory = SettingsViewModelFactory(settingsRepository)
    val settingsViewModel: SettingsViewModel = viewModel(factory = settingsViewModelFactory)

    val themeMode by settingsViewModel.themeMode.collectAsState()

    val backgroundColor = if (themeMode == 0) black else white
    val fontColor = if (themeMode == 0) white else black
    val buttonColor = if (themeMode == 0) white else black
    val buttonFontColor = if (themeMode == 0) black else white

    LaunchedEffect(key1 = Unit) {
        // --- MODIFIED --- Call the new function
        gamesViewModel.getAllGamesWithPlayers()
        gameTypesViewModel.getAllGameTypes()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(64.dp))
        WidgetTitle("GAMES PLAYED", R.drawable.papers, navController);
        Spacer(modifier = Modifier.height(20.dp))

        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "GAMES PLAYED",
            fontFamily = LeagueGothic,
            fontSize = 48.sp,
            color = fontColor,
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Left
        )
        Spacer(modifier = Modifier.height(20.dp))
        Column(Modifier.padding(horizontal = 16.dp)) {
            if (allGamesWithPlayers.isNotEmpty() && gameTypes.isNotEmpty()) {
                // --- MODIFIED --- Loop through the list of GameWithPlayers
                for (gameWithPlayers in allGamesWithPlayers) {
                    val game = gameWithPlayers.game
                    val gameType = gameTypes.find { it?.id == game.id_GameType }

                    if (gameType != null) {
                        var buttonIconId = 0
                        var accentColor = yellow
                        when (gameType.type) {
                            "Dados" -> {
                                buttonIconId = R.drawable.dices
                                accentColor = blue
                            }
                            "Cartas" -> {
                                buttonIconId = R.drawable.card
                                accentColor = yellow
                            }
                            "Generico" -> {
                                buttonIconId = R.drawable.paper
                                accentColor = green
                            }
                            else -> {
                                buttonIconId = R.drawable.paper
                                accentColor = white
                            }
                        }
                        GameBox(
                            title = gameType.name.uppercase(),
                            onClick = { navController.navigate(Screen.Game.createRoute(game.id, game.id_GameType)) },
                            bgcolor = darkgray,
                            textcolor = buttonColor,
                            accentColor = accentColor,
                            icon = buttonIconId,
                            gameType = gameType.type,
                            daysSinceLastPlayed = game.date,
                            // --- FIX ---
                            // Pass the List<Player> directly instead of mapping it to a List<String>
                            players = gameWithPlayers.players
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }

            } else {
                Text(
                    text = "No games played yet",
                    fontFamily = LeagueGothic,
                    fontSize = 24.sp,
                    color = fontColor,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Left
                )
            }
        }
    }
}
