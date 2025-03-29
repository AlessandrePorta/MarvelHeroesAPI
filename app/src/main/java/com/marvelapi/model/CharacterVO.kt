package com.marvelapi.model

data class CharacterVO(
    var id: Int? = null,
    var name: String? = null,
    var description: String? = null,
    var thumbnail: String? = null,
    var resourceURI: String? = null,
    var isFavorite: Boolean = false
)