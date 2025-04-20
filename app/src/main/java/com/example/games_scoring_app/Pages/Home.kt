package com.example.games_scoring_app.Pages

import androidx.compose.foundation.Image
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.games_scoring_app.Components.ButtonDateBar
import com.example.games_scoring_app.Components.ButtonBar
import com.example.games_scoring_app.R
import com.example.games_scoring_app.Components.PageTitle
import com.example.games_scoring_app.Data.AppDatabase
import com.example.games_scoring_app.Data.GameTypesRepository
import com.example.games_scoring_app.Data.GamesRepository
import com.example.games_scoring_app.Screen
import com.example.games_scoring_app.Theme.black
import com.example.games_scoring_app.Theme.white
import com.example.games_scoring_app.Theme.LeagueGothic
import com.example.games_scoring_app.Theme.blue
import com.example.games_scoring_app.Theme.yellow
import com.example.games_scoring_app.Viewmodel.GameTypesViewModel
import com.example.games_scoring_app.Viewmodel.GameTypesViewModelFactory
import com.example.games_scoring_app.Viewmodel.GamesViewModel
import com.example.games_scoring_app.Viewmodel.GamesViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob


@Composable
fun HomePage(navController: NavController) {
    val appName = stringResource(id = R.string.app_name)
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

    val lastGame by gamesViewModel.lastGame.collectAsState()
    val gameTypes by gameTypesViewModel.allGameTypes.collectAsState()

    var lastGameType by remember { mutableStateOf("") }
    LaunchedEffect(key1 = Unit) {
        gamesViewModel.getLastGame()
        gameTypesViewModel.getAllGameTypes()
    }

    LaunchedEffect(key1 = lastGame) {
        if (lastGame != null) {
            noGames = false
            lastGameType = gameTypesViewModel.getGameTypeNameById(lastGame!!.id_GameType) ?: ""
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(black)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        PageTitle(appName.uppercase(), R.drawable.game_topview, navController);

        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "LAST GAME PLAYED",
            fontFamily = LeagueGothic,
            fontSize = 48.sp,
            color = white,
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Left
        )
        Spacer(modifier = Modifier.height(20.dp))
        Column (Modifier.padding(horizontal = 30.dp )) {
            if (lastGame!= null){
                ButtonDateBar(
                    text = lastGameType.uppercase(),
                    onClick = { navController.navigate(Screen.Game.createRoute(lastGame!!.id,true, lastGame!!.id_GameType)) },
                    bgcolor = blue,
                    height = 50.dp,
                    textcolor = black,
                    value = "${lastGame?.date}"
                )
            } else {
                ButtonDateBar(
                    text = "NO GAME",
                    bgcolor = white,
                    height = 64.dp,
                    textcolor = black,
                    value = "00/00/00",
                    onClick = {  }
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "GAMES",
            fontFamily = LeagueGothic,
            fontSize = 48.sp,
            color = white,
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Left
        )
        Spacer(modifier = Modifier.height(20.dp))
        Column (Modifier.padding(horizontal = 30.dp )) {
            if (gameTypes.isNotEmpty()) {
                for (gameType in gameTypes) {
                    if (gameType != null) {
                        ButtonBar(
                            text = gameType.name.uppercase(),
                            bgcolor = yellow,
                            height = 48.dp,
                            textcolor = black,
                            onClick = { navController.navigate(Screen.SetUp.createRoute(gameType.id)) },
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
            ButtonBar(
                text = "ROLL DICE",
                bgcolor = yellow,
                height = 48.dp,
                textcolor = black,
                onClick = { navController.navigate(Screen.RollDice.route) },
            )
            Spacer(modifier = Modifier.height(20.dp))
            /*ButtonBar(
                text = "TRUCO",
                bgcolor = yellow,
                height = 48.dp,
                textcolor = black,
                onClick = { navController.navigate(Screen.SetUp.createRoute(0)) },
            )
            Spacer(modifier = Modifier.height(20.dp))
            ButtonBar(
                text = "GENERALA",
                bgcolor = yellow,
                height = 48.dp,
                textcolor = black,
                onClick = { navController.navigate(Screen.SetUp.createRoute(1)) },
            )*/
        }

    }
}