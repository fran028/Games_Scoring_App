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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.games_scoring_app.Data.Players
import com.example.games_scoring_app.Data.Scores
import com.example.games_scoring_app.Theme.LeagueGothic
import com.example.games_scoring_app.Theme.black
import com.example.games_scoring_app.Theme.blue
import com.example.games_scoring_app.Theme.gray
import com.example.games_scoring_app.Theme.green
import com.example.games_scoring_app.Theme.white
import com.example.games_scoring_app.Theme.yellow

@Composable
fun TrucoScoreboard(players: Array<String>, maxScore: Int, themeMode: Int) {
    val TAG = "Truco"

    val player1 = players[0]
    val player2 = players[1]

    val backgroundColor = if (themeMode == 0) black else white
    val fontColor = if (themeMode == 0) white else black
    val buttonColor = if (themeMode == 0) white else black
    val buttonFontColor = if (themeMode == 0) black else white

    if( player1 == null || player2 == null){
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
        // Initialize scores as mutable state
        var score1 by remember { mutableStateOf(0) }
        var score2 by remember { mutableStateOf(0) }
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
                        /*if (score1 <= scoreValue) {
                            score1 = score1 + 1
                        } else if (score1 > 0) {
                            score1 = score1 - 1
                        }*/
                        score1 = scoreValue
                        Log.d(TAG, "newScore: ${score1}")
                    },
                    buttonColor = buttonColor,
                    buttonFontColor = buttonFontColor,
                    fontColor = fontColor
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
                            .height(750.dp)
                            .background(buttonColor, shape = RoundedCornerShape(5.dp))
                    )
                }
                PlayerTrucoColumn(
                    player2,
                    score2,
                    maxScore,
                    onScoreClick = { scoreValue ->
                        Log.d(TAG, "scoreValue: ${scoreValue}")
                        Log.d(TAG, "oldScore: ${score2}")
                        /*if (score2 <= scoreValue) {
                            score2 = score2 + 1
                        } else if (score2 > 0) {
                            score2 = score2 - 1
                        }*/
                        score2 = scoreValue
                        Log.d(TAG, "newScore: ${score2}")
                    },
                    buttonColor = buttonColor,
                    buttonFontColor = buttonFontColor,
                    fontColor = fontColor
                )
            }
            Spacer(modifier = Modifier.height(20.dp))

        }
    }
}

@Composable
private fun PlayerTrucoColumn(
    playerName: String,
    score: Int,
    maxScore: Int,
    onScoreClick: (Int) -> Unit,
    buttonColor: Color,
    buttonFontColor: Color,
    fontColor: Color
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
                    if(score >= maxScore) yellow else if(score >= maxScore/2) green else buttonColor,
                    shape = RoundedCornerShape(7.5.dp)
                )
                .padding(2.5.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = playerName,
                fontFamily = LeagueGothic,
                fontSize = 32.sp,
                color = if(score >= maxScore || score >= maxScore/2) black else buttonFontColor,
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
        val lineLenght = 80.dp
        val lineThick = 9.dp
        val diagonalLineLenght = 80.dp
        val rowWidth = 100.dp

        val colorBuenas = green
        val colorMalas = buttonColor
        val colorNormal = gray

        Spacer(modifier = Modifier.height(16.dp))
        for (i in 0..maxScore/5-1){
            Box(
                modifier = Modifier
                    .width(lineLenght)
                    .height(lineThick)
                    .padding(0.dp)
                    .background(
                        if ((i*5)+1 <= score) if ((i*5)+1 > maxScore / 2) colorBuenas else colorMalas else colorNormal ,
                        shape = RoundedCornerShape(2.5.dp)
                    )
                    .clickable { // Add the clickable modifier here
                        onScoreClick(1+(5*i))
                    },
            )
            Row(
                modifier = Modifier
                    .width(rowWidth)
                    .padding(0.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .width(lineThick)
                        .height(lineLenght)
                        .padding(0.dp)
                        .background(
                            if ((i*5)+2 <= score) if ((i*5)+2 > maxScore / 2) colorBuenas else colorMalas else colorNormal ,
                            shape = RoundedCornerShape(2.5.dp)
                        )
                        .clickable { // Add the clickable modifier here
                            onScoreClick(2+5*i)
                        },
                )
                Box(
                    modifier = Modifier
                        .width(lineThick)
                        .height(diagonalLineLenght)
                        .rotate(45f)
                        .padding(0.dp)
                        .background(
                            if ((i*5)+3 <= score) if ((i*5)+3 > maxScore / 2) colorBuenas else colorMalas else colorNormal ,
                            shape = RoundedCornerShape(2.5.dp)
                        )
                        .clickable { // Add the clickable modifier here
                            onScoreClick(3+5*i)
                        },
                )
                Box(
                    modifier = Modifier
                        .width(lineThick)
                        .height(lineLenght)
                        .padding(0.dp)
                        .background(
                            if ((i*5)+4 <= score) if ((i*5)+4 > maxScore / 2) colorBuenas else colorMalas else colorNormal,
                            shape = RoundedCornerShape(2.5.dp)
                        )
                        .clickable { // Add the clickable modifier here
                            onScoreClick(4+5*i)
                        },
                )
            }
            Box(
                modifier = Modifier
                    .width(lineLenght)
                    .height(lineThick)
                    .padding(0.dp)
                    .background(
                        if ((i*5)+5 <= score) if ((i*5)+5 > maxScore / 2) colorBuenas else colorMalas else colorNormal ,
                        shape = RoundedCornerShape(2.5.dp)
                    )
                    .clickable { // Add the clickable modifier here
                        onScoreClick(5+5*i)
                    },
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
