package com.marvelapi.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CharacterDao {

    @Insert
    suspend fun insertCharacters(characters: List<CharacterEntity>)

    @Query("SELECT * FROM characters WHERE name LIKE :query")
    suspend fun searchCharacters(query: String): List<CharacterEntity>

    @Query("SELECT * FROM characters")
    suspend fun getAllCharacters(): List<CharacterEntity>

    @Query("DELETE FROM characters")
    suspend fun deleteAllCharacters()

    @Update
    suspend fun updateCharacter(character: CharacterEntity)
}