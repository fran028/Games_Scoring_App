package com.example.games_scoring_app.Pages

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.games_scoring_app.Components.ButtonBar
import com.example.games_scoring_app.Components.LoadingMessage
import com.example.games_scoring_app.Components.PageTitle
import com.example.games_scoring_app.Data.AppDatabase
import com.example.games_scoring_app.Data.GameTypes
import com.example.games_scoring_app.Data.GameTypesRepository
import com.example.games_scoring_app.Data.GamesRepository
import com.example.games_scoring_app.Data.Players
import com.example.games_scoring_app.Data.PlayersRepository
import com.example.games_scoring_app.Data.ScoresRepository
import com.example.games_scoring_app.Data.SettingsRepository
import com.example.games_scoring_app.Games.GeneralaScoreboard
import com.example.games_scoring_app.Games.LevelsScoreboard
import com.example.games_scoring_app.Games.PuntosScoreboard
import com.example.games_scoring_app.Games.TrucoScoreboard
import com.example.games_scoring_app.Games.RankingScoreboard
import com.example.games_scoring_app.R
import com.example.games_scoring_app.Screen
import com.example.games_scoring_app.Theme.LeagueGothic
import com.example.games_scoring_app.Theme.black
import com.example.games_scoring_app.Theme.blue
import com.example.games_scoring_app.Theme.cream
import com.example.games_scoring_app.Theme.white
import com.example.games_scoring_app.Theme.yellow
import com.example.games_scoring_app.Viewmodel.GameTypesViewModel
import com.example.games_scoring_app.Viewmodel.GameTypesViewModelFactory
import com.example.games_scoring_app.Viewmodel.SettingsViewModel
import com.example.games_scoring_app.Viewmodel.SettingsViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GamePage(navController: NavController, gameTypeId: Int, playerNames: Array<String> ) {
    if(gameTypeId == 0) {
        Log.d("GamePage", "Navigating to Home screen")
        navController.navigate(Screen.Home.route)
    }
    val TAG = "GamePage"
    Log.d(TAG, "--- GAME STARTED ---")
    val scrollState = rememberScrollState()

    val applicationScope = CoroutineScope(SupervisorJob())
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context, applicationScope)
    val coroutineScope = rememberCoroutineScope()

    /*val gamesRepository = GamesRepository(database.gamesDao())
    val gamesViewModelFactory = GamesViewModelFactory(gamesRepository)
    val gamesViewModel: GamesViewModel = viewModel(factory = gamesViewModelFactory)*/

    val gameTypesRepository = GameTypesRepository(database.gameTypesDao())
    val gameTypesViewModelFactory = GameTypesViewModelFactory(gameTypesRepository)
    val gameTypesViewModel: GameTypesViewModel = viewModel(factory = gameTypesViewModelFactory)

    /*val playersRepository = PlayersRepository(database.playersDao())
    val playersViewModelFactory = PlayersViewModelFactory(playersRepository)
    val playersViewModel: PlayersViewModel = viewModel(factory = playersViewModelFactory)*/

    val settingsRepository = SettingsRepository(database.settingsDao())
    val settingsViewModelFactory = SettingsViewModelFactory(settingsRepository)
    val settingsViewModel: SettingsViewModel = viewModel(factory = settingsViewModelFactory)

    Log.d(TAG, "Viemodels setup finish")


    //val lastGame by gamesViewModel.lastGame.collectAsState()
    val gameType= remember { mutableStateOf<GameTypes?>(null) }
    //val players = remember { mutableStateOf<List<Players>?>(null) }
    val showScoreboard = remember { mutableStateOf(false) }
    val themeMode by settingsViewModel.themeMode.collectAsState()
    Log.d(TAG, "Variables initialize")

    LaunchedEffect(key1 = Unit) {
        Log.d(TAG, "First LaunchedEffect called")
        //players.value = playersViewModel.getPlayerByGameId(gameId)
        gameType.value = gameTypesViewModel.getGameTypeById(gameTypeId)
        showScoreboard.value = true
        settingsViewModel.getThemeMode()
        Log.d(TAG, "First LaunchedEffect finish")
    }

    val titelImage = remember { mutableStateOf(R.drawable.fondo_cartas_truco) }

    val backgroundColor = if (themeMode == 0) black else cream
    val fontColor = if (themeMode == 0) white else black
    val buttonColor = if (themeMode == 0) white else black
    val buttonFontColor = if (themeMode == 0) black else white


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        if (showScoreboard.value) {
            when (gameType.value!!.name) {
                "Generala" -> {
                    titelImage.value = R.drawable.dice_far
                }
                "Truco" -> {
                    titelImage.value = R.drawable.fondo_cartas_truco
                }
                "Points" -> {
                    titelImage.value = R.drawable.papers
                }
                "Ranking" -> {
                    titelImage.value = R.drawable.papers
                    }
                "Levels" -> {
                    titelImage.value = R.drawable.papers
                }
            }
            PageTitle(gameType.value!!.name.uppercase(), titelImage.value, navController)
            Spacer(modifier = Modifier.height(20.dp))

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                ButtonBar(
                    text = "NEW GAME",
                    bgcolor = yellow,
                    height = 48.dp,
                    textcolor = black,
                    onClick = {
                        Log.d(TAG, "NEW GAME button clicked")
                        coroutineScope.launch {
                            /*val emptyGame = gamesViewModel.emptyGame()
                            emptyGame.id_GameType = gameType.value!!.id
                            gamesViewModel.addNewGame(emptyGame)
                            gamesViewModel.getLastGame()
                            // Wait until lastGame is not null, indicating the game is created.
                            while (lastGame == null) {
                                kotlinx.coroutines.delay(100) // short delay to avoid busy-waiting
                            }
                            Log.d(TAG, "lastGame: $lastGame")
                            for (player in players.value!!) {
                                player.id_game = lastGame!!.id
                                playersViewModel.addNewPlayer(player)
                                Log.d(TAG, "player: $player")
                            }
                            Log.d(TAG, "Navigating to Game screen")
                             */
                            navController.navigate(Screen.Game.createRoute(gameTypeId, playerNames))

                        }
                    }
                )
                if (gameType.value!!.name.isNotBlank()) {

                    Spacer(modifier = Modifier.height(20.dp))
                    Log.d(TAG, "gameType: ${gameType.value!!.name}")
                    when (gameType.value!!.name) {
                        "Generala" -> {
                            GeneralaScoreboard(playerNames, themeMode)
                        }
                        "Truco" -> {
                            TrucoScoreboard(playerNames, gameType.value!!.maxScore, themeMode)
                        }
                        "Points" -> {
                            PuntosScoreboard(playerNames, gameType.value!!.maxScore, themeMode)
                        }
                        "Ranking" -> {
                            RankingScoreboard(playerNames, themeMode)
                        }
                        "Levels" -> {
                            LevelsScoreboard(playerNames, themeMode)
                        }
                    }
                } else {
                    Log.d(TAG, "gameType is empty")
                }
            }
        }  else {
            LoadingMessage("LOADING", themeMode)
        }
    }

}