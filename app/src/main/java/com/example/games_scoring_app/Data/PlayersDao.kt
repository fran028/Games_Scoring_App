package com.example.games_scoring_app.Data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface PlayersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlayer(player: Players)

    @Update
    fun updatePlayer(player: Players)

    @Delete
    fun deletePlayer(player: Players)

    @Query("SELECT * FROM players")
    fun getAllPlayers(): List<Players>

    @Query("SELECT * FROM players WHERE id = :id")
    fun getPlayerById(id: Int): Players?

    @Query("SELECT * FROM players WHERE name = :name")
    fun getPlayerByName(name: String): Players?

    @Query("SELECT * FROM players WHERE id_Game = :gameId")
    suspend fun getPlayersByGameId(gameId: Int): List<Players>

}