package org.example.recipesphere.ui.recipes.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.recipesphere.domain.model.Recipe
import org.example.recipesphere.ui.components.RecipeCard
import org.example.recipesphere.ui.components.SearchField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipesListScreen(
    viewModel: RecipesListViewModel = remember { RecipesListViewModel() },
    onRecipeClick: (Recipe) -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()
    var query by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recipes") },
                actions = {
//                    IconButton(onClick = { viewModel.onRefresh() }) {
//                        Icon(
//                            imageVector = androidx.compose.material.icons.Icons.Default.Refresh,
//                            contentDescription = "Refresh"
//                        )
//                    }
                }
            )
        }
    ) { innerPadding ->
        when (val s = state) {
            is RecipesListState.Error -> ErrorContent(
                message = s.errorMessage,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
            is RecipesListState.Loaded -> {
                val filtered = remember(query, s.recipes) {
                    val q = query.trim().lowercase()
                    if (q.isEmpty()) s.recipes
                    else s.recipes.filter { r ->
                        r.title.lowercase().contains(q) ||
                                (r.location ?: "").lowercase().contains(q) ||
                                r.instructions.lowercase().contains(q)
                    }
                }
                LoadedContent(
                    recipes = filtered,
                    query = query,
                    onQueryChange = { query = it },
                    onRecipeClick = onRecipeClick,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }
            RecipesListState.Loading -> LoadingContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        }
    }
}

@Composable
private fun LoadedContent(
    recipes: List<Recipe>,
    query: String,
    onQueryChange: (String) -> Unit,
    onRecipeClick: (Recipe) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(8.dp)) {
        SearchField(query = query, onQueryChange = onQueryChange)
        Spacer(Modifier.height(8.dp))

        if (recipes.isEmpty()) {
            EmptyContent()
        } else {
            val gridState = rememberLazyGridState()
            LazyVerticalGrid(
                state = gridState,
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(recipes, key = { it.id }) { recipe ->
                    RecipeCard(recipe = recipe, onClick = onRecipeClick)
                }
            }
        }
    }
}

@Composable
private fun LoadingContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorContent(message: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
private fun EmptyContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No recipes match your search.",
            style = MaterialTheme.typography.titleMedium
        )
    }
}
