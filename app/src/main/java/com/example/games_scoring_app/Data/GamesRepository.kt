package com.example.games_scoring_app.Data

class GamesRepository(private val gamesDao: GamesDao) {
    fun insertGame(game: Games) {
        gamesDao.insertGame(game)
    }

    fun updateGame(game: Games) {
        gamesDao.updateGame(game)
    }

    fun deleteGame(game: Games) {
        gamesDao.deleteGame(game)
    }

    fun getAllGames(): List<Games> {
        return gamesDao.getAllGames()
    }
}