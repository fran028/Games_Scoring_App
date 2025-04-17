package com.example.games_scoring_app.Data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface GamesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGame(game: Games)
    @Update
    fun updateGame(game: Games)
    @Delete
    fun deleteGame(game: Games)

    @Query("SELECT * FROM games")
    fun getAllGames(): List<Games>



}