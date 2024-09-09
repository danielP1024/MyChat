package com.danielpasser.mychat.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.danielpasser.mychat.compose.Registration
import com.danielpasser.mychat.models.Country
import com.danielpasser.mychat.models.networkmodels.request.CheckAuthCodeRequest
import com.danielpasser.mychat.models.networkmodels.request.SendAuthCodeRequest
import com.danielpasser.mychat.models.networkmodels.response.AuthInfoResponse
import com.danielpasser.mychat.models.networkmodels.response.SendAuthCodeResponse
import com.danielpasser.mychat.repositories.AuthRepository
import com.danielpasser.mychat.repositories.TokenRepository
import com.danielpasser.mychat.utils.ApiResponse
import com.danielpasser.mychat.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val application: Application,
    private val authRepository: AuthRepository,
    tokenRepository: TokenRepository
) : BaseTokenViewModel(tokenRepository) {

    private val _isLogin: MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    val isLogin: StateFlow<Boolean> = _isLogin

    private val _countries: MutableStateFlow<List<Country>> =
        MutableStateFlow(Utils.getCountries(application))
    val countries: StateFlow<List<Country>> = _countries

    private val _phoneNumber: MutableStateFlow<String> = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber

    private val _selectedCountry: MutableStateFlow<Country> = MutableStateFlow(currentCountry())
    val selectedCountry: StateFlow<Country> = _selectedCountry


    private val _sendAuthCode: MutableStateFlow<ApiResponse<SendAuthCodeResponse>?> =
        MutableStateFlow(null)
    val sendAuthCode: StateFlow<ApiResponse<SendAuthCodeResponse>?> = _sendAuthCode

    private val _showUserNotRegDialog: MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    val showUserNotRegDialog: StateFlow<Boolean?> = _showUserNotRegDialog

    private val _checkAuthCode: MutableStateFlow<ApiResponse<AuthInfoResponse>?> =
        MutableStateFlow(null)
    val checkAuthCode: StateFlow<ApiResponse<AuthInfoResponse>?> = _checkAuthCode

    private fun fullNumber() = _selectedCountry.value.dialCode + _phoneNumber.value

    private fun currentCountry(): Country {
        val cCode: String = Locale.getDefault().country
        return _countries.value.first { country -> country.isoCode == cCode }
    }

    fun onPhoneNumberChanged(phoneNumber: String) {
        _phoneNumber.value = phoneNumber
    }

    fun onCountrySelected(selectedCountry: Country) {
        _selectedCountry.value =
            _countries.value.first { country -> country.isoCode == selectedCountry.isoCode }
    }

    fun sendAuthCode() {
        viewModelScope.launch {
            authRepository.sendAuthCode(SendAuthCodeRequest(fullNumber())).collect {
                _sendAuthCode.value = it
            }
        }
    }

    private fun checkAuthCode(smsCode: String) {
        viewModelScope.launch {
            authRepository.checkAuthCode(CheckAuthCodeRequest(phone = fullNumber(), code = smsCode))
                .collect { authInfoResponse ->
                    when (authInfoResponse) {
                        is ApiResponse.Success -> {
                            clearAlertDialog()
                            onCheckAuthCodeSuccess(authInfoResponse.data)
                        }

                        is ApiResponse.Failure -> {
                            Log.e("TEST", authInfoResponse.toString())
                        }

                        else -> _checkAuthCode.value = authInfoResponse
                    }
                }
        }
    }


    private fun onCheckAuthCodeSuccess(authInfoResponse: AuthInfoResponse) {
        if (authInfoResponse.isUserExists == true) onLogin(authInfoResponse)
        else _showUserNotRegDialog.value = true
    }


    private fun onLogin(authInfoResponse: AuthInfoResponse) {
        viewModelScope.launch {
            val job = launch {
                saveAllTokens(
                    accessToken = authInfoResponse.accessToken,
                    refreshToken = authInfoResponse.refreshToken
                )
            }
            job.join()
            _isLogin.value = true
        }
    }

    fun clearAlertDialog() {
        _sendAuthCode.value = null
    }

    fun onSmsCodeReceived(smsCode: String) {
        checkAuthCode(smsCode)
    }

    fun dismissUserNotRegDialog() {
        _showUserNotRegDialog.value = false
    }

    fun registerNewUserInfo() =
        Registration(country = _selectedCountry.value, phone = _phoneNumber.value)

}