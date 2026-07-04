package com.flamedate.app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val name: String,
    val age: Int,
    val bio: String,
    val photos: String,
    val interests: String,
    val distance: Double,
    val occupation: String,
    val school: String,
    @ColumnInfo(name = "is_verified") val isVerified: Boolean,
    @ColumnInfo(name = "is_current_user") val isCurrentUser: Boolean = false
)
