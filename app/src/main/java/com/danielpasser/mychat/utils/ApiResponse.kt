package com.danielpasser.mychat.utils

import com.danielpasser.mychat.models.networkmodels.response.ErrorResponse

sealed class ApiResponse<out T> {
    object Loading: ApiResponse<Nothing>()

    data class Success<out T>(
        val data: T
    ): ApiResponse<T>()

    data class Failure(
        val error: ErrorResponse,
        val code: Int,
    ): ApiResponse<Nothing>()
}