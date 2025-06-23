package com.example.games_scoring_app.Data

import android.content.Context
import android.util.Log
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
    entities = [Players::class, Games::class, Scores::class, GameTypes::class],
    version = 1,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun playersDao(): PlayersDao
    abstract fun gamesDao(): GamesDao
    abstract fun scoresDao(): ScoresDao
    abstract fun gameTypesDao(): GameTypesDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        private val _isDatabaseReady = MutableStateFlow(false)
        val isDatabaseReady: StateFlow<Boolean> = _isDatabaseReady.asStateFlow()

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            Log.d("AppDatabase", "getDatabase called")
            val appContext = context.applicationContext
            return INSTANCE ?: synchronized(this) {
                Log.d("AppDatabase", "Creating new database instance")
                val instance = Room.databaseBuilder(
                    appContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(AppDatabaseCallback(scope, appContext))
                    .build()
                //signalDatabaseOperational()
                INSTANCE = instance
                instance

            }
        }
        private fun signalDatabaseOperational() {
            if (!_isDatabaseReady.value) { // Only set to true if it's currently false
                _isDatabaseReady.value = true
                Log.d("AppDatabase", "Database signaled as operational (created/opened and populated if new).")
            }
        }
    }
    private class AppDatabaseCallback(private val scope: CoroutineScope, private val appContext: Context) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            Log.d("AppDatabase", "onCreate called")
            super.onCreate(db)
            scope.launch(Dispatchers.IO) {
                val database = INSTANCE ?: AppDatabase.getDatabase(appContext, scope)

                Log.d("AppDatabase", "Pre-populating database...")
                populateDatabase(database.gameTypesDao())
                Log.d("AppDatabase", "Database pre-populated.")
                signalDatabaseOperational() // Signal that the database is ready
            }

        }

        override fun onOpen(db: SupportSQLiteDatabase) {
            Log.d("AppDatabase", "onOpen called")
            super.onOpen(db)
            Log.d("AppDatabase", "onOpen called - Database is being opened.")
            signalDatabaseOperational()
        }


        suspend fun populateDatabase(gameTypesDao: GameTypesDao) {
            // Add your initial data here using your DAO
            Log.d("AppDatabase", "Adding initial data to database...")
            val item1 = GameTypes(
                name = "Truco",
                maxPlayers = 2,
                minPlayers = 2,
                maxScore = 30
            )
            gameTypesDao.insertGameType(item1)
            Log.d("AppDatabase", "Truco added to database")

            val item2 = GameTypes(
                name = "Generala",
                maxPlayers = 8,
                minPlayers = 1,
                maxScore = 370
            )
            gameTypesDao.insertGameType(item2)
            Log.d("AppDatabase", "Generala added to database")

            val item3 = GameTypes(
                name = "Puntos",
                maxPlayers = 8,
                minPlayers = 2,
                maxScore = 1000
            )
            gameTypesDao.insertGameType(item3)
            Log.d("AppDatabase", "Puntos added to database")

        }
    }
}