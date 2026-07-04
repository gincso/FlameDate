package com.flamedate.app.domain.model

data class Match(
    val id: String,
    val userId: String,
    val matchedUserId: String,
    val matchedUserName: String,
    val matchedUserPhoto: String,
    val matchedAt: Long,
    val isNew: Boolean
)
