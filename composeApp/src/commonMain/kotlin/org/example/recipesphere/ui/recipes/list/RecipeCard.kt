package org.example.recipesphere.ui.recipes.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import org.example.recipesphere.domain.model.Recipe
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent

@Composable
fun RecipeCard(
    recipe: Recipe,
    onClick: (Recipe) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        onClick = { onClick(recipe) },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        // Top image (or loading/error)
        val shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
        SubcomposeAsyncImage(
            model = recipe.photoUrl, // can be null; Coil will show error slot
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(shape)
        ) {
            when (painter.state.value) {
                is coil3.compose.AsyncImagePainter.State.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is coil3.compose.AsyncImagePainter.State.Error -> {
                    // simple placeholder
                    Box(Modifier.fillMaxSize())
                }
                else -> SubcomposeAsyncImageContent()
            }
        }

        Column(Modifier.padding(12.dp)) {
            Text(recipe.title, style = MaterialTheme.typography.titleMedium, maxLines = 1)
            Text(
                "${recipe.timeMinutes} min • ${recipe.location ?: "Unknown"}",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1
            )
        }
    }
}
