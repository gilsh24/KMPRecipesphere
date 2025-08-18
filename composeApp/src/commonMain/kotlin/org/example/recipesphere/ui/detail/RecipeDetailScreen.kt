package org.example.recipesphere.ui.recipes.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.example.recipesphere.domain.model.Recipe
import org.example.recipesphere.util.nowEpochMs
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    recipeId: String,
    onBack: () -> Unit = {},
    viewModel: RecipeDetailViewModel = remember { RecipeDetailViewModel(recipeId) }
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recipe") },
                navigationIcon = {
                    // Keep it icon-free to avoid extra deps; swap to IconButton later if you want.
                    TextButton(onClick = onBack) { Text("Back") }
                }
            )
        }
    ) { innerPadding ->
        when (val s = state) {
            is RecipeDetailState.Loading -> DetailLoading(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
            is RecipeDetailState.Error -> DetailError(
                message = s.errorMessage,
                onRetry = { viewModel.onRetry() },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
            is RecipeDetailState.Loaded -> DetailContent(
                recipe = s.recipe,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        }
    }
}

@Composable
private fun DetailContent(
    recipe: Recipe,
    modifier: Modifier = Modifier
) {
    val scroll = rememberScrollState()
    androidx.compose.foundation.layout.Column(
        modifier = modifier
            .verticalScroll(scroll)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Image placeholder (we'll plug a real image loader later)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(12.dp))
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = recipe.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(Modifier.height(8.dp))

        MetaChips(
            timeMinutes = recipe.timeMinutes,
            location = recipe.location
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Instructions",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = recipe.instructions,
            style = MaterialTheme.typography.bodyLarge,
            lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
        )

        Spacer(Modifier.height(24.dp))
        Text(
            text = "Published by: ${recipe.authorId} • ${epochToFriendly(recipe.createdAtEpochMs)}",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun MetaChips(
    timeMinutes: Int,
    location: String?
) {
    // Simple text rows—swap to Chips later if you want fancier UI
    KeyValueRow(label = "Time", value = "$timeMinutes min")
    if (!location.isNullOrBlank()) {
        Spacer(Modifier.height(4.dp))
        KeyValueRow(label = "Location", value = location)
    }
}

@Composable
private fun KeyValueRow(label: String, value: String) {
    androidx.compose.foundation.layout.Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun DetailLoading(modifier: Modifier = Modifier) {
    androidx.compose.foundation.layout.Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.material3.CircularProgressIndicator()
    }
}

@Composable
private fun DetailError(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.foundation.layout.Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.foundation.layout.Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(message, style = MaterialTheme.typography.titleMedium)
            Button(onClick = onRetry) { Text("Retry") }
        }
    }
}

// Tiny util—replace later with a proper formatter if desired
private fun epochToFriendly(epochMs: Long): String {
    val now = nowEpochMs()
    val mins = (now - epochMs) / 60000L
    return when {
        mins < 1 -> "just now"
        mins < 60 -> "$mins min ago"
        mins < 60 * 24 -> "${mins / 60} h ago"
        else -> "${mins / (60 * 24)} d ago"
    }
}
