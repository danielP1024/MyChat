package com.danielpasser.mychat.compose

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.danielpasser.mychat.models.networkmodels.response.errors
import com.danielpasser.mychat.utils.ApiResponse

@Composable
fun <T> ShowToastCompose(response: ApiResponse<T>?) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {

        if (response is ApiResponse.Failure) {
            Log.e("TEST",response.error.toString())
            val text = "${response.code} ${response.error.errors()}"
            Toast.makeText(
                context,
                text,
                Toast.LENGTH_SHORT,
            ).show()
        }
    }
}