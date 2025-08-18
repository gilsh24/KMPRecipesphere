package org.example.recipesphere.domain.repository

import kotlinx.coroutines.flow.Flow
import org.example.recipesphere.domain.model.User
import org.example.recipesphere.util.AppResult

interface AuthRepository {
    suspend fun login(email: String, password: String): AppResult<User>
    suspend fun logout(): AppResult<Unit>
    fun observeAuth(): Flow<User?>            // emits on login/logout/token refresh
    suspend fun currentUser(): User?
}
