package com.example.games_scoring_app.Data

class PlayersRepository(private val playersDao: PlayersDao) {
    fun insertPlayer(player: Players) {
        playersDao.insertPlayer(player)
    }

    fun updatePlayer(player: Players) {
        playersDao.updatePlayer(player)
    }

    fun deletePlayer(player: Players) {
        playersDao.deletePlayer(player)
    }

    fun getAllPlayers(): List<Players> {
        return playersDao.getAllPlayers()
    }

    fun getPlayerById(id: Int): Players? {
        return playersDao.getPlayerById(id)
    }

    fun getPlayerByName(name: String): Players? {
        return playersDao.getPlayerByName(name)
    }

    suspend fun getPlayersByGameId(gameId: Int): List<Players> {
        return playersDao.getPlayersByGameId(gameId)
    }

}