package org.example.recipesphere.ui.recipes.list

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.example.recipesphere.domain.repository.RecipeRepository
import org.example.recipesphere.util.AppResult

class RecipesListViewModel(
    private val repo: RecipeRepository
) {
    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    private val _uiState = MutableStateFlow<RecipesListState>(RecipesListState.Loading)
    val uiState: StateFlow<RecipesListState> = _uiState

    init { load() }

    private fun load(force: Boolean = false) {
        scope.launch {
            _uiState.emit(RecipesListState.Loading)
            delay(250)
            when (val res = repo.listRecipes(force)) {
                is AppResult.Ok  -> _uiState.emit(RecipesListState.Loaded(res.value))
                is AppResult.Err -> _uiState.emit(RecipesListState.Error(res.error.message ?: "Unknown error"))
            }
        }
    }

    fun onRefresh() = load(force = true)
    fun onCleared() { job.cancel() }
}
