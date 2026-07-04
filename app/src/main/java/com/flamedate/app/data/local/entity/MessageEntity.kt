package com.flamedate.app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "match_id") val matchId: String,
    @ColumnInfo(name = "sender_id") val senderId: String,
    val text: String,
    val timestamp: Long,
    @ColumnInfo(name = "is_read") val isRead: Boolean
)
