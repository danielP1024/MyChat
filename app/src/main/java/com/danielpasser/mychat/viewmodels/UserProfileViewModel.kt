package com.danielpasser.mychat.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danielpasser.mychat.models.networkmodels.response.ProfileData
import com.danielpasser.mychat.models.networkmodels.response.UserResponse
import com.danielpasser.mychat.repositories.UserRepository
import com.danielpasser.mychat.utils.ApiResponse
import com.danielpasser.mychat.utils.TIMEOUT_MILLIS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {


    private val _userResponse: MutableStateFlow<ApiResponse<UserResponse>?> =
        MutableStateFlow(null)
    val userResponse: StateFlow<ApiResponse<UserResponse>?> = _userResponse
    val userDB: StateFlow<ProfileData?> = userRepository.userDB().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = null
    )

    private fun getUser() {
        viewModelScope.launch {
            userRepository.getUser().collect {

                _userResponse.value = it

            }
        }
    }

    init {
        getUser()
    }


}