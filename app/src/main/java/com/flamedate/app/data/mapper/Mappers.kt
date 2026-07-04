package com.flamedate.app.data.mapper

import com.flamedate.app.data.local.entity.MatchEntity
import com.flamedate.app.data.local.entity.MessageEntity
import com.flamedate.app.data.local.entity.UserEntity
import com.flamedate.app.domain.model.Match
import com.flamedate.app.domain.model.Message
import com.flamedate.app.domain.model.UserProfile

fun UserEntity.toDomain(): UserProfile = UserProfile(
    id = id,
    name = name,
    age = age,
    bio = bio,
    photos = photos.split("|").filter { it.isNotBlank() },
    interests = interests.split("|").filter { it.isNotBlank() },
    distance = distance,
    occupation = occupation,
    school = school,
    isVerified = isVerified
)

fun UserProfile.toEntity(isCurrentUser: Boolean = false): UserEntity = UserEntity(
    id = id,
    name = name,
    age = age,
    bio = bio,
    photos = photos.joinToString("|"),
    interests = interests.joinToString("|"),
    distance = distance,
    occupation = occupation,
    school = school,
    isVerified = isVerified,
    isCurrentUser = isCurrentUser
)

fun MatchEntity.toDomain(): Match = Match(
    id = id,
    userId = userId,
    matchedUserId = matchedUserId,
    matchedUserName = matchedUserName,
    matchedUserPhoto = matchedUserPhoto,
    matchedAt = matchedAt,
    isNew = isNew
)

fun Match.toEntity(): MatchEntity = MatchEntity(
    id = id,
    userId = userId,
    matchedUserId = matchedUserId,
    matchedUserName = matchedUserName,
    matchedUserPhoto = matchedUserPhoto,
    matchedAt = matchedAt,
    isNew = isNew
)

fun MessageEntity.toDomain(): Message = Message(
    id = id,
    matchId = matchId,
    senderId = senderId,
    text = text,
    timestamp = timestamp,
    isRead = isRead
)

fun Message.toEntity(): MessageEntity = MessageEntity(
    id = id,
    matchId = matchId,
    senderId = senderId,
    text = text,
    timestamp = timestamp,
    isRead = isRead
)
