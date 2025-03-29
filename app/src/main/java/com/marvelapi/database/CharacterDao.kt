package com.marvelapi.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(character: List<CharacterEntity>)

    @Query("SELECT * FROM characterentity")
    suspend fun getAllCharacters(): List<CharacterEntity>

    @Query("SELECT * FROM characterentity WHERE isFavorite = :isFavorite ORDER BY name ASC")
    fun getFavorites(isFavorite : Boolean) : PagingSource<Int, CharacterEntity>

    @Query("SELECT * FROM characterentity WHERE id = :characterId")
    suspend fun getCharacterById(characterId: Int): CharacterEntity?

    @Query("SELECT * FROM characterentity WHERE name LIKE :query ORDER BY name ASC")
    fun pagingSource(query: String): PagingSource<Int, CharacterEntity>

    @Delete
    suspend fun delete(character: CharacterEntity)

    @Query("SELECT * FROM characterentity WHERE name LIKE :name")
    suspend fun getCharactersByName(name: String): List<CharacterEntity>
}