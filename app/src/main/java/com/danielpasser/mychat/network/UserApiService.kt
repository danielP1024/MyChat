package com.danielpasser.mychat.network

import com.danielpasser.mychat.models.networkmodels.request.UserRequest
import com.danielpasser.mychat.models.networkmodels.response.ProfileData
import com.danielpasser.mychat.models.networkmodels.response.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface UserApiService {
    @GET("users/me/")
    suspend fun getUser(): Response<UserResponse>

    @PUT("users/me/")
    suspend fun saveUser(@Body userRequest: UserRequest): Response<ProfileData>
}