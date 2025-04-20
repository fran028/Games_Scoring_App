package com.example.games_scoring_app.Data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface GameTypesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGameType(gameType: GameTypes)

    @Update
    fun updateGameType(gameType: GameTypes)

    @Delete
    fun deleteGameType(gameType: GameTypes)

    @Query("SELECT * FROM gameTypes")
    fun getAllGameTypes(): List<GameTypes>

    @Query("SELECT * FROM gameTypes WHERE id = :id")
    suspend fun getGameTypeById(id: Int): GameTypes?

    @Query("SELECT * FROM gameTypes WHERE name = :name")
    fun getGameTypeByName(name: String): GameTypes?

    companion object {
        fun insertGameType(item: GameTypes) {

        }
    }


}