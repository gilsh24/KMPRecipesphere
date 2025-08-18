package org.example.recipesphere.ui.recipes.list

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Simple, self-contained VM (no expect/actual, no DI) just for mocking.
 * Later you can replace this with your BaseViewModel pattern + real use-cases.
 */
class RecipesListViewModel {

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    private val _uiState = MutableStateFlow<RecipesListState>(RecipesListState.Loading)
    val uiState: StateFlow<RecipesListState> = _uiState

    init { loadFake() }

    private fun loadFake() {
        scope.launch {
            _uiState.emit(RecipesListState.Loading)
            delay(600) // pretend to fetch
            val data = FakeRecipes.sampleList()
            _uiState.emit(RecipesListState.Loaded(data))
        }
    }

    fun onRefresh() {
        loadFake()
    }

    fun onCleared() {
        job.cancel()
    }
}
