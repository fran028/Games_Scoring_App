package com.example.games_scoring_app.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.games_scoring_app.Theme.LeagueGothic
import com.example.games_scoring_app.Theme.RobotoCondensed

@Composable
fun ScoreBoardBox(
    onClick: () -> Unit,
    title: String,
    description: String,
    bgcolor: Color,
    textcolor: Color,
    accentColor: Color,
    width: Dp = 0.dp,
    icon: Int,
    gameType: String = "",
    timesPlayed: Int,
    daysSinceLastPlayed: String
) {
    var iconSize = 32
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min) // Ensures all children can fill the height of the tallest
            .clickable { onClick() },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // --- Element 1: Title and Description Box ---
        // Use weight to make it take up the available space
        Box(
            modifier = Modifier
                .weight(1f) // This makes the box expand
                .fillMaxHeight() // Makes it match the height of the row
                .background(bgcolor, shape = RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .padding(horizontal = 16.dp, vertical = 16.dp),
            contentAlignment = Alignment.CenterStart // Aligns the Column inside
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                // Row to hold the Title and the Icon Box
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title,
                        style = TextStyle(
                            fontFamily = LeagueGothic,
                            color = textcolor,
                            fontSize = 40.sp,
                        ),
                        textAlign = TextAlign.Start
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    // Little colored box for the icon
                    Box(
                        modifier = Modifier
                            .size((iconSize).dp) // Smaller box for the icon
                            .background(accentColor, shape = RoundedCornerShape(4.dp))
                            .clip(RoundedCornerShape(4.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = icon),
                            contentDescription = "Title Icon",
                            modifier = Modifier.size( if(gameType == "Generico") (iconSize*0.25f).dp else (iconSize*0.75f).dp) // Smaller icon
                        )
                    }
                }

                if (description.isNotBlank()) {
                    Text(
                        text = description,
                        style = TextStyle(
                            fontFamily = RobotoCondensed,
                            color = textcolor,
                            fontSize = 16.sp,
                        ),
                        textAlign = TextAlign.Start
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // --- Element 2: Stats Box ---
        Box(
            modifier = Modifier
                .fillMaxHeight() // Makes it match the height of the row
                .background(accentColor, shape = RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .padding(horizontal = 12.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center // Center the stats column vertically and horizontally
        ) {
            // Column to stack stats vertically
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // "Partidas" Stat
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Partidas",
                        style = TextStyle(
                            fontFamily = RobotoCondensed,
                            color = bgcolor,
                            fontSize = 16.sp
                        )
                    )
                    Text(
                        text = "$timesPlayed",
                        style = TextStyle(
                            fontFamily = RobotoCondensed,
                            color = bgcolor,
                            fontSize = 16.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(4.dp)) // Vertical spacer

                // "Última vez" Stat
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Última vez",
                        style = TextStyle(
                            fontFamily = RobotoCondensed,
                            color = bgcolor,
                            fontSize = 16.sp
                        )
                    )
                    Text(
                        text = daysSinceLastPlayed,
                        style = TextStyle(
                            fontFamily = RobotoCondensed,
                            color = bgcolor,
                            fontSize = 16.sp
                        )
                    )
                }
            }
        }
    }
}
