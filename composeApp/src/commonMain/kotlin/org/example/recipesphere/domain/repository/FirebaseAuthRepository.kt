package org.example.recipesphere.data.auth

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.example.recipesphere.domain.model.AuthUser
import org.example.recipesphere.domain.repository.AuthRepository
import org.example.recipesphere.util.AppResult

class FirebaseAuthRepository(
    private val auth: FirebaseAuth = Firebase.auth
) : AuthRepository {

    override val authState: Flow<AuthUser?> =
        auth.authStateChanged.map { fbUser -> fbUser.toDomainOrNull() }

    override suspend fun signIn(email: String, password: String): AppResult<Unit> = try {
        auth.signInWithEmailAndPassword(email, password)
        AppResult.Ok(Unit)
    } catch (t: Throwable) { AppResult.Err(t) }

    override suspend fun signUp(email: String, password: String): AppResult<Unit> = try {
        auth.createUserWithEmailAndPassword(email, password)
        AppResult.Ok(Unit)
    } catch (t: Throwable) { AppResult.Err(t) }

    override suspend fun signOut(): AppResult<Unit> = try {
        auth.signOut(); AppResult.Ok(Unit)
    } catch (t: Throwable) { AppResult.Err(t) }

    override fun currentUser(): AuthUser? = auth.currentUser.toDomainOrNull()
}

private fun FirebaseUser?.toDomainOrNull(): AuthUser? =
    this?.uid?.let { nonNullUid -> this.email?.let { AuthUser(uid = nonNullUid, email = it) } }
