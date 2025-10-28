package com.example.games_scoring_app.Components

import com.example.games_scoring_app.Data.Players
import com.example.games_scoring_app.Theme.gold
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
import com.example.games_scoring_app.R
import com.example.games_scoring_app.Theme.LeagueGothic
import com.example.games_scoring_app.Theme.RobotoCondensed
import com.example.games_scoring_app.Theme.red

@Composable
fun GameBox(
    onClick: () -> Unit,
    onDelete: () -> Unit,
    title: String,
    bgcolor: Color,
    textcolor: Color,
    accentColor: Color,
    width: Dp = 0.dp,
    icon: Int,
    gameType: String = "",
    daysSinceLastPlayed: String,
    players: List<Players>
) {


    var iconSize = 32
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min) // Ensures all children can fill the height of the tallest
            ,
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            // Removed horizontalAlignment
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .fillMaxHeight()
                .width(50.dp)
                .clickable { onDelete() }
        ) {
            Box(
                modifier = Modifier
                    .background(red, shape = RoundedCornerShape(10.dp))
                    .clip(RoundedCornerShape(10.dp))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
                    .weight(1f)
                    .fillMaxWidth(), // Make the box fill the column's width
                contentAlignment = Alignment.Center // Center the stats column vertically and horizontally
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.trash), // Assuming you have a delete icon
                        contentDescription = "Delete Game",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .weight(1f) // This makes the box expand
                .fillMaxHeight() // Makes it match the height of the row
                .background(bgcolor, shape = RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .clickable { onClick() },
            contentAlignment = Alignment.CenterStart // Aligns the Column inside
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                // Row to hold the Title and the Icon Box
                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth())
                {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
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
                                modifier = Modifier.size(if (gameType == "Generico") (iconSize * 0.25f).dp else (iconSize * 0.75f).dp) // Smaller icon
                            )
                        }

                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            text = title,
                            style = TextStyle(
                                fontFamily = LeagueGothic,
                                color = textcolor,
                                fontSize = 40.sp,
                            ),
                            textAlign = TextAlign.Start
                        )
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = "($daysSinceLastPlayed)",
                        style = TextStyle(
                            fontFamily = RobotoCondensed,
                            color = textcolor,
                            fontSize = 20.sp,
                        ),
                        textAlign = TextAlign.Start
                    )

                }
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (!players.isEmpty()) {
                        Text(
                            text = "Players:",
                            style = TextStyle(
                                fontFamily = RobotoCondensed,
                                color = textcolor,
                                fontSize = 16.sp,
                            ),
                            textAlign = TextAlign.Start
                        )
                        Spacer(modifier = Modifier.size(2.dp))
                        for (player in players) {
                            Text(
                                text = player.name,
                                style = TextStyle(
                                    fontFamily = RobotoCondensed,
                                    color = if (player.won) gold else textcolor,
                                    fontSize = 16.sp,
                                ),
                                textAlign = TextAlign.Start
                            )
                            Spacer(modifier = Modifier.size(2.dp))
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .fillMaxHeight()
                .width(50.dp).clickable { onClick() }
        ) {
            Box(
                modifier = Modifier
                    .background(accentColor, shape = RoundedCornerShape(10.dp))
                    .clip(RoundedCornerShape(10.dp))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
                    .weight(1f)
                    .fillMaxWidth(), // Make the box fill the column's width
                contentAlignment = Alignment.Center // Center the stats column vertically and horizontally
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.play), // Assuming you have a delete icon
                        contentDescription = "play Game",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}
