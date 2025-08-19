package org.example.recipesphere.ui.auth.login

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import org.example.recipesphere.domain.repository.AuthRepository
import org.example.recipesphere.util.AppResult

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val errorMessage: String? = null
) {
    val canSubmit: Boolean =
        !isLoading &&
                email.isNotBlank() &&
                password.isNotBlank() &&
                emailError == null &&
                passwordError == null
}

class LoginViewModel(
    private val auth: AuthRepository
) {
    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    private val _ui = MutableStateFlow(LoginUiState())
    val ui: StateFlow<LoginUiState> = _ui

    init {
        // If already signed in, navigate immediately
        auth.currentUser()?.let { _ui.value = _ui.value.copy(isLoggedIn = true) }

        // Optional: react to future auth changes
        scope.launch {
            auth.authState.collectLatest { user ->
                _ui.value = _ui.value.copy(isLoggedIn = user != null)
            }
        }
    }

    fun onEmailChange(value: String) {
        val error = validateEmail(value)
        _ui.value = _ui.value.copy(email = value, emailError = error, errorMessage = null)
    }

    fun onPasswordChange(value: String) {
        val error = validatePassword(value)
        _ui.value = _ui.value.copy(password = value, passwordError = error, errorMessage = null)
    }

    fun signIn() {
        val s = _ui.value
        if (!s.canSubmit) return
        scope.launch {
            _ui.value = s.copy(isLoading = true, errorMessage = null)
            when (val res = auth.signIn(s.email.trim(), s.password)) {
                is AppResult.Ok  -> _ui.value = _ui.value.copy(isLoading = false, isLoggedIn = true)
                is AppResult.Err -> _ui.value = _ui.value.copy(isLoading = false, errorMessage = mapError(res.error))
            }
        }
    }

    fun signUp() {
        val s = _ui.value
        if (!s.canSubmit) return
        scope.launch {
            _ui.value = s.copy(isLoading = true, errorMessage = null)
            when (val res = auth.signUp(s.email.trim(), s.password)) {
                is AppResult.Ok  -> _ui.value = _ui.value.copy(isLoading = false, isLoggedIn = true)
                is AppResult.Err -> _ui.value = _ui.value.copy(isLoading = false, errorMessage = mapError(res.error))
            }
        }
    }

    fun acknowledgeNavigation() {
        if (_ui.value.isLoggedIn) _ui.value = _ui.value.copy(isLoggedIn = false)
    }

    fun onCleared() { job.cancel() }

    private fun validateEmail(email: String): String? =
        if ('@' in email && '.' in email.substringAfter('@')) null else "Invalid email"

    private fun validatePassword(pw: String): String? =
        if (pw.length >= 6) null else "Min 6 characters"

    private fun mapError(t: Throwable): String =
        t.message ?: "Authentication failed"
}
