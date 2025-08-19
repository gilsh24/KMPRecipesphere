package org.example.recipesphere.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthUser (
    val uid: String,
    val email: String
)