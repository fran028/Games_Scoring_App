package com.example.games_scoring_app.Data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ScoresDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertScore(score: Scores)

    @Update
    fun updateScore(score: Scores)

    @Delete
    fun deleteScore(score: Scores)

    @Query("SELECT * FROM scores")
    fun getAllScores(): List<Scores>

    @Query("SELECT * FROM scores WHERE id = :id")
    fun getScoreById(id: Int): Scores?

    @Query("SELECT * FROM scores WHERE id_player = :id_player")
    fun getScoresByPlayerId(id_player: Int): List<Scores>

}