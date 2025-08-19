package org.example.recipesphere.domain.repository

import kotlinx.coroutines.flow.Flow
import org.example.recipesphere.domain.model.AuthUser
import org.example.recipesphere.util.AppResult

interface AuthRepository {
    val authState: Flow<AuthUser?>        // emits on sign-in/out

    suspend fun signIn(email: String, password: String): AppResult<Unit>
    suspend fun signUp(email: String, password: String): AppResult<Unit>
    suspend fun signOut(): AppResult<Unit>

    fun currentUser(): AuthUser?
}
