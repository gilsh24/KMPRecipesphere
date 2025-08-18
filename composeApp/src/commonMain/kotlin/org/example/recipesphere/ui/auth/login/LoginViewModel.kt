package org.example.recipesphere.ui.auth.login

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false
) {
    val canSubmit: Boolean =
        !isLoading &&
                email.isNotBlank() &&
                password.isNotBlank() &&
                emailError == null &&
                passwordError == null
}

class LoginViewModel {
    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    private val _ui = MutableStateFlow(LoginUiState())
    val ui: StateFlow<LoginUiState> = _ui

    fun onEmailChange(value: String) {
        val error = validateEmail(value)
        _ui.value = _ui.value.copy(email = value, emailError = error)
    }

    fun onPasswordChange(value: String) {
        val error = validatePassword(value)
        _ui.value = _ui.value.copy(password = value, passwordError = error)
    }

    fun submit() {
        val current = _ui.value
        if (!current.canSubmit) return
        scope.launch {
            _ui.value = _ui.value.copy(isLoading = true)
            delay(600) // pretend to call server
            // Mock "success" if validation passed
            _ui.value = _ui.value.copy(isLoading = false, isLoggedIn = true)
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
}
