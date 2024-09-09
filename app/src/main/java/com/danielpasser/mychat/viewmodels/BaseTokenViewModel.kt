package com.danielpasser.mychat.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danielpasser.mychat.repositories.TokenRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

abstract class BaseTokenViewModel(private val tokenRepository: TokenRepository) : ViewModel() {
    private suspend fun saveAccessToken(token: String) {
        tokenRepository.saveAccessToken(token)
    }

    private suspend fun saveRefreshToken(refreshToken: String) {
        tokenRepository.saveRefreshToken(refreshToken)
    }

    protected suspend fun saveAllTokens(refreshToken: String, accessToken: String) {
        viewModelScope.launch {
            val downloadJobs = listOf(
                launch { saveAccessToken(accessToken) },
                launch { saveRefreshToken(refreshToken) },
            )
            downloadJobs.joinAll()
        }
    }


    protected  suspend fun deleteAccessToken() {
        tokenRepository.deleteAccessToken()
    }

    protected suspend fun deleteRefreshToken() {
        tokenRepository.deleteRefreshToken()
    }
}