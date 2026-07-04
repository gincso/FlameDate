package com.flamedate.app.update

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.content.FileProvider
import com.flamedate.app.BuildConfig
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

data class UpdateInfo(
    val versionCode: Int,
    val versionName: String,
    val downloadUrl: String,
    val releaseNotes: String = ""
)

class UpdateChecker(private val context: Context) {

    companion object {
        private const val UPDATE_URL = "https://flamedate.app/update.json"
        private const val APK_FILE_NAME = "flamedate_update.apk"
    }

    private val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    private var pendingDownloadId: Long = -1

    suspend fun checkForUpdate() {
        try {
            val updateInfo = withContext(Dispatchers.IO) {
                fetchUpdateInfo()
            }

            if (updateInfo != null && updateInfo.versionCode > BuildConfig.VERSION_CODE) {
                downloadAndInstall(updateInfo)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun fetchUpdateInfo(): UpdateInfo? {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL(UPDATE_URL)
                val connection = url.openConnection() as HttpURLConnection
                connection.connectTimeout = 5000
                connection.readTimeout = 5000

                val inputStream = connection.inputStream
                val json = inputStream.bufferedReader().use { it.readText() }
                Gson().fromJson(json, UpdateInfo::class.java)
            } catch (e: Exception) {
                null
            }
        }
    }

    private fun downloadAndInstall(updateInfo: UpdateInfo) {
        val apkFile = File(context.cacheDir, "updates/$APK_FILE_NAME")
        apkFile.parentFile?.mkdirs()

        val request = DownloadManager.Request(Uri.parse(updateInfo.downloadUrl)).apply {
            setTitle("FlameDate Update")
            setDescription("Downloading ${updateInfo.versionName}")
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationUri(Uri.fromFile(apkFile))
            setAllowedOverMetered(true)
            setAllowedOverRoaming(true)
        }

        pendingDownloadId = downloadManager.enqueue(request)

        context.registerReceiver(
            object : BroadcastReceiver() {
                override fun onReceive(ctx: Context, intent: Intent) {
                    val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                    if (id == pendingDownloadId) {
                        installApk(apkFile)
                        ctx.unregisterReceiver(this)
                    }
                }
            },
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )
    }

    private fun installApk(apkFile: File) {
        val uri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(
                context,
                "${BuildConfig.APPLICATION_ID}.fileprovider",
                apkFile
            )
        } else {
            Uri.fromFile(apkFile)
        }

        val installIntent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/vnd.android.package-archive")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (context.packageManager.canRequestPackageInstalls()) {
                context.startActivity(installIntent)
            }
        } else {
            context.startActivity(installIntent)
        }
    }
}
