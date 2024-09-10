package com.danielpasser.mychat.models.networkmodels.response

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("detail") val detail: ArrayList<Detail> = arrayListOf()
)

data class Detail(
    @SerializedName("loc") var loc: ArrayList<String> = arrayListOf(),
    @SerializedName("msg") var msg: String? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("ctx") var ctx: Ctx? = Ctx()
)

fun ErrorResponse.errors(): String =
    this.detail.map { it.msg }.joinToString(separator = "\n")

data class Ctx(

    @SerializedName("limit_value") var limitValue: Int? = null

)