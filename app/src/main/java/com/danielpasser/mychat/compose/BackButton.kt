package com.danielpasser.mychat.compose

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.danielpasser.mychat.R

@Composable
fun BackButton(onBackClicked:()->Unit) {
    IconButton(onClick = onBackClicked) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(id = R.string.back)
        )
    }
}