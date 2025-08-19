package org.example.recipesphere.domain.usecase.auth

import org.example.recipesphere.domain.repository.AuthRepository

class LogoutUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke() = authRepository.signOut()
}
