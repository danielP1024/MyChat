package com.danielpasser.mychat.models.networkmodels.request

data class CheckAuthCodeRequest(
    val phone: String,
    val code: String
)
