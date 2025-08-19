package org.example.recipesphere.ui.profile

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.example.recipesphere.data.seed.RecipeSeeder

data class ProfileUiState(
    val isSeeding: Boolean = false,
    val message: String? = null
)

class ProfileViewModel(
    private val seeder: RecipeSeeder
) {
    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    private val _ui = MutableStateFlow(ProfileUiState())
    val ui: StateFlow<ProfileUiState> = _ui

    fun seed() {
        if (_ui.value.isSeeding) return
        scope.launch {
            _ui.value = _ui.value.copy(isSeeding = true, message = null)
            runCatching { seeder.seed() }
                .onSuccess { count ->
                    _ui.value = ProfileUiState(isSeeding = false, message = "Seeded $count recipes. Go refresh the list.")
                }
                .onFailure { t ->
                    _ui.value = ProfileUiState(isSeeding = false, message = "Seeding failed: ${t.message}")
                }
        }
    }

    fun onCleared() { job.cancel() }
    fun clearMessage() { _ui.value = _ui.value.copy(message = null) }
}
