package com.flamedate.app.domain.model

data class UserProfile(
    val id: String,
    val name: String,
    val age: Int,
    val bio: String,
    val photos: List<String>,
    val interests: List<String>,
    val distance: Double,
    val occupation: String,
    val school: String,
    val isVerified: Boolean
)
