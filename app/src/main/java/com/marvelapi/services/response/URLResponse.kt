package com.marvelapi.services.response

import com.google.gson.annotations.SerializedName

data class URLResponse(
    @SerializedName("type") var type: String? = null,
    @SerializedName("url") var url: String? = null
)