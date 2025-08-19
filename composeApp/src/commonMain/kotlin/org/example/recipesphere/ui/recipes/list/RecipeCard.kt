package org.example.recipesphere.ui.recipes.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.example.recipesphere.domain.model.Recipe
import org.example.recipesphere.ui.components.Poster

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
        val shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
        Poster(
            url = recipe.photoUrl,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(shape)
        )

        Column(Modifier.padding(12.dp)) {
            Text(
                text = recipe.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${recipe.timeMinutes} min • ${recipe.location ?: "Unknown"}",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Clip
            )
        }
    }
}
