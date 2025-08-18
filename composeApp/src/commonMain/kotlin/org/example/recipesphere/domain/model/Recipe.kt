package org.example.recipesphere.domain.model

data class Recipe(
    val id: String,
    val title: String,
    val photoUrl: String?,
    val timeMinutes: Int,
    val instructions: String,
    val location: String?,
    val authorId: String,
    val createdAtEpochMs: Long
)
