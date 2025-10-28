package com.example.games_scoring_app.Pages

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.navigation.NavController
import com.example.games_scoring_app.Components.ButtonBar
import com.example.games_scoring_app.Components.PageTitle
import com.example.games_scoring_app.Components.PlayerAmountGrid
import com.example.games_scoring_app.R
import com.example.games_scoring_app.Screen
import com.example.games_scoring_app.Theme.LeagueGothic
import com.example.games_scoring_app.Theme.black
import com.example.games_scoring_app.Theme.blue
import com.example.games_scoring_app.Theme.white
import com.example.games_scoring_app.Theme.yellow
import kotlinx.coroutines.delay

@Composable
fun RollDicePage(navController: NavController){
    val TAG = "RollDicePage"
    Log.d(TAG, "RollDicePage called")

    val scrollState = rememberScrollState()

    var selectedDiceCount by remember { mutableStateOf(1) }
    val maxDiceCount = 5
    val minDiceCount = 1

    var diceValues by remember { mutableStateOf(List(selectedDiceCount) { 0 }) }
    var rollDice by remember { mutableStateOf(false) }

    val configuration = LocalConfiguration.current
    val screenWidth = remember { configuration.screenWidthDp.dp }

    val diceSize = remember(selectedDiceCount, screenWidth) {
        (screenWidth - 32.dp - (selectedDiceCount * 16.dp)) / selectedDiceCount
    }
    val diceCorner = remember(selectedDiceCount) {
        (75.dp / selectedDiceCount )
    }
    Log.d(TAG, "diceSize: $diceSize")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(black)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        PageTitle("GAME SETUP", R.drawable.dice_far, navController)
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "DICE TO ROLL",
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
        PlayerAmountGrid(
            maxPlayers = maxDiceCount,
            minPlayers = minDiceCount,
            onPlayerAmountSelected = { amount ->
                selectedDiceCount = amount
            },
            selectedbgcolor = blue,
            bgcolor = white
        )
        Spacer(modifier = Modifier.height(20.dp))
        Column(modifier = Modifier.padding(horizontal = 32.dp)) {
            ButtonBar(
                onClick = {
                    rollDice = true
                },
                text = "ROLL DICE",
                bgcolor = yellow,
                height = 48.dp,
                textcolor = black,
            )
            LaunchedEffect(rollDice) {
                if (rollDice) {
                    val targetValues = List(selectedDiceCount) { (1..6).random() }
                    val animationFrames = 10
                    for (i in 0 until animationFrames) {
                        diceValues = List(selectedDiceCount) { (1..6).random() }
                        delay(50)
                    }
                    diceValues = targetValues
                    rollDice = false
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                for (value in diceValues.take(selectedDiceCount)) {
                    Box(
                        modifier = Modifier
                            .width(diceSize)
                            .height(diceSize)
                            .background(white, shape = RoundedCornerShape(diceCorner)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = value.toString(),
                            fontFamily = LeagueGothic,
                            fontSize = 48.sp,
                            color = black,
                            textAlign = TextAlign.Right,
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                }
            }

        }
    }
}