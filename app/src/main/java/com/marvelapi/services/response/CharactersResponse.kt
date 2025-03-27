package com.marvelapi.services.response

import com.google.gson.annotations.SerializedName
import com.marvelapi.model.Character

data class CharactersResponse(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("thumbnail") var thumbnail: ThumbnailResponse? = null,
)

fun CharactersResponse.toModel() = Character(
    id = id,
    name = name,
    description = description,
    thumbnail = thumbnail,
)

fun Character.toEntity() = CharactersResponse(
    id = id,
    name = name,
    description = description,
    thumbnail = thumbnail
)



