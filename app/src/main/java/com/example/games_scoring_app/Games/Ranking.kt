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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.games_scoring_app.Theme.LeagueGothic
import com.example.games_scoring_app.Theme.black
import com.example.games_scoring_app.Theme.blue
import com.example.games_scoring_app.Theme.copper
import com.example.games_scoring_app.Theme.green
import com.example.games_scoring_app.Theme.red
import com.example.games_scoring_app.Theme.silver
import com.example.games_scoring_app.Theme.white
import com.example.games_scoring_app.Theme.yellow


@Composable
fun RankingScoreboard(players: Array<String>, themeMode: Int) {
    val TAG = "RankingScoreboard"
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
        val playerScores = remember {
            List(players.size) { mutableStateOf(emptyList<Int>()) }
        }
        val minwidth = 40.dp
        val maxwidth = (372 / players.size - 6.4 * (players.size-1)).dp
        Log.d(TAG, "player size: ${players.size}")
        Log.d(TAG, "maxwidth: $maxwidth")
        val width = if (maxwidth < minwidth) minwidth else maxwidth
        var showPopup by remember { mutableStateOf(false) }
        var selectedPlayerIndex by remember { mutableStateOf(0) }
        var inputValue by remember { mutableStateOf("") }

        val rankingAssignments = remember {
            mutableStateOf(List(players.size) { -1 }) // Initialize all slots to unassigned (-1)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .padding(0.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            if (players.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .width(372.dp)
                        .padding(0.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        modifier = Modifier.padding(0.dp).fillMaxWidth(),
                        text = "Select from Player List",
                        fontFamily = LeagueGothic,
                        fontSize = 32.sp,
                        color = fontColor,
                        textAlign = TextAlign.Left,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        players.forEachIndexed { index, player -> // Use forEachIndexed for clarity
                            val playerNameDisplay =
                                if (players.size > 2 && player.length > 2) player.substring(0, 2) else player
                            PlayerRankingColumn(
                                playerName = playerNameDisplay,
                                columnWidth = width,
                                playerIndex = index,
                                isSelected = selectedPlayerIndex == index,
                                onPlayerClicked = { clickedPlayerIndex ->
                                    selectedPlayerIndex = clickedPlayerIndex
                                },
                                buttonColor = buttonColor,
                                buttonFontColor = buttonFontColor
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        modifier = Modifier.padding(0.dp).fillMaxWidth(),
                        text = "Players Ranking",
                        fontFamily = LeagueGothic,
                        fontSize = 32.sp,
                        color = fontColor,
                        textAlign = TextAlign.Left,
                    )
                    val prefixList = listOf("st","nd","rd","th")
                    val numberOfRankingSlots = players.size
                    for (rankIndex in 0 until numberOfRankingSlots) {
                        val currentRank = rankIndex + 1 // 1 for 1st, 2 for 2nd...
                        val assignedPlayerOriginalIndex = rankingAssignments.value[rankIndex]
                        val displayPlayerName = if (assignedPlayerOriginalIndex != -1) {
                            players.getOrNull(assignedPlayerOriginalIndex) ?: "ERROR" // Get player name or show error
                        } else {
                            "Tap to assign" // Placeholder if no player assigned
                        }
                        val rankText = if (currentRank > 3) "$currentRank${prefixList[3]}" else "$currentRank${prefixList[currentRank - 1]}"

                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(45.dp)
                                .clickable(enabled = selectedPlayerIndex != -1) {
                                    if (selectedPlayerIndex != -1) {
                                        val newAssignments = rankingAssignments.value.toMutableList()
                                        newAssignments[rankIndex] = selectedPlayerIndex
                                        rankingAssignments.value = newAssignments
                                        Log.d(TAG, "Assigned player ${players[selectedPlayerIndex]} to rank $currentRank")
                                    }
                                },
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(46.dp)
                                    .height(45.dp)
                                    .background(backgroundColor),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = rankText,
                                    fontFamily = LeagueGothic,
                                    fontSize = 36.sp,
                                    color = fontColor,
                                    textAlign = TextAlign.Center,
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Box(
                                modifier = Modifier
                                    .weight(1f) // Takes remaining width
                                    .height(45.dp)
                                    .background(
                                        when (currentRank) {
                                            1 -> yellow
                                            2 -> silver
                                            3 -> copper
                                            numberOfRankingSlots -> red
                                            else -> buttonColor
                                        },
                                        shape = RoundedCornerShape(7.5.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 2.5.dp), // Added horizontal padding
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = displayPlayerName.uppercase(), // Display assigned player's name or placeholder
                                    fontFamily = LeagueGothic,
                                    fontSize = 24.sp,
                                    color = if (currentRank != 1 && currentRank != 2 && currentRank != 3 && currentRank != numberOfRankingSlots ) buttonFontColor else black,
                                    textAlign = TextAlign.Center, // Keep center for the text within this box
                                    maxLines = 1,
                                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis // Handle long names
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
private fun PlayerRankingColumn(
    playerName: String,
    columnWidth: Dp,
    playerIndex: Int,
    isSelected: Boolean, // New parameter: whether this specific player button is the selected one
    onPlayerClicked: (Int) -> Unit, // New callback: reports WHICH player index was clicked
    buttonColor: Color,
    buttonFontColor: Color

) {
     // Observe the MutableState
    Box(
        modifier = Modifier
            .width(columnWidth)
            .height(45.dp)
            .background(
                if (isSelected) blue else buttonColor,
                shape = RoundedCornerShape(7.5.dp)
            )
            .padding(2.5.dp)
            .clickable {
                onPlayerClicked(playerIndex)
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = playerName,
            fontFamily = LeagueGothic,
            fontSize = 24.sp,
            color = if (isSelected) black else buttonFontColor,
            textAlign = TextAlign.Center,
        )
    }
    Spacer(modifier = Modifier.height(6.dp))
}



