package com.flamedate.app.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.flamedate.app.data.local.entity.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Upsert
    suspend fun upsertMessages(messages: List<MessageEntity>)

    @Upsert
    suspend fun upsertMessage(message: MessageEntity)

    @Query("SELECT * FROM messages WHERE match_id = :matchId ORDER BY timestamp ASC")
    fun getMessages(matchId: String): Flow<List<MessageEntity>>

    @Query("UPDATE messages SET is_read = 1 WHERE match_id = :matchId AND sender_id != :currentUserId")
    suspend fun markMessagesRead(matchId: String, currentUserId: String)

    @Query("DELETE FROM messages")
    suspend fun deleteAll()
}
