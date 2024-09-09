package com.danielpasser.mychat.viewmodels

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.danielpasser.mychat.compose.Registration
import com.danielpasser.mychat.models.Country
import com.danielpasser.mychat.models.networkmodels.request.RegisterRequest
import com.danielpasser.mychat.models.networkmodels.response.AuthInfoResponse
import com.danielpasser.mychat.repositories.AuthRepository
import com.danielpasser.mychat.repositories.TokenRepository
import com.danielpasser.mychat.utils.ApiResponse
import com.danielpasser.mychat.utils.Utils.Companion.usernameCheck
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository, tokenRepository: TokenRepository
) : BaseTokenViewModel(tokenRepository) {

    private val _phoneNumber: MutableStateFlow<String> = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber

    private val _selectedCountry: MutableStateFlow<Country> = MutableStateFlow(Country())
    val selectedCountry: StateFlow<Country> = _selectedCountry

    private val _isUserNameCorrect: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isUserNameCorrect: StateFlow<Boolean> = _isUserNameCorrect


    private val _name: MutableStateFlow<String> = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _userName: MutableStateFlow<String> = MutableStateFlow("")
    val userName: StateFlow<String> = _userName

    private val _register: MutableStateFlow<ApiResponse<AuthInfoResponse>?> =
        MutableStateFlow(null)
    val register: StateFlow<ApiResponse<AuthInfoResponse>?> = _register

    fun resetRegister() {
        _register.value = null
    }

    fun onUsernameChanged(str: String) {
        if (str.usernameCheck())
            _userName.value = str
    }

    fun onNameChanged(str: String) {
        _name.value = str
    }

    private fun fullNumber() = _selectedCountry.value.dialCode + _phoneNumber.value
    private fun checkIfUsernameCorrect() {

    }

    fun register() {
        viewModelScope.launch {
            val registerRequest =
                RegisterRequest(fullNumber(), username = userName.value, name = name.value)
            authRepository.register(registerRequest).collect {
                when (it) {
                    is ApiResponse.Failure -> {
                        Log.e("TEST", it.toString())
                    }

                    ApiResponse.Loading -> {}
                    is ApiResponse.Success -> {
                        launch {
                            saveAllTokens(
                                refreshToken = it.data.refreshToken,
                                accessToken = it.data.accessToken
                            )
                        }.join()
                        _register.value = it
                    }
                }
            }
        }
    }

    fun initData(reg: Registration) {
        _phoneNumber.value = reg.phone
        _selectedCountry.value = reg.country
    }
}