package org.example.recipesphere.ui.recipes.create

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.example.recipesphere.data.remote.PhotoUploader
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

    // location (optional)
    val locationLabel: String? = null,
    val lat: Double? = null,
    val lon: Double? = null,

    // progress/errors
    val isUploadingPhoto: Boolean = false,
    val isSubmitting: Boolean = false,
    val error: String? = null,

    // navigation
    val createdId: String? = null
) {
    val timeMinutes: Int? = timeMinutesText.toIntOrNull()
    val canSubmit: Boolean =
        !isSubmitting &&
                !isUploadingPhoto &&
                title.isNotBlank() &&
                instructions.isNotBlank() &&
                (timeMinutes ?: -1) >= 0
}

class CreateRecipeViewModel(
    private val repo: RecipeRepository,
    private val uploader: PhotoUploader
) {
    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    private val _ui = MutableStateFlow(CreateRecipeUiState())
    val ui: StateFlow<CreateRecipeUiState> = _ui

    // --- field updates ---
    fun onTitleChange(v: String)        { _ui.value = _ui.value.copy(title = v, error = null) }
    fun onPhotoUrlChange(v: String)     { _ui.value = _ui.value.copy(photoUrl = v, error = null) }
    fun onTimeMinutesChange(v: String)  { _ui.value = _ui.value.copy(timeMinutesText = v.filter { it.isDigit() }, error = null) }
    fun onInstructionsChange(v: String) { _ui.value = _ui.value.copy(instructions = v, error = null) }

    // from LocationPickerButton
    fun setLocation(label: String?, lat: Double?, lon: Double?) {
        _ui.value = _ui.value.copy(locationLabel = label, lat = lat, lon = lon)
    }

    // from ImagePickerButton (bytes from gallery/camera)
    fun onImagePicked(bytes: ByteArray, mimeType: String? = null) {
        val fileName = "${newId(prefix = "img")}.jpg"
        scope.launch {
            _ui.value = _ui.value.copy(isUploadingPhoto = true, error = null)
            runCatching {
                uploader.upload(
                    bytes = bytes,
                    fileName = fileName,
                    contentType = mimeType ?: "image/jpeg"
                )
            }.onSuccess { url ->
                _ui.value = _ui.value.copy(isUploadingPhoto = false, photoUrl = url)
            }.onFailure { t ->
                _ui.value = _ui.value.copy(isUploadingPhoto = false, error = "Upload failed: ${t.message}")
            }
        }
    }

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
            location = s.locationLabel,           // saved human-readable label
            authorId = "me",                      // TODO: replace with real UID from AuthRepository
            createdAtEpochMs = nowEpochMs()
        )

        scope.launch {
            _ui.value = _ui.value.copy(isSubmitting = true, error = null)
            when (val res = repo.createRecipe(recipe)) {
                is AppResult.Ok  -> _ui.value = _ui.value.copy(isSubmitting = false, createdId = id)
                is AppResult.Err -> _ui.value = _ui.value.copy(isSubmitting = false, error = res.error.message ?: "Failed")
            }
        }
    }

    fun consumeNavigation() {
        if (_ui.value.createdId != null) _ui.value = _ui.value.copy(createdId = null)
    }

    fun onCleared() { job.cancel() }
}
