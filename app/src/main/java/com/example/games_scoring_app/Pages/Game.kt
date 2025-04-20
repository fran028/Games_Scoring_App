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
import com.example.games_scoring_app.Games.GeneralaScoreboard
import com.example.games_scoring_app.Games.PedroScoreboard
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
fun GamePage(navController: NavController, gameId: Int, gameTypeId: Int, new: Boolean) {
    if(gameTypeId == 0) {
        Log.d("GamePage", "Navigating to Home screen")
        navController.navigate(Screen.Home.route)
    }
    val TAG = "GamePage"
    Log.d(TAG, "--- GAME STARTED ---")
    Log.d(TAG, "GamePage called with gameId: $gameId")
    val scrollState = rememberScrollState()

    val applicationScope = CoroutineScope(SupervisorJob())
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context, applicationScope)
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

    Log.d(TAG, "Viemodels setup finish")


    val lastGame by gamesViewModel.lastGame.collectAsState()
    val gameType= remember { mutableStateOf<GameTypes?>(null) }
    val players = remember { mutableStateOf<List<Players>?>(null) }
    val showScoreboard = remember { mutableStateOf(false) }
    Log.d(TAG, "Variables initialize")

    LaunchedEffect(key1 = Unit) {
        Log.d(TAG, "First LaunchedEffect called")
        players.value = playersViewModel.getPlayerByGameId(gameId)
        gameType.value = gameTypesViewModel.getGameTypeById(gameTypeId)
        showScoreboard.value = true
        Log.d(TAG, "First LaunchedEffect finish")
    }

    val titelImage = remember { mutableStateOf(R.drawable.dice_far) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(black)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        if (showScoreboard.value) {
            if(gameType.value!!.name == "Generala"){
                titelImage.value = R.drawable.dice_far
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
                            val emptyGame = gamesViewModel.emptyGame()
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
                            navController.navigate(Screen.Game.createRoute(lastGame!!.id,true, gameTypeId))

                        }
                    }
                )
                if (gameType.value!!.name.isNotBlank()) {
                    Log.d(TAG, "gameType: ${gameType.value!!.name}")
                    Spacer(modifier = Modifier.height(20.dp))
                    if (gameType.value!!.name == "Generala") {
                        GeneralaScoreboard(players.value!!)
                    } else if (gameType.value!!.name == "Truco") {
                        TrucoScoreboard(players.value!!, gameType.value!!.maxScore)
                    } else if (gameType.value!!.name == "Pedro") {
                        PedroScoreboard(players.value!!, gameType.value!!.maxScore)
                    }
                } else {
                    Log.d(TAG, "gameType is empty")
                }
            }
        }  else {
            LoadingMessage("LOADING PAGE . . .")
        }
    }

}