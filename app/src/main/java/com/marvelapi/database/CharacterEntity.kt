package com.marvelapi.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.marvelapi.model.CharacterVO

@Entity
data class CharacterEntity(
    @PrimaryKey var id: Int? = null,

    @ColumnInfo(name = "name")
    var name: String? = null,

    @ColumnInfo(name = "description")
    var description: String? = null,

    @ColumnInfo(name = "url")
    var thumbnail: String? = null,

    @ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean? = null
) {
    fun toModel() = CharacterVO(
        id = id,
        name = name,
        description = description,
        thumbnail = thumbnail,
        isFavorite = isFavorite ?: false
    )
}

fun CharacterVO.toEntity() = CharacterEntity(
    this?.id, this?.name, this?.description, this?.thumbnail, this?.isFavorite
)