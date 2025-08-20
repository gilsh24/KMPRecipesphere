package org.example.recipesphere.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.painterResource
import org.example.recipesphere.resources.Res
import org.example.recipesphere.resources.diehard
import org.example.recipesphere.ui.imageloading.rememberAppImageLoader

@Composable
fun Poster(
    url: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    val imageLoader = rememberAppImageLoader()

    AsyncImage(
        model = url ?: "",
        imageLoader = imageLoader,
        contentDescription = null,
        modifier = modifier,
        contentScale = contentScale,
        placeholder = painterResource(Res.drawable.diehard),
        error = painterResource(Res.drawable.diehard)
    )
}
