package org.example.recipesphere.ui.recipes.detail

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Simple, self-contained VM (mock-only).
 * Later we can swap this to your BaseViewModel + real use-cases.
 */
class RecipeDetailViewModel(private val recipeId: String) {

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    private val _uiState = MutableStateFlow<RecipeDetailState>(RecipeDetailState.Loading)
    val uiState: StateFlow<RecipeDetailState> = _uiState

    init { load() }

    private fun load() {
        scope.launch {
            _uiState.emit(RecipeDetailState.Loading)
            delay(400) // simulate network/db work
            val recipe = FakeRecipeSource.loadById(recipeId)
            if (recipe != null) {
                _uiState.emit(RecipeDetailState.Loaded(recipe))
            } else {
                _uiState.emit(RecipeDetailState.Error("Recipe not found"))
            }
        }
    }

    fun onRetry() = load()
    fun onCleared() { job.cancel() }
}
