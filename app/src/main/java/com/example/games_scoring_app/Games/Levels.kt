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
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.games_scoring_app.Theme.LeagueGothic
import com.example.games_scoring_app.Theme.black
import com.example.games_scoring_app.Theme.blue
import com.example.games_scoring_app.Theme.green
import com.example.games_scoring_app.Theme.red
import com.example.games_scoring_app.Theme.white


@Composable
fun LevelsScoreboard(players: Array<String>, themeMode: Int) {
    val TAG = "LevelsScoreboard"
    Log.d(TAG, "PuntosScoreboard called")
    var emptyPlayers = false

    for (player in players) {
        if (player == null) {
            emptyPlayers = true
            break
        }
    }

    val backgroundColor = if (themeMode == 0) black else white
    val fontColor = if (themeMode == 0) white else black
    val buttonColor = if (themeMode == 0) white else black
    val buttonFontColor = if (themeMode == 0) black else white

    if (emptyPlayers) {
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
                text = "DATA ERROR",
                fontFamily = LeagueGothic,
                fontSize = 48.sp,
                color = fontColor,
                modifier = Modifier
            )
        }
    } else {
        val playerLevels = remember { List(players.size) { 0 }.toMutableStateList() }
        val numPlayers = players.size
        val minwidth = 40.dp
        val maxwidth = (372 / players.size - 6.4 * (players.size-1)).dp
        Log.d(TAG, "player size: ${players.size}")
        Log.d(TAG, "maxwidth: $maxwidth")
        val width = if (maxwidth < minwidth) minwidth else maxwidth



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
                players.forEachIndexed { index, player ->
                    val playerNameDisplay =
                        if (numPlayers > 3 && player.length > 3) player.substring(0, 3) else player
                    PlayerLevelColumn(
                        playerName = playerNameDisplay,
                        currentLevel = playerLevels[index],
                        width = width,
                        onLevelUp = {
                            playerLevels[index] = playerLevels[index] + 1
                        },
                        onLevelDown = {
                            if (playerLevels[index] > 0) { // Assuming level cannot go below 0
                                playerLevels[index] = playerLevels[index] - 1
                            }
                        },
                        buttonColor = buttonColor,
                        buttonFontColor = buttonFontColor
                    )
                }
            }
        }
    }
}

@Composable
private fun PlayerLevelColumn(
    playerName: String,
    width: Dp,
    currentLevel: Int,
    onLevelUp: () -> Unit,
    onLevelDown: () -> Unit,
    buttonColor: Color,
    buttonFontColor: Color
) {
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
                    blue,
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
        Box(
            modifier = Modifier
                .width(width)
                .height(45.dp)
                .background(
                    red,
                    shape = RoundedCornerShape(7.5.dp)
                )
                .padding(2.5.dp)
                .clickable {
                    onLevelDown()
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "-",
                fontFamily = LeagueGothic,
                fontSize = 32.sp,
                color = black,
                textAlign = TextAlign.Right,
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        for (level in 1..currentLevel) {
            Box(
                modifier = Modifier
                    .width(width)
                    .height(45.dp)
                    .background(
                        /*if (TotalScore(scoresList) >= maxScore) red else*/ buttonColor, // Use scoresList here
                        shape = RoundedCornerShape(7.5.dp)
                    )
                    .padding(2.5.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "",
                    fontFamily = LeagueGothic,
                    fontSize = 24.sp,
                    color = buttonFontColor,
                    textAlign = TextAlign.Right,
                )
            }

            Spacer(modifier = Modifier.height(6.dp))
        }
        Box(
            modifier = Modifier
                .width(width)
                .height(45.dp)
                .background(green, shape = RoundedCornerShape(7.5.dp))
                .padding(2.5.dp)
                .clickable { onLevelUp() },
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
