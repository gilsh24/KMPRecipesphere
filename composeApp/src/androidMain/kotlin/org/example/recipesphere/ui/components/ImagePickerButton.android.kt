package org.example.recipesphere.ui.components

import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import io.ktor.utils.io.core.use

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
actual fun ImagePickerButton(
    enabled: Boolean,
    onPick: (ByteArray) -> Unit
) {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri == null) return@rememberLauncherForActivityResult
        val bytes = try {
            val stream = context.contentResolver.openInputStream(uri)
            if (stream != null) {
                stream.use { stream ->
                    stream.readAllBytes()
                }
            } else null
        } catch (_: Exception) {
            null
        }
        if (bytes != null) onPick(bytes)
    }

    Button(onClick = { launcher.launch("image/*") }, enabled = enabled) {
        Text("Pick image")
    }
}
