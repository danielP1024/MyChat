package com.danielpasser.mychat.models.networkmodels.response

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("profile_data") var profileData: ProfileData? = ProfileData()
)