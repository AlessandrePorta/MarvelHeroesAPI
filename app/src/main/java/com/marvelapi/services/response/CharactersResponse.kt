package com.marvelapi.services.response

import com.google.gson.annotations.SerializedName

data class CharactersResponse(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("thumbnail") var thumbnail: ThumbnailResponse? = null,
)



