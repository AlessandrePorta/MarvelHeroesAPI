package com.marvelapi.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(character: List<CharacterEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(character: CharacterEntity)

    @Query("SELECT * FROM characters ORDER BY name ASC")
    suspend fun getAllCharacters(): List<CharacterEntity>

    @Query("SELECT * FROM characters WHERE isFavorite = :isFavorite ORDER BY name ASC")
    fun getFavorites(isFavorite: Boolean): PagingSource<Int, CharacterEntity>

    @Query("SELECT * FROM characters WHERE isFavorite = 1")
    fun getAllFavoriteCharacters(): Flow<List<CharacterEntity>>

    @Query("SELECT * FROM characters WHERE id = :characterId")
    suspend fun getCharacterById(characterId: Int): CharacterEntity?

    @Query("SELECT * FROM characters WHERE name LIKE :query ORDER BY name ASC")
    fun pagingSource(query: String): PagingSource<Int, CharacterEntity>

    @Delete
    suspend fun delete(character: CharacterEntity)

    @Query("SELECT * FROM characters WHERE name LIKE :name")
    suspend fun getCharactersByName(name: String): List<CharacterEntity>
}