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
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.games_scoring_app.Data.Players
import com.example.games_scoring_app.Data.Scores
import com.example.games_scoring_app.Theme.LeagueGothic
import com.example.games_scoring_app.Theme.black
import com.example.games_scoring_app.Theme.blue
import com.example.games_scoring_app.Theme.green
import com.example.games_scoring_app.Theme.white
import com.example.games_scoring_app.Theme.yellow

@Composable
fun TrucoScoreboard(players: Array<String>, maxScore: Int) {
    val TAG = "Truco"

    val player1 = players[0]
    val player2 = players[1]
    if( player1 == null || player2 == null){
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
        // Initialize scores as mutable state
        var score1 by remember { mutableStateOf(0) }
        var score2 by remember { mutableStateOf(0) }
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
                    .padding(vertical = 0.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                PlayerTrucoColumn(
                    player1,
                    score1,
                    maxScore,
                    onScoreClick = { scoreValue ->
                        Log.d(TAG, "scoreValue: ${scoreValue}")
                        Log.d(TAG, "oldScore: ${score1}")
                        if (score1 <= scoreValue) {
                            score1 = score1 + 1
                        } else if (score1 > 0) {
                            score1 = score1 - 1
                        }
                        Log.d(TAG, "newScore: ${score1}")
                    }
                )
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(40.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .width(10.dp)
                            .height(620.dp)
                            .background(white, shape = RoundedCornerShape(5.dp))
                            .border(2.dp, white, shape = RoundedCornerShape(5.dp))
                    )
                }
                PlayerTrucoColumn(
                    player2,
                    score2,
                    maxScore,
                    onScoreClick = { scoreValue ->
                        Log.d(TAG, "scoreValue: ${scoreValue}")
                        Log.d(TAG, "oldScore: ${score2}")
                        if (score2 <= scoreValue) {
                            score2 = score2 + 1
                        } else if (score2 > 0) {
                            score2 = score2 - 1
                        }
                        Log.d(TAG, "newScore: ${score2}")
                    }
                )
            }

        }
    }
}

@Composable
private fun PlayerTrucoColumn(
    playerName: String,
    score: Int,
    maxScore: Int,
    onScoreClick: (Int) -> Unit,
){
    val TAG = "Truco"
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(150.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {
        Box(
            modifier = Modifier
                .width(125.dp)
                .height(45.dp)
                .background(
                    if(score >= maxScore) yellow else if(score >= maxScore/2) green else white,
                    shape = RoundedCornerShape(7.5.dp)
                )
                .padding(2.5.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = playerName,
                fontFamily = LeagueGothic,
                fontSize = 32.sp,
                color = black,
                textAlign = TextAlign.Right,
            )
        }
        /*Text(
            text = playerName,
            fontFamily = LeagueGothic,
            fontSize = 48.sp,
            color = if(score >= maxScore) yellow else if(score >= maxScore/2) green else white,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
        )*/
        Spacer(modifier = Modifier.height(16.dp))
        for (i in 1..maxScore){
            val backgroundColor = if (i <= score) if (i > maxScore/2) green else blue else white
            Box(
                modifier = Modifier
                    .width(125.dp)
                    .height(9.dp)
                    .padding(0.dp)
                    .background(backgroundColor, shape = RoundedCornerShape(2.5.dp))
                    .border(2.dp, backgroundColor, shape = RoundedCornerShape(5.dp))
                    .clickable { // Add the clickable modifier here
                        onScoreClick(i)
                    },
            )
            if(i%5 == 0){
                Spacer(modifier = Modifier.height(16.dp))
            } else {
                Spacer(modifier = Modifier.height(8.dp))
            }

        }
    }
}
