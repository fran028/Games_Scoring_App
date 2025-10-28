package com.example.games_scoring_app.Components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.example.games_scoring_app.Theme.LeagueGothic
import com.example.games_scoring_app.Theme.black
import com.example.games_scoring_app.Theme.blue
import com.example.games_scoring_app.Theme.darkgray
import com.example.games_scoring_app.Theme.white

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PlayerAmountGrid(
    maxPlayers: Int,
    minPlayers: Int,
    onPlayerAmountSelected: (Int) -> Unit,
    bgcolor: Color = white,
    textcolor: Color = black,
    selectedbgcolor: Color = blue,
) {
    var selectedAmount by remember { mutableStateOf(0) }
    selectedAmount = minPlayers
    val configuration = LocalConfiguration.current // Get screen configuration
    val screenWidth = configuration.screenWidthDp.dp // Get screen width in dp
    val buttonWidth = remember(maxPlayers) {
        (screenWidth - 32.dp - (maxPlayers) * 8.dp) / maxPlayers
    } .coerceAtLeast(40.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(darkgray, shape = RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            FlowRow(
                modifier = Modifier.padding(horizontal = 8.dp), // Padding for inside the box
                maxItemsInEachRow = maxPlayers,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                for (i in 1..maxPlayers) {
                    Button(
                        onClick = {
                            if (i in minPlayers..maxPlayers) {
                                onPlayerAmountSelected(i)
                                selectedAmount = i
                            }
                        },
                        modifier = Modifier
                            .width(buttonWidth)
                            .height(64.dp),
                        shape = RoundedCornerShape(5.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (i <= selectedAmount) selectedbgcolor else bgcolor,
                            contentColor = textcolor
                        ),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = i.toString(),
                                style = TextStyle(
                                    fontFamily = LeagueGothic,
                                    color = textcolor,
                                    fontSize = 48.sp,
                                    textAlign = TextAlign.Center,
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                }
            }
        }
    }
}
