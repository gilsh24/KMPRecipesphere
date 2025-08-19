package org.example.recipesphere.ui.components

import android.Manifest
import android.location.Geocoder
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import java.util.Locale

@Composable
actual fun LocationPickerButton(
    enabled: Boolean,
    onPicked: (label: String?, lat: Double?, lon: Double?) -> Unit
) {
    val context = LocalContext.current

    val requestPerms = rememberLauncherForActivityResult(
        RequestMultiplePermissions()
    ) { grants ->
        val fine = grants[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val coarse = grants[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (fine || coarse) fetchLocation(context, onPicked)
    }

    Button(
        onClick = {
            if (!enabled) return@Button
            val fineGranted = ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            val coarseGranted = ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            if (fineGranted || coarseGranted) {
                fetchLocation(context, onPicked)
            } else {
                requestPerms.launch(arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ))
            }
        },
        enabled = enabled
    ) { Text("Use my location") }
}

private fun fetchLocation(
    context: android.content.Context,
    onPicked: (label: String?, lat: Double?, lon: Double?) -> Unit
) {
    val client = LocationServices.getFusedLocationProviderClient(context)
    val token = CancellationTokenSource()
    client.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, token.token)
        .addOnSuccessListener { loc ->
            if (loc == null) {
                onPicked(null, null, null); return@addOnSuccessListener
            }
            val lat = loc.latitude
            val lon = loc.longitude
            // Best-effort reverse geocode
            val label = try {
                @Suppress("DEPRECATION")
                if (Build.VERSION.SDK_INT >= 33) {
                    val geocoder = Geocoder(context)
                    val list = geocoder.getFromLocation(lat, lon, 1)
                    list?.firstOrNull()?.let { a ->
                        val city = a.locality ?: a.subAdminArea
                        val country = a.countryCode ?: a.countryName
                        listOfNotNull(city, country).joinToString(", ")
                    }
                } else {
                    val geocoder = Geocoder(context, Locale.getDefault())
                    val list = geocoder.getFromLocation(lat, lon, 1)
                    list?.firstOrNull()?.let { a ->
                        val city = a.locality ?: a.subAdminArea
                        val country = a.countryCode ?: a.countryName
                        listOfNotNull(city, country).joinToString(", ")
                    }
                }
            } catch (_: Exception) { null }
            onPicked(label, lat, lon)
        }
        .addOnFailureListener { onPicked(null, null, null) }
}
