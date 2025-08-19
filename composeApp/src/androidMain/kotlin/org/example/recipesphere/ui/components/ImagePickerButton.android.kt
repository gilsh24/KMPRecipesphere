package org.example.recipesphere.ui.components

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.activity.result.contract.ActivityResultContracts.TakePicture
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File

@Composable
actual fun ImagePickerButton(
    enabled: Boolean,
    onPick: (ByteArray) -> Unit
) {
    val context = LocalContext.current
    var cameraUri by remember { mutableStateOf<Uri?>(null) }

    // Gallery picker -> returns a content Uri
    val galleryLauncher = rememberLauncherForActivityResult(GetContent()) { uri: Uri? ->
        if (uri == null) return@rememberLauncherForActivityResult
        val bytes = try {
            context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
        } catch (_: Exception) { null }
        if (bytes != null) onPick(bytes)
    }

    // Camera capture (full-res) -> we pass a pre-created Uri and read it back
    val takePictureLauncher = rememberLauncherForActivityResult(TakePicture()) { success: Boolean ->
        if (!success) return@rememberLauncherForActivityResult
        val uri = cameraUri ?: return@rememberLauncherForActivityResult
        val bytes = try {
            context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
        } catch (_: Exception) { null }
        if (bytes != null) onPick(bytes)
    }

    // Request CAMERA permission if needed
    val permissionLauncher = rememberLauncherForActivityResult(RequestPermission()) { granted ->
        if (granted) {
            cameraUri = createImageUri(context)
            takePictureLauncher.launch(cameraUri!!)
        }
    }

    Row {
        Button(onClick = { galleryLauncher.launch("image/*") }, enabled = enabled) {
            Text("Gallery")
        }
        Button(
            onClick = {
                if (!enabled) return@Button
                val granted = ContextCompat.checkSelfPermission(
                    context, Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
                if (granted) {
                    cameraUri = createImageUri(context)
                    takePictureLauncher.launch(cameraUri!!)
                } else {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
            },
            enabled = enabled
        ) { Text("Camera") }
    }
}

private fun createImageUri(context: Context): Uri {
    val imagesDir = File(context.cacheDir, "images").apply { mkdirs() }
    val file = File.createTempFile("capture_", ".jpg", imagesDir)
    return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
}
