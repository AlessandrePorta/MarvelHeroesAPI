package com.marvelapi.services.response

import com.google.gson.annotations.SerializedName

data class ThumbnailResponse(
    @SerializedName("path") var path: String? = null,
    @SerializedName("extension") var extension: String? = null
)