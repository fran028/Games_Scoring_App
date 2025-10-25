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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewGame(game: Games): Long // Make it a suspend function returning Long

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

    // Obtiene el número total de partidas para un tipo de juego
    @Query("SELECT COUNT(*) FROM games WHERE id_GameType = :gameTypeId")
    suspend fun getGamesCountByGameType(gameTypeId: Int): Int

    // Obtiene la fecha de la última partida jugada para un tipo de juego
    @Query("SELECT date FROM games WHERE id_GameType = :gameTypeId ORDER BY id DESC LIMIT 1")
    suspend fun getLastPlayedDateByGameType(gameTypeId: Int): String?
}