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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.games_scoring_app.Components.ButtonDateBar
import com.example.games_scoring_app.Components.ButtonBar
import com.example.games_scoring_app.R
import com.example.games_scoring_app.Components.PageTitle
import com.example.games_scoring_app.Screen
import com.example.games_scoring_app.Theme.black
import com.example.games_scoring_app.Theme.white
import com.example.games_scoring_app.Theme.LeagueGothic
import com.example.games_scoring_app.Theme.yellow


@Composable
fun HomePage(navController: NavController) {
    var noMatches = true
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(black)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        PageTitle("App Name", R.drawable.ic_launcher_foreground, navController);

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
            if (!noMatches){
                /*for (match in lastMatches!!) {
                    var matchName = "Match ${match?.id}"
                    if(match?.isExample == true){
                        matchName = "Example Match"
                    }
                    ButtonDateBar(
                        text = matchName,
                        onClick = { navController.navigate(Screen.Match.createRoute(match?.id)) },
                        bgcolor = yellow,
                        height = 50.dp,
                        textcolor = black,
                        value = "${match?.date}"
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }*/
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
            ButtonBar(
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
            )
        }

    }
}