package com.danielpasser.mychat.models.networkmodels.response

import com.google.gson.annotations.SerializedName

data class SendAuthCodeResponse(@SerializedName("is_success") val isSuccess: Boolean)