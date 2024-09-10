package com.danielpasser.mychat.repositories

import android.util.Log
import com.danielpasser.mychat.models.networkmodels.request.UserRequest
import com.danielpasser.mychat.models.networkmodels.response.ProfileData
import com.danielpasser.mychat.models.networkmodels.response.toUserEntity
import com.danielpasser.mychat.models.room.toProfiled
import com.danielpasser.mychat.network.UserApiService
import com.danielpasser.mychat.room.UserDao
import com.danielpasser.mychat.utils.ApiResponse
import com.danielpasser.mychat.utils.apiRequestFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userApiService: UserApiService,
    private val userDao: UserDao
) {
    fun getUser() = apiRequestFlow {
        userApiService.getUser()
    }.map { response ->
        if (response is ApiResponse.Success) {
            Log.e("TEST", response.data.profileData.toString())
            saveUserDB(response.data.profileData)
        }
        return@map response
    }

    fun saveUser(userRequest: UserRequest) =
        apiRequestFlow { userApiService.saveUser(userRequest = userRequest) }.map { response ->
            if (response is ApiResponse.Success) {
          //   getUser().collect{ }
            }
            return@map response
        }

    private suspend fun saveUserDB(user: ProfileData?) {

        if (user != null) {
            userDao.insert(user = user.toUserEntity())
        }
    }

    fun userDB() = userDao.getUsers().map {
        if (it.isEmpty()) null
        else
            it.first().toProfiled()
    }
}