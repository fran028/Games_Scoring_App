package com.example.games_scoring_app.Games

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.games_scoring_app.Data.Players
import com.example.games_scoring_app.Theme.LeagueGothic
import com.example.games_scoring_app.Theme.black
import com.example.games_scoring_app.Theme.green
import com.example.games_scoring_app.Theme.red
import com.example.games_scoring_app.Theme.white
import kotlin.text.toIntOrNull

@Composable
fun PuntosScoreboard(players: Array<String>, maxScore: Int) {
    val TAG = "PuntosScoreboard"
    Log.d(TAG, "PuntosScoreboard called")
    var emptyPlayers = false

    for (player in players) {
        if (player == null) {
            emptyPlayers = true
            break
        }
    }
    if (emptyPlayers) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize()
                .background(black)
                .padding(0.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "DATA ERROR",
                fontFamily = LeagueGothic,
                fontSize = 48.sp,
                color = white,
                modifier = Modifier
            )
        }
    } else {
        val playerScores = remember {
            List(players.size) { mutableStateOf(emptyList<Int>()) }
        }
        val minwidth = 40.dp
        val maxwidth = (372 / players.size - 6.4 * (players.size-1)).dp
        Log.d(TAG, "player size: ${players.size}")
        Log.d(TAG, "maxwidth: $maxwidth")
        val width = if (maxwidth < minwidth) minwidth else maxwidth
        var showPopup by remember { mutableStateOf(false) }
        var selectedPlayerIndex by remember { mutableStateOf(-1) }
        var inputValue by remember { mutableStateOf("") }

        if (showPopup && selectedPlayerIndex >= 0) {
            AddScorePopup(
                onDismiss = { showPopup = false },
                onConfirm = {
                    val score = inputValue.toIntOrNull()
                    if (score != null) {
                        Log.d(TAG, "Score: $score")
                        val currentScores = playerScores[selectedPlayerIndex].value.toMutableList()
                        currentScores.add(score)
                        playerScores[selectedPlayerIndex].value = currentScores // Update the MutableState
                    }
                    showPopup = false
                    inputValue = ""
                },
                inputValue = inputValue,
                onInputValueChange = { inputValue = it },
                playername = players[selectedPlayerIndex]
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(black)
                .padding(0.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            if (players.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .width(372.dp)
                        .padding(0.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    for (i in players.indices) {
                        val playerName = if (players.size > 2) players[i].substring( 0, 2 ) else players[i]
                        PlayerPuntosColumn(
                            playerName,
                            playerScores[i],
                            maxScore,
                            width,
                            onAddScoreClicked = {
                                selectedPlayerIndex = i
                                showPopup = true
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun PlayerPuntosColumn(
    playerName: String,
    scores: MutableState<List<Int>>, // Changed type
    maxScore: Int,
    width: Dp,
    onAddScoreClicked: () -> Unit
) {
    val scoresList by scores // Observe the MutableState
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(width)
            .padding(0.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .width(width)
                .height(45.dp)
                .background(
                    /*if (TotalScore(scoresList) >= maxScore) red else*/ white, // Use scoresList here
                    shape = RoundedCornerShape(7.5.dp)
                )
                .padding(2.5.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = playerName,
                fontFamily = LeagueGothic,
                fontSize = 24.sp,
                color = black,
                textAlign = TextAlign.Right,
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        for (score in scoresList) {
            Text(
                text = score.toString(),
                fontFamily = LeagueGothic,
                fontSize = 36.sp,
                color = white,
                textAlign = TextAlign.Right,
            )

            Spacer(modifier = Modifier.height(6.dp))
        }
        Text(
            text = TotalScore(scoresList).toString(),
            fontFamily = LeagueGothic,
            fontSize = 36.sp,
            color = /*if (TotalScore(scoresList) >= maxScore) red else */green,
            textAlign = TextAlign.Right,
        )
        Box(
            modifier = Modifier
                .width(width)
                .height(45.dp)
                .background(white, shape = RoundedCornerShape(7.5.dp))
                .padding(2.5.dp)
                .clickable { onAddScoreClicked() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "+",
                fontFamily = LeagueGothic,
                fontSize = 24.sp,
                color = black,
                textAlign = TextAlign.Right,
            )
        }
    }
}

@Composable
fun AddScorePopup(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    inputValue: String,
    onInputValueChange: (String) -> Unit,
    playername: String
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Score to $playername") },
        text = {
            OutlinedTextField(
                value = inputValue,
                onValueChange = onInputValueChange,
                label = { Text("Enter score") }
            )
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

fun TotalScore(scores: List<Int>): Int {
    var total = 0
    for (score in scores) {
        total += score
    }
    return total
}