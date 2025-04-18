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

    @Query("SELECT * FROM games ORDER BY date, id DESC LIMIT 1")
    fun getLastGame(): Games?

    @Query("SELECT * FROM games WHERE id = :id")
    fun getGameById(id: Int): Games?

}