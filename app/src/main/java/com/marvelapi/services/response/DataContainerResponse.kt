package com.marvelapi.services.response

import com.google.gson.annotations.SerializedName

data class DataContainerResponse(
    @SerializedName("offset") var offset: Int = 0,
    @SerializedName("limit") var limit: Int? = null,
    @SerializedName("total") var total: Int = 0,
    @SerializedName("count") var count: Int? = null,
    @SerializedName("results") var results: List<CharactersResponse>
)