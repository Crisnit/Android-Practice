package com.example.androidpractice.content

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Environment
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.datastore.preferences.core.emptyPreferences
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.androidpractice.di.AppModule
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.File

@Composable
fun ProfileScreen(navController: NavHostController) {
    val context = LocalContext.current
    val dataStore = AppModule.provideProfileDataStore(context)

    val profileState by dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { prefs ->
            ProfileData(
                name = prefs[PreferencesKeys.PROFILE_NAME] ?: "Без имени",
                avatarUri = prefs[PreferencesKeys.PROFILE_AVATAR_URI] ?: "",
                resumeUrl = prefs[PreferencesKeys.PROFILE_RESUME_URL] ?: ""
            )
        }
        .collectAsState(initial = ProfileData("", "", ""))

    var downloadId by remember { mutableStateOf<Long?>(null) }
    val downloadReceiver = remember {
        object : BroadcastReceiver() {
            @SuppressLint("Range")
            override fun onReceive(context: Context?, intent: Intent?) {
                val id: Long = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)!!
                if (downloadId == id) {
                    val downloadManager = context?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    val query = DownloadManager.Query().setFilterById(id)
                    val cursor = downloadManager.query(query)
                    if (cursor.moveToFirst()) {
                        val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                        if (status == DownloadManager.STATUS_SUCCESSFUL) {
                            val uri = FileProvider.getUriForFile(
                                context,
                                "${context.packageName}.provider",
                                File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "resume.pdf")
                            )
                            val openIntent = Intent(Intent.ACTION_VIEW).apply {
                                setDataAndType(uri, "application/pdf")
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                            context.startActivity(openIntent)
                        }
                    }
                    cursor.close()
                    context.unregisterReceiver(this)
                }
            }
        }
    }

    DisposableEffect(Unit) {
        ContextCompat.registerReceiver(
            context,
            downloadReceiver,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
            ContextCompat.RECEIVER_EXPORTED
        )
        onDispose {
            context.unregisterReceiver(downloadReceiver)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        IconButton(onClick = { navController.navigate(NavigationRoutes.EditProfile.route) }) {
            Icon(Icons.Default.Edit, contentDescription = "Редактировать")
        }
        if (profileState.avatarUri.isNotEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(profileState.avatarUri.toUri()),
                contentDescription = "Аватарка",
                modifier = Modifier.size(100.dp)
            )
        } else {
            Icon(Icons.Default.Person, contentDescription = "Аватарка", modifier = Modifier.size(100.dp))
        }
        Text(profileState.name, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            downloadAndOpenResume(context, profileState.resumeUrl) { id ->
                downloadId = id
            }
        }) {
            Text("Резюме")
        }
    }
}

private fun downloadAndOpenResume(context: Context, url: String, onDownloadStarted: (Long) -> Unit) {
    if (url.isEmpty()) return
    val request = DownloadManager.Request(url.toUri())
        .setTitle("resume.pdf")
        .setDescription("Скачивание PDF")
        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "resume.pdf")

    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    val downloadId = downloadManager.enqueue(request)
    onDownloadStarted(downloadId)
}

data class ProfileData(val name: String, val avatarUri: String, val resumeUrl: String)