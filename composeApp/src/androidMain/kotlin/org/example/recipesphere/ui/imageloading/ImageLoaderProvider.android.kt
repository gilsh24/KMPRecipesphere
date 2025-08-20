package org.example.recipesphere.ui.imageloading

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import coil3.ImageLoader
import coil3.network.okhttp.OkHttpNetworkFetcherFactory

@Composable
actual fun rememberAppImageLoader(): ImageLoader {
    val ctx = LocalContext.current
    return remember(ctx) {
        ImageLoader.Builder(ctx)
            .components { add(OkHttpNetworkFetcherFactory()) } // HTTP/HTTPS
            .build()
    }
}
