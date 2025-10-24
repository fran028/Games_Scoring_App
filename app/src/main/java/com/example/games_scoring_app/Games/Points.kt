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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.games_scoring_app.Data.PlayerWithScores
import com.example.games_scoring_app.Data.ScoreTypes
import com.example.games_scoring_app.Data.Scores
import com.example.games_scoring_app.Theme.LeagueGothic
import com.example.games_scoring_app.Theme.black
import com.example.games_scoring_app.Theme.green
import com.example.games_scoring_app.Theme.red
import com.example.games_scoring_app.Theme.white

@Composable
fun PuntosScoreboard(
    playersWithScores: List<PlayerWithScores>,
    scoreTypes: List<ScoreTypes>,
    maxScore: Int,
    themeMode: Int,
    onAddScore: (Scores) -> Unit,
    onUpdateScore: (Scores) -> Unit // Included for consistency, but may not be used
) {
    val TAG = "PuntosScoreboard"
    Log.d(TAG, "PuntosScoreboard called")

    val backgroundColor = if (themeMode == 0) black else white
    val fontColor = if (themeMode == 0) white else black
    val buttonColor = if (themeMode == 0) white else black
    val buttonFontColor = if (themeMode == 0) black else white

    // Find the relevant ScoreType for "Points", which is usually one "Final Score".
    val finalScoreType = scoreTypes.find { it.name == "Final Score" }

    if (playersWithScores.isEmpty() || finalScoreType == null) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize()
                .background(backgroundColor)
                .padding(0.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "LOADING DATA...",
                fontFamily = LeagueGothic,
                fontSize = 48.sp,
                color = fontColor,
                modifier = Modifier
            )
        }
    } else {
        val minWidth = 40.dp
        val maxWidth = (372 / playersWithScores.size - 6.4 * (playersWithScores.size - 1)).dp
        val width = if (maxWidth < minWidth) minWidth else maxWidth

        var showPopup by remember { mutableStateOf(false) }
        var selectedPlayer by remember { mutableStateOf<PlayerWithScores?>(null) }
        var inputValue by remember { mutableStateOf("") }

        if (showPopup && selectedPlayer != null) {
            AddScorePopup(
                onDismiss = { showPopup = false },
                onConfirm = {
                    val score = inputValue.toIntOrNull()
                    if (score != null) {
                        Log.d(TAG, "Score: $score")
                        // Create a new Score object to be added to the database
                        val newScore = Scores(
                            id_player = selectedPlayer!!.player.id,
                            id_score_type = finalScoreType.id,
                            score = score,
                            isFinalScore = false // These are individual entries, not a final total
                        )
                        // Call the lambda to trigger the ViewModel to save the score
                        onAddScore(newScore)
                    }
                    showPopup = false
                    inputValue = ""
                },
                inputValue = inputValue,
                onInputValueChange = { inputValue = it },
                playername = selectedPlayer!!.player.name,
                buttonColor = buttonColor,
                buttonFontColor = buttonFontColor
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .padding(0.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier
                    .width(372.dp)
                    .padding(0.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                playersWithScores.forEach { playerWithScores ->
                    val playerName = if (playersWithScores.size > 2) playerWithScores.player.name.take(2) else playerWithScores.player.name
                    PlayerPuntosColumn(
                        playerName = playerName,
                        scores = playerWithScores.scores, // Pass the list of Score objects directly
                        maxScore = maxScore,
                        width = width,
                        onAddScoreClicked = {
                            selectedPlayer = playerWithScores
                            showPopup = true
                        },
                        buttonColor = buttonColor,
                        buttonFontColor = buttonFontColor,
                        fontColor = fontColor
                    )
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun PlayerPuntosColumn(
    playerName: String,
    scores: List<Scores>, // Changed type to list of Score objects
    maxScore: Int,
    width: Dp,
    onAddScoreClicked: () -> Unit,
    buttonColor: Color,
    buttonFontColor: Color,
    fontColor: Color
) {
    val totalScore = TotalScore(scores)
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .height(500.dp) // Give the column a fixed height to make it scrollable
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
                    if (maxScore > 0 && totalScore >= maxScore) red else buttonColor,
                    shape = RoundedCornerShape(7.5.dp)
                )
                .padding(2.5.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = playerName,
                fontFamily = LeagueGothic,
                fontSize = 24.sp,
                color = buttonFontColor,
                textAlign = TextAlign.Center,
            )
        }
        Spacer(modifier = Modifier.height(6.dp))

        // This inner column will contain the scrollable scores
        Column(modifier = Modifier.weight(1f).verticalScroll(scrollState)) {
            scores.forEach { score ->
                Text(
                    text = score.score.toString(),
                    fontFamily = LeagueGothic,
                    fontSize = 36.sp,
                    color = fontColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(6.dp))
            }
        }

        Spacer(modifier = Modifier.height(6.dp))
        // Total score and add button are outside the scrollable area
        Text(
            text = totalScore.toString(),
            fontFamily = LeagueGothic,
            fontSize = 36.sp,
            color = if (maxScore > 0 && totalScore >= maxScore) red else green,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .width(width)
                .height(45.dp)
                .background(buttonColor, shape = RoundedCornerShape(7.5.dp))
                .padding(2.5.dp)
                .clickable { onAddScoreClicked() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "+",
                fontFamily = LeagueGothic,
                fontSize = 24.sp,
                color = buttonFontColor,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun AddScorePopup(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    inputValue: String,
    onInputValueChange: (String) -> Unit,
    playername: String,
    buttonColor: Color,
    buttonFontColor: Color
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Score to $playername") },
        containerColor = buttonColor,
        textContentColor = buttonFontColor,
        titleContentColor = buttonFontColor,
        text = {
            OutlinedTextField(
                value = inputValue,
                onValueChange = onInputValueChange,
                label = { Text("Enter score") },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = buttonColor,
                    focusedContainerColor = buttonColor,
                    focusedIndicatorColor = buttonFontColor,
                    disabledContainerColor = buttonColor,
                    focusedTextColor = buttonFontColor,
                    unfocusedTextColor = buttonFontColor,
                    cursorColor = buttonFontColor,
                ),
            )
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(text = "Add")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = "Cancel")
            }
        }
    )
}

// Updated to work with a list of Score objects
private fun TotalScore(scores: List<Scores>): Int {
    return scores.sumOf { it.score }
}
