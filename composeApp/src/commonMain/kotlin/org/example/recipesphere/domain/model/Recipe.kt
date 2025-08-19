package org.example.recipesphere.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Recipe(
    val id: String,
    val title: String,
    val photoUrl: String? = null,
    val timeMinutes: Int,
    val instructions: String,
    val location: String? = null,
    val authorId: String,
    val createdAtEpochMs: Long
)
