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
import com.example.games_scoring_app.Components.PageTitle
import com.example.games_scoring_app.Data.AppDatabase
import com.example.games_scoring_app.Data.GameTypesRepository
import com.example.games_scoring_app.Data.GamesRepository
import com.example.games_scoring_app.Data.PlayersRepository
import com.example.games_scoring_app.Data.ScoresRepository
import com.example.games_scoring_app.Games.GeneralaScoreboard
import com.example.games_scoring_app.Games.TrucoScoreboard
import com.example.games_scoring_app.R
import com.example.games_scoring_app.Screen
import com.example.games_scoring_app.Theme.LeagueGothic
import com.example.games_scoring_app.Theme.black
import com.example.games_scoring_app.Theme.white
import com.example.games_scoring_app.Theme.yellow
import com.example.games_scoring_app.Viewmodel.GameTypesViewModel
import com.example.games_scoring_app.Viewmodel.GameTypesViewModelFactory
import com.example.games_scoring_app.Viewmodel.GamesViewModel
import com.example.games_scoring_app.Viewmodel.GamesViewModelFactory
import com.example.games_scoring_app.Viewmodel.PlayersViewModel
import com.example.games_scoring_app.Viewmodel.PlayersViewModelFactory
import com.example.games_scoring_app.Viewmodel.ScoresViewModel
import com.example.games_scoring_app.Viewmodel.ScoresViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GamePage(navController: NavController, gameId: Int, new: Boolean) {
    val TAG = "GamePage"
    Log.d(TAG, "--- GAME STARTED ---")
    Log.d(TAG, "GamePage called with gameId: $gameId")
    val scrollState = rememberScrollState()

    val applicationScope = CoroutineScope(SupervisorJob())
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context, applicationScope)
    Log.d(TAG, "database: $database")
    val coroutineScope = rememberCoroutineScope()

    val gamesRepository = GamesRepository(database.gamesDao())
    val gamesViewModelFactory = GamesViewModelFactory(gamesRepository)
    val gamesViewModel: GamesViewModel = viewModel(factory = gamesViewModelFactory)

    val gameTypesRepository = GameTypesRepository(database.gameTypesDao())
    val gameTypesViewModelFactory = GameTypesViewModelFactory(gameTypesRepository)
    val gameTypesViewModel: GameTypesViewModel = viewModel(factory = gameTypesViewModelFactory)

    val playersRepository = PlayersRepository(database.playersDao())
    val playersViewModelFactory = PlayersViewModelFactory(playersRepository)
    val playersViewModel: PlayersViewModel = viewModel(factory = playersViewModelFactory)

    val scoresRepository = ScoresRepository(database.scoresDao())
    val scoresViewModelFactory = ScoresViewModelFactory(scoresRepository)
    val scoresViewModel: ScoresViewModel = viewModel(factory = scoresViewModelFactory)

    Log.d(TAG, "Viemodels setup finish")

    val game by gamesViewModel.currentGame.collectAsState()
    val lastGame by gamesViewModel.lastGame.collectAsState()
    val gameTypes by gameTypesViewModel.allGameTypes.collectAsState()
    val gameType = remember { mutableStateOf(gameTypesViewModel.emptyGameType()) }
    val players by playersViewModel.currentGamePlayers.collectAsState()
    val scores by scoresViewModel.allPlayersScores.collectAsState()
    Log.d(TAG, "Variables initialize")

    LaunchedEffect(key1 = Unit) {
        Log.d(TAG, "First LaunchedEffect called")
        gamesViewModel.getGameById(gameId)
        gameTypesViewModel.getAllGameTypes()
        Log.d(TAG, "First LaunchedEffect finish")
    }

    LaunchedEffect(key1 = game, key2 = gameTypes, key3 = game?.id_GameType) {
        Log.d(TAG, "Second LaunchedEffect called")
        if (game != null) {
            Log.d(TAG, "game is not null")
            val newGameType = gameTypesViewModel.getGameTypeById(game!!.id_GameType)!!
            gameType.value = newGameType
            playersViewModel.getPlayersByGameId(gameId)
        } else {
            Log.d(TAG, "game is null")
        }
        Log.d(TAG, "Second LaunchedEffect finish")
    }

    LaunchedEffect(key1 = players, new) {
        Log.d(TAG, "Third LaunchedEffect called")
        if (players.isNotEmpty()) {
            if (!new) {
                Log.d(TAG, "Not new")
                scoresViewModel.getAllPlayersScores(players)
                Log.d(TAG, "scores: $scores")
            } else {
                Log.d(TAG, "New")
                scoresViewModel.addEmtpyScoreToAllPlayers(players)
                scoresViewModel.getAllPlayersScores(players)
                Log.d(TAG, "scores: $scores")
            }
        } else {
            Log.d(TAG, "players is empty")
        }
        Log.d(TAG, "Third LaunchedEffect finish")
    }
    var titelImage = R.drawable.fondo_cartas_truco
    if(gameType.value.name == "Generala"){
        titelImage = R.drawable.dice_far
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(black)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        PageTitle(gameType.value.name.uppercase(), titelImage, navController)
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
                        val emptyGame = gamesViewModel.emptyGame()
                        emptyGame.id_GameType = gameType.value.id
                        gamesViewModel.addNewGame(emptyGame)
                        gamesViewModel.getLastGame()
                        // Wait until lastGame is not null, indicating the game is created.
                        while (lastGame == null) {
                            kotlinx.coroutines.delay(100) // short delay to avoid busy-waiting
                        }
                        Log.d(TAG, "lastGame: $lastGame")
                        for (player in players) {
                            player!!.id_game = lastGame!!.id
                            playersViewModel.addNewPlayer(player)
                            Log.d(TAG, "player: $player")
                        }
                        Log.d(TAG, "Navigating to Game screen")
                        navController.navigate(Screen.Game.createRoute(lastGame!!.id, true))

                    }
                }
            )
            if (scores.isNotEmpty() && players.isNotEmpty() ) {
                if(gameType.value.name.isNotEmpty() || gameType.value.name.isNotBlank()) {
                    Log.d(TAG, "gameType: ${gameType.value.name}")
                    Log.d(TAG, "scores: $scores")
                    Spacer(modifier = Modifier.height(20.dp))
                    if (gameType.value.name == "Generala") {
                        GeneralaScoreboard(players, scores, gameType.value.maxScore)
                    } else if (gameType.value.name == "Truco") {
                        TrucoScoreboard(players, scores, gameType.value.maxScore)
                    }
                } else {
                   Log.d(TAG, "gameType is empty")
                }

            } else {
                Spacer(modifier = Modifier.height(100.dp))
                Text(
                    text = "LOADING ...",
                    fontFamily = LeagueGothic,
                    fontSize = 60.sp,
                    color = white,
                    modifier = Modifier
                        .padding(horizontal = 32.dp)
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center
                )
            }
        }
    }

}