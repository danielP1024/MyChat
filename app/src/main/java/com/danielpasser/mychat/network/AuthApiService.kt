package com.danielpasser.mychat.network

import com.danielpasser.mychat.models.networkmodels.request.CheckAuthCodeRequest
import com.danielpasser.mychat.models.networkmodels.request.RefreshTokenRequest
import com.danielpasser.mychat.models.networkmodels.request.RegisterRequest
import com.danielpasser.mychat.models.networkmodels.request.SendAuthCodeRequest
import com.danielpasser.mychat.models.networkmodels.response.AuthInfoResponse
import com.danielpasser.mychat.models.networkmodels.response.SendAuthCodeResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("users/send-auth-code/")
    suspend fun sendAuthCode(@Body sendAuthCodeRequest: SendAuthCodeRequest): Response<SendAuthCodeResponse>

    @POST("users/check-auth-code/")
    suspend fun checkAuthCode(@Body checkAuthCodeRequest: CheckAuthCodeRequest): Response<AuthInfoResponse>

    @POST("users/register/")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<AuthInfoResponse>

    @POST("users/refresh-token/")
    suspend fun refreshToken(@Body refreshTokenRequest: RefreshTokenRequest): Response<AuthInfoResponse>
}
