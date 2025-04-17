package com.example.games_scoring_app.Data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class Games(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "id_GameType") val id_GameType: Int,
    @ColumnInfo(name = "date") val date: String,
)