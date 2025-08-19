package org.example.recipesphere.domain.usecase.auth

import org.example.recipesphere.domain.repository.AuthRepository
import org.example.recipesphere.util.AppResult

class LoginUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String) =
        authRepository.signIn(email, password)
}
