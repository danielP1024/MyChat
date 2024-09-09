package com.danielpasser.mychat.compose

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.danielpasser.mychat.R
import com.danielpasser.mychat.utils.Utils
import com.danielpasser.mychat.utils.Utils.Companion.toBitmap
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalEncodingApi::class)
@Composable
fun ImageLoaderOld(onImagedSelected: (Uri?) -> Unit, img: String?) {
    val context = LocalContext.current


    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }


    var url by remember { mutableStateOf<Uri?>(null) }


    val pickMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {

                val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
                context.contentResolver.takePersistableUriPermission(uri, flag)
                //url = uri
                imageBitmap = uri.toBitmap(context)?.asImageBitmap()

            }
        }



    Box {
        GlideImage(
            model = imageBitmap,
            //Base64.decode(img ?: "", 0),
            contentDescription = "",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
        )
        {
            it.placeholder(R.drawable.baseline_account_circle_24)
        }

        IconButton(
            onClick = { pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(4.dp)
        ) {
            Icon(imageVector = Icons.Rounded.Edit, contentDescription = "")
        }

    }
}