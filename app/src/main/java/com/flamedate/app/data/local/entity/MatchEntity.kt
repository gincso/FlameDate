package com.flamedate.app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "matches")
data class MatchEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "user_id") val userId: String,
    @ColumnInfo(name = "matched_user_id") val matchedUserId: String,
    @ColumnInfo(name = "matched_user_name") val matchedUserName: String,
    @ColumnInfo(name = "matched_user_photo") val matchedUserPhoto: String,
    @ColumnInfo(name = "matched_at") val matchedAt: Long,
    @ColumnInfo(name = "is_new") val isNew: Boolean
)
