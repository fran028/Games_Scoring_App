package com.example.games_scoring_app.Pages

import android.content.ContentValues.TAG
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.isEmpty
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.games_scoring_app.Components.ButtonBar
import com.example.games_scoring_app.Components.IconButtonBar
import com.example.games_scoring_app.Components.LoadingMessage
import com.example.games_scoring_app.Components.PageTitle
import com.example.games_scoring_app.Components.PlayerAmountGrid
import com.example.games_scoring_app.Data.AppDatabase
import com.example.games_scoring_app.Data.GameTypesRepository
import com.example.games_scoring_app.Data.GamesRepository
import com.example.games_scoring_app.Data.Players
import com.example.games_scoring_app.Data.PlayersRepository
import com.example.games_scoring_app.R
import com.example.games_scoring_app.Screen
import com.example.games_scoring_app.Theme.LeagueGothic
import com.example.games_scoring_app.Theme.RobotoCondensed
import com.example.games_scoring_app.Theme.black
import com.example.games_scoring_app.Theme.blue
import com.example.games_scoring_app.Theme.green
import com.example.games_scoring_app.Theme.white
import com.example.games_scoring_app.Theme.yellow
import com.example.games_scoring_app.Viewmodel.GameTypesViewModel
import com.example.games_scoring_app.Viewmodel.GameTypesViewModelFactory
import com.example.games_scoring_app.Viewmodel.GamesViewModel
import com.example.games_scoring_app.Viewmodel.GamesViewModelFactory
import com.example.games_scoring_app.Viewmodel.PlayersViewModel
import com.example.games_scoring_app.Viewmodel.PlayersViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.collections.sliceArray

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SetupPage(navController: NavController, gameType: Int) {
    val TAG  = "SetupPage"
    Log.d(TAG, "SetupPage called")
    Log.d(TAG, "match_type: $gameType")
    val scrollState = rememberScrollState()

    val applicationScope = CoroutineScope(SupervisorJob())
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context, applicationScope)
    Log.d(TAG, "database: $database")

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

    Log.d(TAG, "Viemodels setup finish")

    val gameTypes by gameTypesViewModel.allGameTypes.collectAsState()
    //val lastGame by gamesViewModel.lastGame.collectAsState()
    //val emptyGame = gamesViewModel.emptyGame()

    val names = remember { mutableStateListOf<String>() }
    val thisGameType = remember { mutableStateOf(gameTypesViewModel.emptyGameType()) }
    val maxPlayers by remember { derivedStateOf { thisGameType.value.maxPlayers } }
    val minPlayers by remember { derivedStateOf { thisGameType.value.minPlayers } }
    var selectedPlayerCount by remember { mutableStateOf(0) }
    val defaultNames = listOf("P1", "P2", "P3", "P4", "P5", "P6", "P7", "P8", "P9", "P10")
    val showSetup = remember { mutableStateOf(false) }
    Log.d(TAG, "Variables initialize")

    LaunchedEffect(key1 = Unit) {
        Log.d(TAG, "First LaunchedEffect called")
        gameTypesViewModel.getAllGameTypes()
    }

    LaunchedEffect(key1 = gameTypes) {
        Log.d(TAG, "gameTypes LaunchedEffect called")
        if (gameTypes.isNotEmpty()) {
            Log.d(TAG, "gameTypes is not empty")
            val newGameType = gameTypesViewModel.getGameTypeById(gameType)!!
            thisGameType.value = newGameType // Update the State
            selectedPlayerCount = minPlayers
            Log.d(TAG, "selectedPlayerCount: $selectedPlayerCount")
            names.clear()
            repeat(maxPlayers) {
                names.add("")
            }
            kotlinx.coroutines.delay(1000)
            showSetup.value = true
        } else {
            Log.d(TAG, "gameTypes is empty")
        }
    }

    val allowedChars = remember { ('a'..'z') + ('A'..'Z') + ('0'..'9') } // Define allowed characters

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(black)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        if(showSetup.value) {
            PageTitle("GAME SETUP", R.drawable.games_retro, navController)
            Spacer(modifier = Modifier.height(28.dp))
            var buttonColor = yellow
            var textcolor = black
            var buttonIconId = 0
            when (thisGameType.value.type) {
                "Dados" -> {
                    buttonIconId = R.drawable.dices
                    buttonColor = blue
                    textcolor = black
                }
                "Cartas" -> {
                    buttonIconId = R.drawable.card
                    buttonColor = yellow
                    textcolor = black
                }
                "Generico" -> {
                    buttonIconId = R.drawable.paper
                    buttonColor = white
                    textcolor = black
                }
                else -> {
                    buttonIconId = R.drawable.paper
                    buttonColor = white
                    textcolor = black
                }
            }
            Column (Modifier.padding(horizontal = 30.dp )) {
                IconButtonBar(
                    text = thisGameType.value.name.uppercase(),
                    bgcolor = buttonColor,
                    height = 48.dp,
                    textcolor = textcolor,
                    onClick = { },
                    icon = buttonIconId,
                    iconSize = 32.dp,
                    doubleIcon = true
                )
            }

            if(thisGameType.value.name != "Truco") {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Player Amount",
                    fontFamily = LeagueGothic,
                    fontSize = 48.sp,
                    color = white,
                    modifier = Modifier
                        .padding(horizontal = 32.dp)
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Left
                )

                PlayerAmountGrid(
                    maxPlayers = maxPlayers,
                    minPlayers = minPlayers,
                    onPlayerAmountSelected = { amount ->
                        selectedPlayerCount = amount
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp),
                    selectedbgcolor = blue,
                    bgcolor = white
                )
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp )) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = if(thisGameType.value.name == "Truco") "Team Names" else "Player Names",
                    fontFamily = LeagueGothic,
                    fontSize = 48.sp,
                    color = white,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                        .padding(horizontal = 12.dp),
                    textAlign = TextAlign.Left
                )
                for (i in 0..maxPlayers-1) {
                    val isSelected = i < selectedPlayerCount
                    var inputcolor = white
                    if (isSelected && thisGameType.value.name != "Truco"){
                        inputcolor = blue
                    }

                    TextField(
                        value = names[i],
                        onValueChange = {
                            names[i] = it
                        },
                        enabled = isSelected,
                        placeholder = {
                            Text(
                                text = "Player Name",
                                style = TextStyle(
                                    color = black,
                                    fontSize = 32.sp,
                                    fontFamily = LeagueGothic
                                )
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .padding(horizontal = 12.dp, vertical = 0.dp),
                        shape = RoundedCornerShape(10.dp),
                        textStyle = TextStyle(
                            fontFamily = LeagueGothic,
                            fontSize = 32.sp,
                            color = black
                        ),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = inputcolor, // Background when not focused
                            focusedContainerColor = inputcolor, // Background when focused
                            unfocusedIndicatorColor = Color.Transparent, // Remove the underline when not focused
                            focusedIndicatorColor = black, // Remove the underline when focused
                            disabledContainerColor = white,
                            disabledIndicatorColor = Color.Transparent,
                            disabledTextColor = Color.Transparent,
                            disabledLabelColor = Color.Transparent,
                            disabledPlaceholderColor = Color.Transparent,
                            cursorColor = Color.Transparent,
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)

                    )
                    if(thisGameType.value.name != "Truco") {
                        Spacer(modifier = Modifier.height(6.dp))
                    } else {
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row (modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp), horizontalArrangement = Arrangement.Center) {
                    ButtonBar(
                        text = "START GAME",
                        bgcolor = green,
                        height = 48.dp,
                        textcolor = black,
                        onClick = {
                            Log.d(TAG, "START GAME button clicked")

                            coroutineScope.launch {

                                //gamesViewModel.addNewGame(emptyGame)
                                //kotlinx.coroutines.delay(500) // short delay to avoid busy-waiting
                                //gamesViewModel.getLastGame()
                                //kotlinx.coroutines.delay(500) // short delay to avoid busy-waiting
                                //val gameId = gamesViewModel.lastGame.value?.id ?: 0
                                //kotlinx.coroutines.delay(500)

                                // check player names up to selectedPlayerCount and replace with default if empty
                                for (i in 0 until selectedPlayerCount) {
                                    if (names[i].isEmpty()) {
                                        names[i] = defaultNames[i]
                                    }
                                }
                                val playerNamesToPass = names.take(selectedPlayerCount).toTypedArray()
                                Log.d(TAG, "Navigating to Game screen")
                                Log.d(TAG, "game_type: $gameType")
                                navController.navigate(Screen.Game.createRoute( gameTypeId = gameType, playerNames = playerNamesToPass))
                            }
                        }
                    )
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        } else {
            LoadingMessage("LOADING")
        }
    }
}