package com.marvelapi.services.response

import com.google.gson.annotations.SerializedName

data class WrapperResponse(
    @SerializedName("code") var code: Int? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("data") var dataContainer: DataContainerResponse,
    @SerializedName("etag") var etag: String? = null,
    @SerializedName("copyright") var copyright: String? = null,
    @SerializedName("attributionText") var attributionText: String? = null,
    @SerializedName("attributionHTML") var attributionHTML: String? = null,
)