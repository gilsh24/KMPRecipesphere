package org.example.recipesphere.data.remote

import org.example.recipesphere.domain.model.Recipe

interface RecipesRemoteDataSource {
    suspend fun fetchAll(): List<Recipe>
    suspend fun fetchById(id: String): Recipe?
}