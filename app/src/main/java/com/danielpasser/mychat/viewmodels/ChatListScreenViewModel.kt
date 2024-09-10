package com.danielpasser.mychat.viewmodels

import androidx.lifecycle.ViewModel
import com.danielpasser.mychat.models.ChatMessagePreview
import dagger.hilt.android.lifecycle.HiltViewModel
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
            message = "I am Batman",
            userName = "Batman",
        ),
    )
}