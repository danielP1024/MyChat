package com.danielpasser.mychat.utils

import android.util.Log
import com.danielpasser.mychat.models.networkmodels.response.Detail
import com.danielpasser.mychat.models.networkmodels.response.ErrorResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withTimeoutOrNull
import retrofit2.Response

fun <T> apiRequestFlow(call: suspend () -> Response<T>): Flow<ApiResponse<T>> = flow {
    emit(ApiResponse.Loading)
    try {
        withTimeoutOrNull(20000L) {
            val response = call()
            try {
                if (response.isSuccessful) {
                    response.body()?.let { data ->
                        emit(ApiResponse.Success(data))
                    }
                } else {
                    response.errorBody()?.let { error ->

                        error.close()
                        val text = error.charStream().readText()
                        try {
                            val parsedError: ErrorResponse =
                                Gson().fromJson(text, ErrorResponse::class.java)
                            Log.e("TEST", parsedError.toString())
                            emit(ApiResponse.Failure(error = parsedError, code = response.code()))
                        } catch (e: Exception) {
                            Log.e("TEST", e.toString())
                            emit(
                                ApiResponse.Failure(
                                    ErrorResponse(
                                        detail = arrayListOf(
                                            Detail(
                                                msg = text
                                            )
                                        )
                                    ), response.code()
                                )
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("TEST", e.toString())
                emit(
                    ApiResponse.Failure(
                        ErrorResponse(
                            detail = arrayListOf(
                                Detail(
                                    msg = e.message ?: e.toString()
                                )
                            )
                        ), 400
                    )
                )

            }
        }
    } catch (e: Exception) {
        Log.e("TEST", e.toString())
    }

        ?: emit(
            ApiResponse.Failure(
                ErrorResponse(arrayListOf(Detail(msg = "Timeout! Please try again."))), 408
            )
        )
}.flowOn(Dispatchers.IO)