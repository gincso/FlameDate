package com.flamedate.app.data.repository

import com.flamedate.app.data.local.AppDatabase
import com.flamedate.app.data.local.entity.MatchEntity
import com.flamedate.app.data.local.entity.MessageEntity
import com.flamedate.app.data.local.entity.UserEntity
import com.flamedate.app.data.mapper.toDomain
import com.flamedate.app.data.mapper.toEntity
import com.flamedate.app.domain.model.Match
import com.flamedate.app.domain.model.Message
import com.flamedate.app.domain.model.SwipeAction
import com.flamedate.app.domain.model.UserProfile
import com.flamedate.app.domain.repository.DatingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class DatingRepositoryImpl(
    private val db: AppDatabase
) : DatingRepository {

    private val userDao = db.userDao()
    private val matchDao = db.matchDao()
    private val messageDao = db.messageDao()

    override fun getDiscoverProfiles(): Flow<List<UserProfile>> {
        return userDao.getDiscoverProfiles().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun swipe(userId: String, action: SwipeAction): Boolean {
        if (action == SwipeAction.LIKE || action == SwipeAction.SUPER_LIKE) {
            val user = userDao.getProfileOnce(userId)
            if (user != null) {
                val matchId = UUID.randomUUID().toString()
                matchDao.upsertMatch(
                    MatchEntity(
                        id = matchId,
                        userId = "current_user",
                        matchedUserId = userId,
                        matchedUserName = user.name,
                        matchedUserPhoto = user.photos.split("|").firstOrNull() ?: "",
                        matchedAt = System.currentTimeMillis(),
                        isNew = true
                    )
                )
            }
            return true
        }
        return false
    }

    override fun getMatches(): Flow<List<Match>> {
        return matchDao.getMatches().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getMessages(matchId: String): Flow<List<Message>> {
        return messageDao.getMessages(matchId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun sendMessage(matchId: String, text: String) {
        val msg = Message(
            id = UUID.randomUUID().toString(),
            matchId = matchId,
            senderId = "current_user",
            text = text,
            timestamp = System.currentTimeMillis(),
            isRead = false
        )
        messageDao.upsertMessage(msg.toEntity())
    }

    override suspend fun markMessagesRead(matchId: String) {
        messageDao.markMessagesRead(matchId, "current_user")
    }

    override suspend fun clearNewMatchFlag(matchId: String) {
        matchDao.clearNewFlag(matchId)
    }

    override fun getProfile(userId: String): Flow<UserProfile?> {
        return userDao.getProfile(userId).map { it?.toDomain() }
    }

    override suspend fun updateProfile(profile: UserProfile) {
        userDao.upsertUser(profile.toEntity(isCurrentUser = true))
    }

    override suspend fun seedMockData() {
        userDao.deleteAll()
        matchDao.deleteAll()
        messageDao.deleteAll()

        val currentUser = UserEntity(
            id = "current_user",
            name = "You",
            age = 25,
            bio = "Looking for meaningful connections",
            photos = "",
            interests = "Travel|Music|Food",
            distance = 0.0,
            occupation = "Designer",
            school = "Art School",
            isVerified = true,
            isCurrentUser = true
        )
        userDao.upsertUser(currentUser)

        val mockProfiles = listOf(
            UserEntity("u1", "Emma", 24, "Love hiking and coffee", "https://i.pravatar.cc/400?img=1", "Hiking|Coffee|Photography", 2.5, "Engineer", "MIT", true, false),
            UserEntity("u2", "Sophia", 26, "Foodie & traveler", "https://i.pravatar.cc/400?img=5", "Travel|Cooking|Yoga", 1.2, "Doctor", "Harvard", true, false),
            UserEntity("u3", "Olivia", 23, "Bookworm and dreamer", "https://i.pravatar.cc/400?img=9", "Reading|Art|Music", 3.8, "Teacher", "UCLA", false, false),
            UserEntity("u4", "Isabella", 27, "Adventure seeker", "https://i.pravatar.cc/400?img=3", "Climbing|Diving|Travel", 0.8, "Architect", "Columbia", true, false),
            UserEntity("u5", "Mia", 25, "Dog mom & plant lover", "https://i.pravatar.cc/400?img=6", "Pets|Gardening|Cooking", 5.0, "Writer", "NYU", false, false),
            UserEntity("u6", "Charlotte", 28, "Yoga instructor", "https://i.pravatar.cc/400?img=12", "Yoga|Meditation|Health", 4.2, "Instructor", "Stanford", true, false),
            UserEntity("u7", "Amelia", 22, "Art & fashion enthusiast", "https://i.pravatar.cc/400?img=8", "Fashion|Art|Dancing", 1.5, "Student", "Parsons", false, false),
            UserEntity("u8", "Harper", 26, "Music producer", "https://i.pravatar.cc/400?img=4", "Music|Producing|Gaming", 3.0, "Producer", "Berklee", true, false),
            UserEntity("u9", "Evelyn", 24, "Beach volleyball player", "https://i.pravatar.cc/400?img=2", "Sports|Beach|Travel", 2.1, "Coach", "USC", false, false),
            UserEntity("u10", "Abigail", 29, "Wine & dine", "https://i.pravatar.cc/400?img=7", "Wine|Fine dining|Travel", 6.0, "Sommelier", "CIA", true, false)
        )
        userDao.upsertUsers(mockProfiles)

        val mockMatches = listOf(
            MatchEntity("m1", "current_user", "u2", "Sophia", "https://i.pravatar.cc/400?img=5", System.currentTimeMillis() - 3600000, true),
            MatchEntity("m2", "current_user", "u4", "Isabella", "https://i.pravatar.cc/400?img=3", System.currentTimeMillis() - 86400000, false)
        )
        matchDao.upsertMatches(mockMatches)

        val mockMessages = listOf(
            MessageEntity("msg1", "m1", "u2", "Hey! How are you?", System.currentTimeMillis() - 3000000, true),
            MessageEntity("msg2", "m1", "current_user", "I'm great! Love your travel pics", System.currentTimeMillis() - 2000000, true),
            MessageEntity("msg3", "m1", "u2", "Thanks! We should grab coffee sometime", System.currentTimeMillis() - 1000000, false),
            MessageEntity("msg4", "m2", "u4", "Hi there! Want to go hiking?", System.currentTimeMillis() - 80000000, true),
            MessageEntity("msg5", "m2", "current_user", "Sounds awesome! When?", System.currentTimeMillis() - 70000000, true),
            MessageEntity("msg6", "m2", "u4", "This weekend?", System.currentTimeMillis() - 60000000, true)
        )
        messageDao.upsertMessages(mockMessages)
    }
}
