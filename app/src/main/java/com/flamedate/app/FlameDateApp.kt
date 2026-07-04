package com.flamedate.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.flamedate.app.data.local.AppDatabase
import com.flamedate.app.data.repository.DatingRepositoryImpl
import com.flamedate.app.domain.repository.DatingRepository
import com.flamedate.app.update.UpdateChecker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class FlameDateApp : Application() {

    val database: AppDatabase by lazy { AppDatabase.create(this) }
    val repository: DatingRepository by lazy {
        DatingRepositoryImpl(database)
    }
    val updateChecker: UpdateChecker by lazy { UpdateChecker(this) }

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        seedData()
        checkForUpdates()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                UPDATE_CHANNEL_ID,
                "App Updates",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications about app updates"
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun seedData() {
        appScope.launch {
            repository.seedMockData()
        }
    }

    private fun checkForUpdates() {
        appScope.launch {
            updateChecker.checkForUpdate()
        }
    }

    companion object {
        const val UPDATE_CHANNEL_ID = "app_updates"
    }
}
