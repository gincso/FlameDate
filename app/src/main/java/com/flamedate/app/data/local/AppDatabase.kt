package com.flamedate.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.flamedate.app.data.local.dao.MatchDao
import com.flamedate.app.data.local.dao.MessageDao
import com.flamedate.app.data.local.dao.UserDao
import com.flamedate.app.data.local.entity.MatchEntity
import com.flamedate.app.data.local.entity.MessageEntity
import com.flamedate.app.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class, MatchEntity::class, MessageEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun matchDao(): MatchDao
    abstract fun messageDao(): MessageDao

    companion object {
        private const val DB_NAME = "flamedate.db"

        fun create(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
