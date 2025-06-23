package com.example.games_scoring_app.Games


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.games_scoring_app.Data.Players
import com.example.games_scoring_app.Data.Scores
import com.example.games_scoring_app.Theme.LeagueGothic
import com.example.games_scoring_app.Theme.black
import com.example.games_scoring_app.Theme.blue
import com.example.games_scoring_app.Theme.white
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.games_scoring_app.Theme.gray
import com.example.games_scoring_app.Theme.green
import com.example.games_scoring_app.Theme.red
import com.example.games_scoring_app.Theme.yellow
import java.util.Collections

@Composable
fun rememberPlayerListStates(numberOfPlayers: Int, valoresGenerala: List<List<String>>): List<SnapshotStateList<Int>> {
    val numLists = valoresGenerala.size

    return remember {
        List(numberOfPlayers) {
            SnapshotStateList<Int>().apply { addAll(List(numLists) { 0 }) } // Initialize each list's state to 1 (the second element)
        }
    }
}

fun calculateTotals(playerListStates: List<SnapshotStateList<Int>>, valoresGenerala: List<List<String>>): List<Int> {
    return playerListStates.map { playerSelections ->
        playerSelections.foldIndexed(0) { index, acc, selectionIndex ->
            val scoreValueStr = valoresGenerala[index].getOrElse(selectionIndex) { "0" }
            val scoreValue = when (scoreValueStr) {
                "-", "x" -> 0 // Treat "-" and "x" as 0
                else -> scoreValueStr.toIntOrNull() ?: 0 // Convert other strings to Int, or 0 if not a number
            }
            acc + scoreValue
        }
    }
}
@Composable
fun GeneralaScoreboard(players: Array<String>) {
    val TAG = "Generala"

    val opcionesGenerala = listOf("1", "2", "3", "4", "5", "6", "Escalera", "Full", "Poker", "Generala", "Generala x2")

    val valoresGenerala: List<List<String>> = listOf(
        listOf("-", "1", "2", "3", "4", "5","x"),
        listOf("-", "2", "4", "6", "8", "12", "x"),
        listOf("-", "3", "6", "9", "12", "15", "x"),
        listOf("-", "4", "8", "12", "16", "20", "x"),
        listOf("-", "5", "10", "15", "20", "25", "x"),
        listOf("-", "6", "12", "18", "24", "30", "x"),
        listOf( "-", "20", "25", "x"),
        listOf("-", "30", "35", "x"),
        listOf("-", "40", "45", "x"),
        listOf("-", "50", "55", "x"),
        listOf("-", "100", "105", "x")
    )
   // Log.d(TAG, "valoresGenerala: $valoresGenerala")


    var scoreChanges by remember { mutableStateOf(0) }
    val playerListStates = rememberPlayerListStates(players.size, valoresGenerala)

    val playerTotals = remember(playerListStates, scoreChanges) {
        calculateTotals(playerListStates, valoresGenerala)
    }

    fun cycleToNextOption(playerIndex: Int, listIndex: Int) {
        Log.d(TAG, "cycleToNextOption called")
        val current = playerListStates[playerIndex][listIndex]
        Log.d(TAG, "current: $current")
        val numOptions = valoresGenerala[listIndex].size
        Log.d(TAG, "numOptions: $numOptions")
        val next = (current % numOptions) + 1
        Log.d(TAG, "next: $next")
        playerListStates[playerIndex][listIndex] = next
        scoreChanges++
        Log.d(TAG, "scoreChanges: $scoreChanges")
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(black)
            .padding(0.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(90.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .width(90.dp)
                        .height(45.dp)
                        .background(white, shape = RoundedCornerShape(7.5.dp))
                        .padding(2.5.dp) ,
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        text = "PLAYERS",
                        fontFamily = LeagueGothic,
                        fontSize = 24.sp,
                        color = black,
                        modifier = Modifier.padding(end = 4.dp),
                        textAlign = TextAlign.Right,
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                for (valor in opcionesGenerala) {
                    var fontSize = 32.sp
                    if (valor == "Escalera" || valor == "Full" || valor == "Poker" || valor == "Generala" || valor == "Generala x2") {
                        fontSize = 24.sp
                    }
                    Box(
                        modifier = Modifier
                            .width(90.dp)
                            .height(45.dp)
                            .background(blue, shape = RoundedCornerShape(7.5.dp))
                            .padding(2.5.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(
                            text = valor,
                            fontFamily = LeagueGothic,
                            fontSize = fontSize,
                            color = black,
                            modifier = Modifier.padding(end = 4.dp),
                            textAlign = TextAlign.Right,
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                }
                Box(
                    modifier = Modifier
                        .width(90.dp)
                        .height(45.dp)
                        .background(white, shape = RoundedCornerShape(7.5.dp))
                        .padding(2.5.dp) ,
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        text = "TOTAL",
                        fontFamily = LeagueGothic,
                        fontSize = 24.sp,
                        color = black,
                        modifier = Modifier.padding(end = 4.dp),
                        textAlign = TextAlign.Right,
                    )
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(272.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {

                    for (i in 0..players.size - 1) {
                        val minwidth = 30.dp
                        val maxwidth = (272 / players.size - 4.5 * players.size).dp
                        val width = if (maxwidth < minwidth) minwidth else maxwidth

                        val playerName = if(players.size > 2) players[i].substring(0,2) else players[i]
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(width),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(width)
                                    .height(45.dp)
                                    .background(blue, shape = RoundedCornerShape(7.5.dp))
                                    .padding(2.5.dp) ,
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
                            for (j in valoresGenerala.indices) {
                                val currentOptionIndex = playerListStates[i][j]
                                val currentValue = valoresGenerala[j].getOrElse(currentOptionIndex) { "-" }
                                val boxBackgroundColor = when {
                                    currentValue == "x" -> black // Your desired black color
                                    currentValue == "-" -> gray // Your desired gray color
                                    j >= 6 && currentOptionIndex == 2 -> yellow // Define 'yellow' in your Theme or use Color.Yellow
                                    else -> white // Your default white color
                                }

                                // Determine text color for readability, especially if background is black
                                val textColor = if (currentValue == "x") white else black
                                 Box(
                                    modifier = Modifier
                                        .width(width)
                                        .height(45.dp)
                                        .background(boxBackgroundColor, shape = RoundedCornerShape(7.5.dp))
                                        .padding(0.dp)
                                        .clickable { cycleToNextOption(i, j) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = valoresGenerala[j].getOrElse(currentOptionIndex, { "-" }),
                                        fontFamily = LeagueGothic,
                                        fontSize = 24.sp,
                                        color = textColor,
                                        textAlign = TextAlign.Right,
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                            }
                            Box(
                                modifier = Modifier
                                    .width(width)
                                    .height(45.dp)
                                    .background(white, shape = RoundedCornerShape(7.5.dp))
                                    .padding(2.5.dp) ,
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = playerTotals[i].toString(),
                                    fontFamily = LeagueGothic,
                                    fontSize = 24.sp,
                                    color = black,
                                    textAlign = TextAlign.Right,
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(4.5.dp))
                    }
                }
            }
        }

    }
    Spacer(modifier = Modifier.height(40.dp))

}