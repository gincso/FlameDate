package com.flamedate.app.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.flamedate.app.data.local.entity.MatchEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MatchDao {

    @Upsert
    suspend fun upsertMatches(matches: List<MatchEntity>)

    @Upsert
    suspend fun upsertMatch(match: MatchEntity)

    @Query("SELECT * FROM matches ORDER BY matched_at DESC")
    fun getMatches(): Flow<List<MatchEntity>>

    @Query("SELECT * FROM matches WHERE id = :matchId")
    suspend fun getMatch(matchId: String): MatchEntity?

    @Query("UPDATE matches SET is_new = 0 WHERE id = :matchId")
    suspend fun clearNewFlag(matchId: String)

    @Query("DELETE FROM matches")
    suspend fun deleteAll()
}
