package com.marvelapi.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "characters",
    indices = [Index(value = ["character_name"])] // Update to "character_name" to match the @ColumnInfo
)
data class CharacterEntity(
    @PrimaryKey val id: Int,

    @ColumnInfo(name = "character_name")
    val name: String,

    @ColumnInfo(name = "character_description")
    val description: String?,

    @ColumnInfo(name = "thumbnail_url")
    val thumbnail: String?
)