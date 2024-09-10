package com.danielpasser.mychat.models

import android.graphics.Bitmap
import com.danielpasser.mychat.models.networkmodels.request.AvatarRequest
import com.danielpasser.mychat.utils.Utils.Companion.toBase64str

data class Avatar(
    var fileName: String? = null,
    var bitmap: Bitmap? = null
)

fun Avatar.toAvatarRequest(): AvatarRequest =
    AvatarRequest(fileName = fileName, base64 = bitmap?.toBase64str())

