package com.example.games_scoring_app.Viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.games_scoring_app.Data.Players
import com.example.games_scoring_app.Data.PlayersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlayersViewModel(private val playersRepository: PlayersRepository) : ViewModel() {
    fun addNewPlayer(player: Players) {
        viewModelScope.launch(Dispatchers.IO) {
            playersRepository.insertPlayer(player)
        }
    }

    private val _currentGamePlayers = MutableStateFlow<List<Players?>>(listOf())
    val currentGamePlayers: StateFlow<List<Players?>> = _currentGamePlayers
    fun getPlayersByGameId(gameId: Int){
        viewModelScope.launch(Dispatchers.IO) {
            val playersList = playersRepository.getPlayersByGameId(gameId)
            withContext(Dispatchers.Main) {
                _currentGamePlayers.value = playersList
            }
        }
    }
}