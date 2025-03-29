package com.marvelapi.database

import android.content.Context
import androidx.room.Room

class DatabaseProvider(private val context: Context) {

    @Volatile
    private var _database: CharacterDatabase? = null

    fun getDatabase(): CharacterDatabase {
        return _database ?: synchronized(this) {
            _database ?: Room.databaseBuilder(
                context.applicationContext,
                CharacterDatabase::class.java,
                "character_database"
            ).build().also {
                _database = it
            }
        }
    }
}