package com.danielpasser.mychat.viewmodels

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.danielpasser.mychat.models.Avatar
import com.danielpasser.mychat.models.networkmodels.request.UserRequest
import com.danielpasser.mychat.models.networkmodels.response.ProfileData
import com.danielpasser.mychat.models.networkmodels.response.UserResponse
import com.danielpasser.mychat.models.toAvatarRequest
import com.danielpasser.mychat.repositories.TokenRepository
import com.danielpasser.mychat.repositories.UserRepository
import com.danielpasser.mychat.utils.ApiResponse
import com.danielpasser.mychat.utils.BASE_URL_MEDIA
import com.danielpasser.mychat.utils.Utils.Companion.convertDateToMillis
import com.danielpasser.mychat.utils.Utils.Companion.convertMillisToDate
import com.danielpasser.mychat.utils.Utils.Companion.toBase64str
import com.danielpasser.mychat.utils.Utils.Companion.toBitmap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserEditViewModel @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val application: Application,
    private val userRepository: UserRepository
) : BaseTokenViewModel(tokenRepository) {


    private val _userResponse: MutableStateFlow<ApiResponse<UserResponse>?> =
        MutableStateFlow(null)
    val userResponse: StateFlow<ApiResponse<UserResponse>?> = _userResponse

    private val _saveUserResponse: MutableStateFlow<ApiResponse<ProfileData>?> =
        MutableStateFlow(null)
    val saveUserResponse: StateFlow<ApiResponse<ProfileData>?> = _saveUserResponse


    private val _message: MutableStateFlow<String> =
        MutableStateFlow("")
    val message: StateFlow<String> = _message


    private val _userName: MutableStateFlow<String> =
        MutableStateFlow("")
    private val userName: StateFlow<String> = _userName

    private val _name: MutableStateFlow<String> =
        MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _city: MutableStateFlow<String> =
        MutableStateFlow("")
    val city: StateFlow<String> = _city

    //birthday
    private val _birthday: MutableStateFlow<Long?> =
        MutableStateFlow(null)
    val birthday: StateFlow<Long?> = _birthday


    private val _vk: MutableStateFlow<String> =
        MutableStateFlow("")
    val vk: StateFlow<String> = _vk

    private val _instagram: MutableStateFlow<String> =
        MutableStateFlow("")
    val instagram: StateFlow<String> = _instagram


    private val _status: MutableStateFlow<String> =
        MutableStateFlow("")
    val status: StateFlow<String> = _status

    private val _avatar: MutableStateFlow<Avatar?> =
        MutableStateFlow(null)
    val avatar: StateFlow<Avatar?> = _avatar

    private val _avatarUrl: MutableStateFlow<String> =
        MutableStateFlow("")
    val avatarUrl: StateFlow<String> = _avatarUrl

    init {
        getUser()
    }

    fun onNameChanged(str: String) {
        _name.value = str
    }

    fun onCityChanged(str: String) {
        _city.value = str
    }

    fun onStatusChanged(str: String) {
        _status.value = str
    }

    private fun onUserNameChanged(str: String) {
        _userName.value = str
    }

    fun onBirthDayChanged(birthday: Long?) {
        _birthday.value = birthday
    }

    fun onInstagramChanged(str: String) {
        _instagram.value = str
    }

    fun onVkChanged(str: String) {
        _vk.value = str
    }

    fun onImagedSelected(uri: Uri?) {
        val bitmap = uri?.toBitmap(application)
        _avatar.value = Avatar(fileName = "fileName", bitmap = bitmap)
        val a = bitmap?.toBase64str()
        Log.e("TEST", a.toString())
    }

    private fun getUser() {
        viewModelScope.launch {
            userRepository.getUser().collect {
                _userResponse.value = it
                if (it is ApiResponse.Success) {
                    initUser(it.data)
                }
            }
        }
    }

    private fun initUser(user: UserResponse) {
        onNameChanged(user.profileData?.name ?: "")
        onCityChanged(user.profileData?.city ?: "")
        onStatusChanged(user.profileData?.status ?: "")
        onUserNameChanged(user.profileData?.name ?: "")
        onBirthDayChanged(user.profileData?.birthday?.convertDateToMillis())
        _avatarUrl.value = BASE_URL_MEDIA + user.profileData?.avatars?.avatar
        onVkChanged(user.profileData?.vk ?: "")
        onInstagramChanged(user.profileData?.instagram ?: "")
    }

    fun saveUser() {
        viewModelScope.launch {
            userRepository.saveUser(setUser()).collect {
                if (it is ApiResponse.Success) getUser()
                _saveUserResponse.value = it
            }
        }
    }

    private fun setUser(): UserRequest {
        return UserRequest(
            name = name.value,
            city = city.value,
            status = status.value,
            username = userName.value,
            birthday = birthday.value.convertMillisToDate(),
            avatar = avatar.value?.toAvatarRequest(),
            vk = vk.value,
            instagram = instagram.value
        )
    }
}