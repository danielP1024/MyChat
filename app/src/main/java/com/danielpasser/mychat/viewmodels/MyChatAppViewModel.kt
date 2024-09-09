package com.danielpasser.mychat.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danielpasser.mychat.repositories.TokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyChatAppViewModel @Inject constructor(
    private val tokenRepository: TokenRepository
) : ViewModel() {
    private val _isLogin: MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    val isLogin: StateFlow<Boolean> =
        tokenRepository.getRefreshToken().map { it?.isNotEmpty() == true }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(11111111),
                initialValue = false
            )

    fun logOff() {
        viewModelScope.launch {
            val deleteJobs = listOf(
                launch { tokenRepository.deleteRefreshToken() },
                launch { tokenRepository.deleteAccessToken() },
            )
            deleteJobs.joinAll()
        }

    }
}
