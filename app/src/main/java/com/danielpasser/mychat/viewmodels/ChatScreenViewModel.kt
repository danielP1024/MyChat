package com.danielpasser.mychat.viewmodels

import androidx.lifecycle.ViewModel
import com.danielpasser.mychat.models.ChatMessage
import com.danielpasser.mychat.repositories.TokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatScreenViewModel @Inject constructor(
) : ViewModel() {


    val myUserId=0L

    val messageList = listOf(
        ChatMessage(
            text = "MESSAGE MESSAGEMESSAGEMESSAGEMESSAGEMESSAGEMESSAGEMESSAGEMESSAGEMESSAGEMESSAGEMESSAGE MESSAGEMESSAGEMESSAGE/nMESSAGEMESSAGE",
            date = "2024-09-08T14:31:17.896Z",
            userId = 1
        ),

        ChatMessage(
            text = "MESSAGE MESSAGEM",
            date = "2024-09-08T14:32:17.896Z",
            userId = 0
        ),

        ChatMessage(
            text = "MESSAGE MESSAGEM",
            date = "2024-09-08T14:31:17.896Z",
            userId = 1
        ),
    )


}