package org.example.recipesphere.ui.recipes.create

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.example.recipesphere.domain.model.Recipe
import org.example.recipesphere.domain.repository.RecipeRepository
import org.example.recipesphere.util.AppResult
import org.example.recipesphere.util.newId
import org.example.recipesphere.util.nowEpochMs

data class CreateRecipeUiState(
    val title: String = "",
    val photoUrl: String = "",
    val timeMinutesText: String = "",
    val instructions: String = "",
    val isSubmitting: Boolean = false,
    val error: String? = null,
    val createdId: String? = null
) {
    val timeMinutes: Int? = timeMinutesText.toIntOrNull()
    val canSubmit: Boolean =
        !isSubmitting &&
                title.isNotBlank() &&
                instructions.isNotBlank() &&
                (timeMinutes ?: -1) >= 0
}

class CreateRecipeViewModel(
    private val repo: RecipeRepository
) {
    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    private val _ui = MutableStateFlow(CreateRecipeUiState())
    val ui: StateFlow<CreateRecipeUiState> = _ui

    fun onTitleChange(v: String)        { _ui.value = _ui.value.copy(title = v,  error = null) }
    fun onPhotoUrlChange(v: String)     { _ui.value = _ui.value.copy(photoUrl = v, error = null) }
    fun onTimeMinutesChange(v: String)  { _ui.value = _ui.value.copy(timeMinutesText = v.filter { it.isDigit() }, error = null) }
    fun onInstructionsChange(v: String) { _ui.value = _ui.value.copy(instructions = v, error = null) }

    fun submit() {
        val s = _ui.value
        if (!s.canSubmit) return
        val id = newId()
        val recipe = Recipe(
            id = id,
            title = s.title.trim(),
            photoUrl = s.photoUrl.takeIf { it.isNotBlank() },
            timeMinutes = s.timeMinutes ?: 0,
            instructions = s.instructions.trim(),
            location = null,           // added later (device API)
            authorId = "me",           // swap to real user when auth is ready
            createdAtEpochMs = nowEpochMs()
        )
        scope.launch {
            _ui.value = _ui.value.copy(isSubmitting = true, error = null)
            when (val res = repo.createRecipe(recipe)) {
                is AppResult.Err -> _ui.value = _ui.value.copy(isSubmitting = false, error = res.error.message ?: "Failed")
                is AppResult.Ok  -> _ui.value = _ui.value.copy(isSubmitting = false, createdId = id)
            }
        }
    }

    fun consumeNavigation() {
        if (_ui.value.createdId != null) _ui.value = _ui.value.copy(createdId = null)
    }

    fun onCleared() { job.cancel() }
}
