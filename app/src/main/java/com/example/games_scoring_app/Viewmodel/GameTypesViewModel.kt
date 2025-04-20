package com.example.games_scoring_app.Viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.games_scoring_app.Data.GameTypes
import com.example.games_scoring_app.Data.GameTypesRepository
import com.example.games_scoring_app.Data.Games
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameTypesViewModel(private val gameTypesRepository: GameTypesRepository) : ViewModel() {
    fun insertGameType(gameType: GameTypes) {
        gameTypesRepository.insertGameType(gameType)
    }

    private val _allGameTypes = MutableStateFlow<List<GameTypes?>>(listOf())
    val allGameTypes: StateFlow<List<GameTypes?>> = _allGameTypes

    fun getAllGameTypes(){
        viewModelScope.launch(Dispatchers.IO) {
            val gamesTypeList = gameTypesRepository.getAllGameTypes()
            withContext(Dispatchers.Main) {
                _allGameTypes.value = gamesTypeList
            }
        }
    }

    suspend fun getGameTypeById(id: Int): GameTypes? {
        return gameTypesRepository.getGameTypeById(id)
    }

    fun emptyGameType(): GameTypes {
        return GameTypes(
            id = 0,
            name = "Empty Game Type",
            maxPlayers = 8,
            minPlayers = 0,
            maxScore = 0
        )
    }

    fun getGameTypeNameById(id: Int): String? {
        val gameType = allGameTypes.value.find { it?.id == id }
        return gameType?.name
    }
}