package org.example.recipesphere.ui.imageloading

import androidx.compose.runtime.Composable
import coil3.ImageLoader

@Composable
expect fun rememberAppImageLoader(): ImageLoader
