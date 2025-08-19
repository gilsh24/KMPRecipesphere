package org.example.recipesphere.ui.components

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

@Composable
actual fun LocationPickerButton(
    enabled: Boolean,
    onPicked: (label: String?, lat: Double?, lon: Double?) -> Unit
) {
    val context = LocalContext.current

    // Ask for FINE location when needed
    val requestPermission = rememberLauncherForActivityResult(RequestPermission()) { granted ->
        if (granted) fetchLocation(context, onPicked)
        else onPicked(null, null, null) // user denied
    }

    Button(
        onClick = {
            if (!enabled) return@Button
            val granted = ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            if (granted) fetchLocation(context, onPicked)
            else requestPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        },
        enabled = enabled
    ) {
        Text("Use my location")
    }
}

@SuppressLint("MissingPermission")
private fun fetchLocation(
    context: android.content.Context,
    onPicked: (label: String?, lat: Double?, lon: Double?) -> Unit
) {
    val fused = LocationServices.getFusedLocationProviderClient(context)

    // Try last known first (instant), then fall back to a fresh single reading
    fused.lastLocation
        .addOnSuccessListener { loc ->
            if (loc != null) {
                val label = "${"%.4f".format(loc.latitude)}, ${"%.4f".format(loc.longitude)}"
                onPicked(label, loc.latitude, loc.longitude)
            } else {
                // fresh single-shot
                val token = CancellationTokenSource()
                fused.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, token.token)
                    .addOnSuccessListener { fresh ->
                        if (fresh != null) {
                            val label = "${"%.4f".format(fresh.latitude)}, ${"%.4f".format(fresh.longitude)}"
                            onPicked(label, fresh.latitude, fresh.longitude)
                        } else {
                            onPicked(null, null, null)
                        }
                    }
                    .addOnFailureListener { onPicked(null, null, null) }
            }
        }
        .addOnFailureListener { onPicked(null, null, null) }
}
