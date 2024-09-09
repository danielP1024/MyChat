package com.danielpasser.mychat.models.networkmodels.request

import com.google.gson.annotations.SerializedName

data class UserRequest(
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("username")
    var username: String? = null,
    @SerializedName("birthday")
    var birthday: String? = null,
    @SerializedName("city")
    var city: String? = null,
    @SerializedName("vk")
    var vk: String? = null,
    @SerializedName("instagram")
    var instagram: String? = null,
    @SerializedName("status")
    var status: String? = null,
    @SerializedName("avatar")
    var avatar: AvatarRequest? = null
)
