package com.example.games_scoring_app.Data

class GameTypesRepository(private val gameTypesDao: GameTypesDao) {
    fun insertGameType(gameType: GameTypes) {
        gameTypesDao.insertGameType(gameType)
    }

    fun updateGameType(gameType: GameTypes) {
        gameTypesDao.updateGameType(gameType)
    }

    fun deleteGameType(gameType: GameTypes) {
        gameTypesDao.deleteGameType(gameType)
    }

    fun getAllGameTypes(): List<GameTypes> {
        return gameTypesDao.getAllGameTypes()
    }

    suspend fun getGameTypeById(id: Int): GameTypes? {
        return gameTypesDao.getGameTypeById(id)
    }

    fun getGameTypeByName(name: String): GameTypes? {
        return gameTypesDao.getGameTypeByName(name)
    }

}