package com.danielpasser.mychat.models.networkmodels.request

import com.google.gson.annotations.SerializedName

data class RefreshTokenRequest(@SerializedName("refresh_token") val refreshToken: String?)