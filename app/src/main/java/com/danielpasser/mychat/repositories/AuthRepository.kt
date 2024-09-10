package com.danielpasser.mychat.repositories

import com.danielpasser.mychat.models.networkmodels.request.CheckAuthCodeRequest
import com.danielpasser.mychat.models.networkmodels.request.RegisterRequest
import com.danielpasser.mychat.models.networkmodels.request.SendAuthCodeRequest
import com.danielpasser.mychat.network.AuthApiService
import com.danielpasser.mychat.utils.apiRequestFlow
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authApiService: AuthApiService,
) {
    fun sendAuthCode(sendAuthCodeRequest: SendAuthCodeRequest) = apiRequestFlow {
        authApiService.sendAuthCode(sendAuthCodeRequest = sendAuthCodeRequest)
    }

    fun checkAuthCode(checkAuthCodeRequest: CheckAuthCodeRequest) =
        apiRequestFlow { authApiService.checkAuthCode(checkAuthCodeRequest = checkAuthCodeRequest) }

    fun register(registerRequest: RegisterRequest) =
        apiRequestFlow { authApiService.register(registerRequest = registerRequest) }
}