package com.flamedate.app.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.flamedate.app.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Upsert
    suspend fun upsertUsers(users: List<UserEntity>)

    @Upsert
    suspend fun upsertUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE is_current_user = 0 ORDER BY RANDOM()")
    fun getDiscoverProfiles(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE id = :userId")
    fun getProfile(userId: String): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE is_current_user = 1 LIMIT 1")
    fun getCurrentUser(): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE is_current_user = 1 LIMIT 1")
    suspend fun getCurrentUserOnce(): UserEntity?

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getProfileOnce(userId: String): UserEntity?

    @Query("DELETE FROM users")
    suspend fun deleteAll()
}
