package com.marvelapi.database

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    private var characterDatabase: CharacterDatabase? = null

    fun getInstance(context: Context): CharacterDatabase {
        return characterDatabase ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                CharacterDatabase::class.java,
                "character_database"
            ).build()
            characterDatabase = instance
            instance
        }
    }
}