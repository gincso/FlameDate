package com.flamedate.app.domain.model

data class Message(
    val id: String,
    val matchId: String,
    val senderId: String,
    val text: String,
    val timestamp: Long,
    val isRead: Boolean
)
