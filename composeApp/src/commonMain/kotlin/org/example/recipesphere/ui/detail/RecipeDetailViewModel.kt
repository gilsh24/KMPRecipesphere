package org.example.recipesphere.ui.recipes.detail

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.example.recipesphere.domain.repository.RecipeRepository
import org.example.recipesphere.util.AppResult

class RecipeDetailViewModel(
    private val repo: RecipeRepository,
    private val recipeId: String
) {
    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    private val _uiState = MutableStateFlow<RecipeDetailState>(RecipeDetailState.Loading)
    val uiState: StateFlow<RecipeDetailState> = _uiState

    init { load() }

    private fun load() {
        scope.launch {
            _uiState.emit(RecipeDetailState.Loading)
            delay(200)
            when (val res = repo.getRecipe(recipeId)) {
                is AppResult.Ok  -> _uiState.emit(RecipeDetailState.Loaded(res.value))
                is AppResult.Err -> _uiState.emit(RecipeDetailState.Error(res.error.message ?: "Not found"))
            }
        }
    }

    fun onRetry() = load()
    fun onCleared() { job.cancel() }
}
