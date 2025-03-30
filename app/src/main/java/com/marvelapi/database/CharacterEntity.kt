package com.marvelapi.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.marvelapi.model.CharacterVO

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,

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
        id = this.id,
        name = this.name,
        description = this.description,
        thumbnail = this.thumbnail,
        isFavorite = this.isFavorite ?: false
    )
}

fun CharacterVO.toEntity() = CharacterEntity(
    this?.id ?: 0, this?.name, this?.description, this?.thumbnail, this?.isFavorite
)