package com.example.games_scoring_app.Viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.games_scoring_app.Data.Games
import com.example.games_scoring_app.Data.GamesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class GamesViewModel(private val gamesRepository: GamesRepository) : ViewModel() {
    private val _lastGame = MutableStateFlow<Games?>(null)
    val lastGame: StateFlow<Games?> = _lastGame
    fun getLastGame() {
        viewModelScope.launch(Dispatchers.IO) {
            val games = gamesRepository.getLastGame() // Use the repository method
            withContext(Dispatchers.Main) {
                _lastGame.value = games
            }
        }
    }

    private val _allGames = MutableStateFlow<List<Games?>>(listOf())
    val allGames: StateFlow<List<Games?>> = _allGames

    fun getAllGames(){
        viewModelScope.launch(Dispatchers.IO) {
            val gameList = gamesRepository.getAllGames()
            withContext(Dispatchers.Main) {
                _allGames.value = gameList
            }
        }
    }

    suspend fun addNewGame(game: Games): Long {
        return withContext(Dispatchers.IO) {
            gamesRepository.addNewGame(game)
        }
    }

    val _currentGame = MutableStateFlow<Games?>(null)
    val currentGame: StateFlow<Games?> = _currentGame

    fun getGameById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val game = gamesRepository.getGameById(id)
            withContext(Dispatchers.Main) {
                _currentGame.value = game
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTodaysDate(): String {
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return today.format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun emptyGame(): Games{
        val date = this.getTodaysDate()
        val emptymatch = Games(
            id_GameType = 0,
            date = date
        )
        return emptymatch
    }
}