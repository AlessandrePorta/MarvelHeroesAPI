package com.marvelapi.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CharacterEntity::class], version = 2)
abstract class CharacterDatabase : RoomDatabase() {

    abstract fun characterDao(): CharacterDao

}