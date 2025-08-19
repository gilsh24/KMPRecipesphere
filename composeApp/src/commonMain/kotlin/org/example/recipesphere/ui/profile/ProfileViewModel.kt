package org.example.recipesphere.ui.profile

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.example.recipesphere.domain.repository.AuthRepository
import org.example.recipesphere.util.AppResult

data class ProfileUiState(
    val email: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val loggedOut: Boolean = false
)

class ProfileViewModel(
    private val auth: AuthRepository
) {
    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    private val _ui = MutableStateFlow(
        ProfileUiState(email = auth.currentUser()?.email)
    )
    val ui: StateFlow<ProfileUiState> = _ui

    init {
        // keep email in sync with auth state
        scope.launch {
            auth.authState.collectLatest { user ->
                _ui.value = _ui.value.copy(email = user?.email)
            }
        }
    }

    fun signOut() {
        if (_ui.value.isLoading) return
        scope.launch {
            _ui.value = _ui.value.copy(isLoading = true, error = null)
            when (val res = auth.signOut()) {
                is AppResult.Ok  -> _ui.value = _ui.value.copy(isLoading = false, loggedOut = true)
                is AppResult.Err -> _ui.value = _ui.value.copy(isLoading = false, error = res.error.message ?: "Failed to sign out")
            }
        }
    }

    fun consumeLoggedOut() {
        if (_ui.value.loggedOut) _ui.value = _ui.value.copy(loggedOut = false)
    }

    fun onCleared() { job.cancel() }
}
