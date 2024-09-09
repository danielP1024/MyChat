package com.danielpasser.mychat.models.networkmodels.request

import com.google.gson.annotations.SerializedName

data class AvatarRequest(
    @SerializedName("filename") var fileName: String? = null,
    @SerializedName("base_64") var base64: String? = null
)