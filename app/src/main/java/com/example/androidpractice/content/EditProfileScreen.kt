package com.example.androidpractice.content

import android.Manifest
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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
import androidx.core.net.toUri
import androidx.datastore.preferences.core.edit
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.androidpractice.di.AppModule
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun EditProfileScreen(navController: NavHostController) {
    val context = LocalContext.current
    val dataStore = AppModule.provideProfileDataStore(context)
    val coroutineScope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var resumeUrl by remember { mutableStateOf("") }
    var avatarUri by remember { mutableStateOf<Uri?>(null) }
    var showImagePickerDialog by remember { mutableStateOf(false) }
    var pendingGallery by remember { mutableStateOf(false) }
    var pendingCamera by remember { mutableStateOf(false) }
    var currentCameraUri by remember { mutableStateOf<Uri?>(null) }

    LaunchedEffect(Unit) {
        val prefs = dataStore.data.first()
        name = prefs[PreferencesKeys.PROFILE_NAME] ?: ""
        resumeUrl = prefs[PreferencesKeys.PROFILE_RESUME_URL] ?: ""
        val uriString = prefs[PreferencesKeys.PROFILE_AVATAR_URI] ?: ""
        if (uriString.isNotEmpty()) avatarUri = uriString.toUri()
    }

    val galleryPermission =
        Manifest.permission.READ_MEDIA_IMAGES
    val galleryPermissionState = rememberPermissionState(galleryPermission)
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { avatarUri = it }
    }
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            currentCameraUri?.let { avatarUri = it }
        }
    }

    LaunchedEffect(galleryPermissionState.status) {
        if (pendingGallery) {
            if (galleryPermissionState.status.isGranted) {
                galleryLauncher.launch("image/*")
            }
            pendingGallery = false
        }
    }

    LaunchedEffect(cameraPermissionState.status) {
        if (pendingCamera) {
            if (cameraPermissionState.status.isGranted) {
                val photoFile = createTempFile(context)
                currentCameraUri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider",
                    photoFile
                )
                cameraLauncher.launch(currentCameraUri!!)
            }
            pendingCamera = false
        }
    }

    if (showImagePickerDialog) {
        AlertDialog(
            onDismissRequest = { showImagePickerDialog = false },
            title = { Text("Выберите источник") },
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
        Button(onClick = {
            coroutineScope.launch {
                dataStore.edit { prefs ->
                    prefs[PreferencesKeys.PROFILE_NAME] = name
                    prefs[PreferencesKeys.PROFILE_AVATAR_URI] = avatarUri?.toString() ?: ""
                    prefs[PreferencesKeys.PROFILE_RESUME_URL] = resumeUrl
                }
            }
            navController.popBackStack()
        }) {
            Text("Готово")
        }
    }
}

private fun createTempFile(context: Context): File {
    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile("avatar", ".jpg", storageDir).apply {
        createNewFile()
    }
}