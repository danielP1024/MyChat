package com.danielpasser.mychat.models

data class ChatMessagePreview(
    val userId: Long = 1L,
    val userName: String,
    val message: String,
    val avatar: Avatar? = null
) {
}