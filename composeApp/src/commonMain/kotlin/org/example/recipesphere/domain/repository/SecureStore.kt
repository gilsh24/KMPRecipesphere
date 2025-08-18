package org.example.recipesphere.domain.repository

interface SecureStore {
    suspend fun putString(key: String, value: String?)
    suspend fun getString(key: String): String?
}
