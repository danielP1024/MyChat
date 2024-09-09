package com.danielpasser.mychat.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Country(
    @SerializedName("name") var name: String? = null,
    @SerializedName("dial_code") var dialCode: String? = null,
    @SerializedName("code") var isoCode: String? = null
) : Parcelable
