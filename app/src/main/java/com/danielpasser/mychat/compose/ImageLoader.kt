package com.danielpasser.mychat.compose

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.danielpasser.mychat.R
import com.danielpasser.mychat.models.Avatar

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImageLoader(onImagedSelected: (Uri?) -> Unit, avatar: Avatar?, url: String) {
    val imageBitmap = avatar?.bitmap?.asImageBitmap()
    val context = LocalContext.current
    val pickMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {
                val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
                context.contentResolver.takePersistableUriPermission(uri, flag)
                onImagedSelected(uri)
            }
        }
    Box {
        val modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
        if (imageBitmap != null) {
            Image(
                modifier = modifier,
                contentScale = ContentScale.FillBounds,
                bitmap = imageBitmap,
                contentDescription = ""
            )
        } else GlideImage(
            model = url,
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = modifier
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