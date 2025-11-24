package com.example.androidpractice.content

import android.Manifest
import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.datastore.preferences.core.edit
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.androidpractice.di.AppModule
import com.example.androidpractice.utils.AlarmHelper
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalTime

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun EditProfileScreen(navController: NavHostController) {
    val context = LocalContext.current
    val dataStore = AppModule.provideProfileDataStore(context)
    val coroutineScope = rememberCoroutineScope()
    var name by remember { mutableStateOf("") }
    var resumeUrl by remember { mutableStateOf("") }
    var avatarUri by remember { mutableStateOf<Uri?>(null) }
    var favoritePairTime by remember { mutableStateOf("") }
    var favoritePairTimeError by remember { mutableStateOf<String?>(null) }
    var showImagePickerDialog by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var pendingGallery by remember { mutableStateOf(false) }
    var pendingCamera by remember { mutableStateOf(false) }
    var currentCameraUri by remember { mutableStateOf<Uri?>(null) }
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        avatarUri = uri
    }
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
        if (success && currentCameraUri != null) {
            avatarUri = currentCameraUri
        }
    }
    val galleryPermissionState = rememberPermissionState(Manifest.permission.READ_EXTERNAL_STORAGE)
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val prefs = dataStore.data.first()
            name = prefs[PreferencesKeys.PROFILE_NAME] ?: ""
            avatarUri = prefs[PreferencesKeys.PROFILE_AVATAR_URI]?.let { Uri.parse(it) }
            resumeUrl = prefs[PreferencesKeys.PROFILE_RESUME_URL] ?: ""
            favoritePairTime = prefs[PreferencesKeys.FAVORITE_PAIR_TIME] ?: ""
            if (favoritePairTime.isNotBlank()) {
                val (h, m) = favoritePairTime.split(":").map { it.toInt() }
                AlarmHelper.setDailyAlarm(context, h, m, name)
            }
        }
    }
    LaunchedEffect(galleryPermissionState.status, cameraPermissionState.status) {
        if (pendingGallery && galleryPermissionState.status.isGranted) {
            galleryLauncher.launch("image/*")
            pendingGallery = false
        }
        if (pendingCamera && cameraPermissionState.status.isGranted) {
            val photoFile = createTempFile(context)
            currentCameraUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                photoFile
            )
            cameraLauncher.launch(currentCameraUri!!)
            pendingCamera = false
        }
    }
    if (showTimePicker) {
        val parts = favoritePairTime.split(":").filter { it.isNotEmpty() }
        val currentHour = parts.getOrNull(0)?.toIntOrNull() ?: LocalTime.now().hour
        val currentMinute = parts.getOrNull(1)?.toIntOrNull() ?: 0

        TimePickerDialog(
            context,
            { _, hour, minute ->
                val time = String.format("%02d:%02d", hour, minute)
                favoritePairTime = time
                favoritePairTimeError = validateFavoritePairTime(time)
                showTimePicker = false
            },
            currentHour,
            currentMinute,
            true
        ).show()
        showTimePicker = false
    }
    if (showImagePickerDialog) {
        AlertDialog(
            onDismissRequest = { showImagePickerDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    showImagePickerDialog = false
                    if (galleryPermissionState.status.isGranted) {
                        galleryLauncher.launch("image/*")
                    } else {
                        galleryPermissionState.launchPermissionRequest()
                        pendingGallery = true
                    }
                }) { Text("Галерея") }
            },
            dismissButton = {
                TextButton(onClick = {
                    showImagePickerDialog = false
                    if (cameraPermissionState.status.isGranted) {
                        val photoFile = createTempFile(context)
                        currentCameraUri = FileProvider.getUriForFile(
                            context,
                            "${context.packageName}.provider",
                            photoFile
                        )
                        cameraLauncher.launch(currentCameraUri!!)
                    } else {
                        cameraPermissionState.launchPermissionRequest()
                        pendingCamera = true
                    }
                }) { Text("Камера") }
            }
        )
    }
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (avatarUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(avatarUri),
                    contentDescription = "Аватарка",
                    modifier = Modifier.size(100.dp).clickable { showImagePickerDialog = true }
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Аватарка",
                    modifier = Modifier.size(100.dp).clickable { showImagePickerDialog = true }
                )
            }
        }
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("ФИО") })
        OutlinedTextField(value = resumeUrl, onValueChange = { resumeUrl = it }, label = { Text("Ссылка на резюме") })
        OutlinedTextField(
            value = favoritePairTime,
            onValueChange = { newValue ->
                val filtered = newValue.filter { it.isDigit() || it == ':' }
                if (filtered.count { it == ':' } <= 1 && filtered.length <= 5) {
                    favoritePairTime = filtered
                    favoritePairTimeError = validateFavoritePairTime(filtered)
                }
            },
            label = { Text("Время любимой пары") },
            trailingIcon = {
                IconButton(onClick = { showTimePicker = true }) {
                    Icon(Icons.Default.Notifications, contentDescription = "Выбрать время")
                }
            },
            isError = favoritePairTimeError != null,
            supportingText = {
                favoritePairTimeError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            }
        )
        Button(onClick = {
            coroutineScope.launch {
                dataStore.edit { prefs ->
                    prefs[PreferencesKeys.PROFILE_NAME] = name
                    prefs[PreferencesKeys.PROFILE_AVATAR_URI] = avatarUri?.toString() ?: ""
                    prefs[PreferencesKeys.PROFILE_RESUME_URL] = resumeUrl
                    prefs[PreferencesKeys.FAVORITE_PAIR_TIME] = favoritePairTime
                }
            }
            if (favoritePairTime.isNotBlank() && favoritePairTimeError == null) {
                val (h, m) = favoritePairTime.split(":").map { it.toInt() }
                AlarmHelper.setDailyAlarm(context, h, m, name)
            } else {
                AlarmHelper.cancelAlarm(context)
            }
            navController.popBackStack()
        }, enabled = favoritePairTimeError == null && favoritePairTime.isNotBlank()) {
            Text("Готово")
        }
    }
}
private fun validateFavoritePairTime(time: String): String? {
    if (time.isBlank()) {
        return "Поле не должно быть пустым"
    }
    if (!time.matches(Regex("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$"))) {
        return "Некорректный формат времени (HH:mm)"
    }
    return null
}
private fun createTempFile(context: Context): File {
    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile("avatar", ".jpg", storageDir).apply {
        createNewFile()
    }
}