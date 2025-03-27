package com.marvelapi.model

import com.marvelapi.services.response.ThumbnailResponse

data class Character(
    var id: Int? = null,
    var name: String? = null,
    var description: String? = null,
    var thumbnail: ThumbnailResponse? = null,
    var resourceURI: String? = null,
    var isFavorite: Boolean? = null
)