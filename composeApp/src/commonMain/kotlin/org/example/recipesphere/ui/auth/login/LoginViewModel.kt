package org.example.recipesphere.ui.auth.login

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
        auth.currentUser()?.let { _ui.value = _ui.value.copy(isLoggedIn = true) }
    }

    fun onEmailChange(value: String) {
        _ui.value = _ui.value.copy(
            email = value,
            emailError = validateEmail(value),
            errorMessage = null
        )
    }

    fun onPasswordChange(value: String) {
        _ui.value = _ui.value.copy(
            password = value,
            passwordError = validatePassword(value),
            errorMessage = null
        )
    }

    fun signIn() {
        val s = _ui.value
        if (!s.canSubmit) return
        scope.launch {
            _ui.value = s.copy(isLoading = true, errorMessage = null)
            when (val res = auth.signIn(s.email.trim(), s.password)) {
                is AppResult.Ok  -> _ui.value = _ui.value.copy(isLoading = false, isLoggedIn = true)
                is AppResult.Err -> _ui.value = _ui.value.copy(isLoading = false, errorMessage = mapSignInError(res.error))
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
                is AppResult.Err -> _ui.value = _ui.value.copy(isLoading = false, errorMessage = mapSignUpError(res.error))
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

    private fun mapSignInError(t: Throwable): String {
        val m = t.message?.lowercase().orEmpty()
        return when {
            "no user record" in m || "user-not-found" in m      -> "No account found for this email."
            "password is invalid" in m || "wrong-password" in m -> "Incorrect password."
            "badly formatted" in m || "invalid-email" in m      -> "Invalid email."
            "too many requests" in m                            -> "Too many attempts, try later."
            else                                                -> "Sign-in failed: ${t.message}"
        }
    }

    private fun mapSignUpError(t: Throwable): String {
        val m = t.message?.lowercase().orEmpty()
        return when {
            "already in use" in m || "email-already-in-use" in m -> "Email already in use."
            "weak-password" in m                                 -> "Password is too weak."
            "invalid-email" in m                                 -> "Invalid email."
            else                                                 -> "Sign-up failed: ${t.message}"
        }
    }
}
