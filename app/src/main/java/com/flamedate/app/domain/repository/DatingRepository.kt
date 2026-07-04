package com.flamedate.app.domain.repository

import com.flamedate.app.domain.model.Match
import com.flamedate.app.domain.model.Message
import com.flamedate.app.domain.model.SwipeAction
import com.flamedate.app.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface DatingRepository {
    fun getDiscoverProfiles(): Flow<List<UserProfile>>
    suspend fun swipe(userId: String, action: SwipeAction): Boolean
    fun getMatches(): Flow<List<Match>>
    fun getMessages(matchId: String): Flow<List<Message>>
    suspend fun sendMessage(matchId: String, text: String)
    suspend fun markMessagesRead(matchId: String)
    suspend fun clearNewMatchFlag(matchId: String)
    fun getProfile(userId: String): Flow<UserProfile?>
    suspend fun updateProfile(profile: UserProfile)
    suspend fun seedMockData()
}
