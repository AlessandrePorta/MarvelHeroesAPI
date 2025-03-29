package com.marvelapi.services.response

import com.google.gson.annotations.SerializedName
import com.marvelapi.database.CharacterEntity
import com.marvelapi.model.CharacterVO

data class CharactersResponse(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("thumbnail") var thumbnail: ThumbnailResponse? = null,
    @SerializedName("resourceURI") var resourceURI: String? = null,
)

fun CharactersResponse.toModel() = CharacterVO(
    id = id,
    name = name,
    description = description,
    resourceURI = resourceURI,
    thumbnail = thumbnail?.path + thumbnail?.extension
)

fun CharactersResponse.toEntity() = CharacterEntity(
    id = id ?: 0,
    name = name ?: "",
    description = description,
    thumbnail = thumbnail?.path + thumbnail?.extension
)



