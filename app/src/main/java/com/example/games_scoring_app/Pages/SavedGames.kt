package com.example.games_scoring_app.Pages

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.games_scoring_app.Data.Games
import com.example.games_scoring_app.Data.GamesRepository
import com.example.games_scoring_app.Data.SettingsRepository
import com.example.games_scoring_app.R
import com.example.games_scoring_app.Screen
import com.example.games_scoring_app.Theme.LeagueGothic
import com.example.games_scoring_app.Theme.black
import com.example.games_scoring_app.Theme.blue
import com.example.games_scoring_app.Theme.darkgray
import com.example.games_scoring_app.Theme.gray
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
import kotlinx.coroutines.launch

@Composable
fun SavedGamesPage(navController: NavController) {
    var noGames = true
    val scrollState = rememberScrollState()

    val applicationScope = CoroutineScope(SupervisorJob())
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context, applicationScope)

    // Create a coroutine scope to launch the delete operation
    val coroutineScope = rememberCoroutineScope()

    val gamesRepository = GamesRepository(database.gamesDao())
    val gamesViewModelFactory = GamesViewModelFactory(gamesRepository)
    val gamesViewModel: GamesViewModel = viewModel(factory = gamesViewModelFactory)
    val gameTypesRepository = GameTypesRepository(database.gameTypesDao())
    val gameTypesViewModelFactory = GameTypesViewModelFactory(gameTypesRepository)
    val gameTypesViewModel: GameTypesViewModel = viewModel(factory = gameTypesViewModelFactory)

    // --- MODIFIED --- Use the new StateFlow that includes players
    val allGamesWithPlayers by gamesViewModel.allGamesWithPlayers.collectAsState()
    val gameTypes by gameTypesViewModel.allGameTypes.collectAsState()

    // --- NEW: State to manage the number of visible games ---
    var gamesLimit by remember { mutableStateOf(10) }
    val initialGamesLimit = 10

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
        WidgetTitle("SCORES", R.drawable.papers, navController);
        Spacer(modifier = Modifier.height(20.dp))
        Row (
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom, // Aligned to bottom to look better
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 16.dp)
        ){
            Text(
                text = "Games Played",
                fontFamily = LeagueGothic,
                fontSize = 48.sp,
                color = fontColor,
                modifier = Modifier,
                textAlign = TextAlign.Left
            )
            Spacer(modifier = Modifier.size(8.dp)) // Increased spacing
            Text(
                text = "(${allGamesWithPlayers.size})",
                fontFamily = LeagueGothic,
                fontSize = 24.sp,
                color = gray,
                modifier = Modifier.padding(bottom = 4.dp), // Adjust padding for alignment
                textAlign = TextAlign.Left
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Column(Modifier.padding(horizontal = 16.dp)) {
            if (allGamesWithPlayers.isNotEmpty() && gameTypes.isNotEmpty()) {
                // --- MODIFIED: Use .take() to limit the number of games shown ---
                allGamesWithPlayers.take(gamesLimit).forEach { gameWithPlayers ->
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
                            players = gameWithPlayers.players,
                            onDelete = {
                                coroutineScope.launch {
                                    gamesViewModel.deleteGame(game)
                                }
                            },
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                // --- MODIFIED: "See More" and "See Less" buttons ---
                if (allGamesWithPlayers.size > initialGamesLimit) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // "See Less" button
                        if (gamesLimit > initialGamesLimit) {
                            Button(
                                onClick = { gamesLimit = (gamesLimit - 10).coerceAtLeast(initialGamesLimit) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = darkgray
                                )
                            ) {
                                Text(
                                    text = "SEE LESS",
                                    fontFamily = LeagueGothic,
                                    fontSize = 24.sp,
                                    color = white
                                )
                            }
                        }
                        // "See More" button
                        if (allGamesWithPlayers.size > gamesLimit) {
                            Button(
                                onClick = { gamesLimit += 10 },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = darkgray
                                )
                            ) {
                                Text(
                                    text = "SEE MORE",
                                    fontFamily = LeagueGothic,
                                    fontSize = 24.sp,
                                    color = white
                                )
                            }
                        }
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
        // Add some final padding at the bottom
        Spacer(modifier = Modifier.height(20.dp))
    }
}
