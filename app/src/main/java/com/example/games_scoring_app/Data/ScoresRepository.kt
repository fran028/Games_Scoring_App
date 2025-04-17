package com.example.games_scoring_app.Data

class ScoresRepository(private val scoresDao: ScoresDao) {

    fun insertScore(score: Scores) {
        scoresDao.insertScore(score)
    }

    fun updateScore(score: Scores) {
        scoresDao.updateScore(score)
    }

    fun deleteScore(score: Scores) {
        scoresDao.deleteScore(score)
    }

    fun getAllScores(): List<Scores> {
        return scoresDao.getAllScores()
    }

    fun getScoreById(id: Int): Scores? {
        return scoresDao.getScoreById(id)
    }

    fun getScoresByPlayerId(id_player: Int): List<Scores> {
        return scoresDao.getScoresByPlayerId(id_player)
    }

}