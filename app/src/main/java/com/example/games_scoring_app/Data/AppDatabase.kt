package com.example.games_scoring_app.Data

import android.content.Context
import android.util.Log
import androidx.compose.ui.geometry.isEmpty
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@Database(
    entities = [Players::class, Games::class, Scores::class, GameTypes::class, Settings::class, ScoreTypes::class],
    version = 4,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun playersDao(): PlayersDao
    abstract fun gamesDao(): GamesDao
    abstract fun scoresDao(): ScoresDao
    abstract fun gameTypesDao(): GameTypesDao
    abstract fun settingsDao(): SettingsDao
    abstract fun scoreTypesDao(): ScoreTypesDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        private val _isDatabaseReady = MutableStateFlow(false)
        val isDatabaseReady: StateFlow<Boolean> = _isDatabaseReady.asStateFlow()

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            Log.d("AppDatabase", "getDatabase called")
            val appContext = context.applicationContext
            if(INSTANCE == null){
                Log.d("AppDatabase", "Creating new database instance")
                val instance = Room.databaseBuilder(
                    appContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(AppDatabaseCallback(scope, appContext))
                    .build()
                INSTANCE = instance
            }
            Log.d("AppDatabase", "Returning existing database instance")
            signalDatabaseOperational()
            return INSTANCE!!
        }
        private fun signalDatabaseOperational() {
            if (!_isDatabaseReady.value) { // Only set to true if it's currently false
                _isDatabaseReady.value = true
                Log.d("AppDatabase", "Database signaled as operational (created/opened and populated if new).")
            }
        }

        private fun stopSignalDatabaseOperational() {
            if (_isDatabaseReady.value) { // Only set to false if it's currently true
                _isDatabaseReady.value = false
                Log.d("AppDatabase", "Database signaled as operational (created/opened and populated if new).")
            }
        }
    }
    private class AppDatabaseCallback(private val scope: CoroutineScope, private val appContext: Context) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            Log.d("AppDatabase", "onCreate called")
            super.onCreate(db)

        }

        override fun onOpen(db: SupportSQLiteDatabase) {
            Log.d("AppDatabase", "onOpen called")
            super.onOpen(db)
            scope.launch(Dispatchers.IO) {
                val database = INSTANCE ?: AppDatabase.getDatabase(appContext, scope)
                stopSignalDatabaseOperational()
                Log.d("AppDatabase", "Pre-populating database...")
                // --- CHANGE 1: Pass the new DAO here ---
                populateDatabase(database.gameTypesDao(), database.settingsDao(), database.scoreTypesDao())
                Log.d("AppDatabase", "Database pre-populated.")
                signalDatabaseOperational()
            }

        }


        // --- CHANGE 2: Add the ScoreTypesDao parameter ---
        suspend fun populateDatabase(gameTypesDao: GameTypesDao, settingsDao: SettingsDao, scoreTypesDao: ScoreTypesDao) {

            // Only clear and populate if the tables are empty to avoid wiping data on every open
            if (gameTypesDao.getAllGameTypesAsList().isEmpty()) {
                Log.d("AppDatabase", "GameTypes table is empty, populating...")

                val settings = settingsDao.getSettings()

                if (settings.isEmpty()) {
                    settingsDao.insertSettings(Settings(name = "theme", value = 0))
                } else {
                    var themeModeLoaded = false
                    for (setting in settings) {
                        if (setting.name == "theme") {
                            themeModeLoaded = true
                            break
                        }
                    }

                    if (!themeModeLoaded) {
                        settingsDao.insertSettings(Settings(name = "theme", value = 0))
                    }
                }



                // Add your initial data here using your DAO
                Log.d("AppDatabase", "Adding initial data to database...")
                // Note: The insert function needs to return the ID to link the score types
                val trucoId = gameTypesDao.insertGameType(GameTypes(name = "Truco", maxPlayers = 2, minPlayers = 2, maxScore = 30, type = "Cartas"))
                val generalaId = gameTypesDao.insertGameType(GameTypes(name = "Generala", maxPlayers = 8, minPlayers = 1, maxScore = 370, type = "Dados"))
                val pointsId = gameTypesDao.insertGameType(GameTypes(name = "Points", maxPlayers = 8, minPlayers = 2, maxScore = 1000, type = "Generico"))
                val rankingId = gameTypesDao.insertGameType(GameTypes(name = "Ranking", maxPlayers = 8, minPlayers = 3, maxScore = 0, type = "Generico"))
                val levelsId = gameTypesDao.insertGameType(GameTypes(name = "Levels", maxPlayers = 8, minPlayers = 1, maxScore = 0, type = "Generico"))

                // --- CHANGE 3: Populate the ScoreTypes table ---
                Log.d("AppDatabase", "Populating ScoreTypes...")
                scoreTypesDao.insertScoreType(ScoreTypes(id_game_type = trucoId.toInt(), name = "Final Score"))

                // Generala Score Types
                scoreTypesDao.insertScoreType(ScoreTypes(id_game_type = generalaId.toInt(), name = "Aces"))
                scoreTypesDao.insertScoreType(ScoreTypes(id_game_type = generalaId.toInt(), name = "Twos"))
                scoreTypesDao.insertScoreType(ScoreTypes(id_game_type = generalaId.toInt(), name = "Threes"))
                scoreTypesDao.insertScoreType(ScoreTypes(id_game_type = generalaId.toInt(), name = "Fours"))
                scoreTypesDao.insertScoreType(ScoreTypes(id_game_type = generalaId.toInt(), name = "Fives"))
                scoreTypesDao.insertScoreType(ScoreTypes(id_game_type = generalaId.toInt(), name = "Sixes"))
                scoreTypesDao.insertScoreType(ScoreTypes(id_game_type = generalaId.toInt(), name = "Straight"))
                scoreTypesDao.insertScoreType(ScoreTypes(id_game_type = generalaId.toInt(), name = "Full House"))
                scoreTypesDao.insertScoreType(ScoreTypes(id_game_type = generalaId.toInt(), name = "Poker"))
                scoreTypesDao.insertScoreType(ScoreTypes(id_game_type = generalaId.toInt(), name = "Generala"))

                // Generic Score Types
                scoreTypesDao.insertScoreType(ScoreTypes(id_game_type = pointsId.toInt(), name = "Final Score"))
                scoreTypesDao.insertScoreType(ScoreTypes(id_game_type = rankingId.toInt(), name = "Final Score"))
                scoreTypesDao.insertScoreType(ScoreTypes(id_game_type = levelsId.toInt(), name = "Final Score"))

                Log.d("AppDatabase", "ScoreTypes populated.")
            } else {
                Log.d("AppDatabase", "Database already populated.")
            }
        }
    }
}
