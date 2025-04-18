package com.example.games_scoring_app.Data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
        lateinit var context: Context

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            //Use the applicationContext
            val appContext = context.applicationContext
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    appContext, // Use application context
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(AppDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
    private class AppDatabaseCallback(private val scope: CoroutineScope) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    Log.d("AppDatabase", "Pre-populating database...")
                    populateDatabase(database.gameTypesDao()) // Call the population function
                    Log.d("AppDatabase", "Database pre-populated")
                }
            }
        }

        suspend fun populateDatabase(gameTypesDao: GameTypesDao) {
            // Add your initial data here using your DAO
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

        }
    }
}