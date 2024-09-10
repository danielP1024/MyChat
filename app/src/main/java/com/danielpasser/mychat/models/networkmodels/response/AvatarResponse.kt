package com.danielpasser.mychat.models.networkmodels.response

import com.google.gson.annotations.SerializedName

data class AvatarResponse (
    @SerializedName("avatar"     ) var avatar     : String? = null,
    @SerializedName("bigAvatar"  ) var bigAvatar  : String? = null,
    @SerializedName("miniAvatar" ) var miniAvatar : String? = null
)