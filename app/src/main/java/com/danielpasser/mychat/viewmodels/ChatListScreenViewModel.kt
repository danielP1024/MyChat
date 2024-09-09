package com.danielpasser.mychat.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danielpasser.mychat.models.ChatMessage
import com.danielpasser.mychat.models.ChatMessagePreview
import com.danielpasser.mychat.repositories.TokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatListScreenViewModel @Inject constructor(
) : ViewModel() {
    val messageList = listOf(
        ChatMessagePreview(
            message = "MESSAGE MESSAGEMESSAGEMESSAGEMESSAGEMESSAGEMESSAGEMESSAGEMESSAGEMESSAGEMESSAGEMESSAGE MESSAGEMESSAGEMESSAGE/nMESSAGEMESSAGE",
            userName = "Ivan Ivanov",
            userId = 1
        ),

        ChatMessagePreview(
            message = "MESSAGE MESSAGEM",
            userName = "Petr Petrov",
        ),

        ChatMessagePreview(
            message = "Batman",
            userName = "I am Batman",
        ),
    )
}