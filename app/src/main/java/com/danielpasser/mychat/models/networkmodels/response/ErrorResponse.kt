package com.danielpasser.mychat.models.networkmodels.response

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("detail") val detail: ArrayList<Detail> = arrayListOf()
)

data class Detail(
    @SerializedName("loc") var loc: ArrayList<String> = arrayListOf(),
    @SerializedName("msg") var msg: String? = null,
    @SerializedName("type") var type: String? = null,
)

fun ErrorResponse.errors(): String =
    this.detail.joinToString(separator = "\n")

