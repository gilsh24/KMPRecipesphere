package org.example.recipesphere.domain.repository

import org.example.recipesphere.domain.model.Recipe
import org.example.recipesphere.util.AppResult

interface RecipeRepository {
    suspend fun listRecipes(forceRefresh: Boolean = false): AppResult<List<Recipe>>
    suspend fun getRecipe(id: String, forceRefresh: Boolean = false): AppResult<Recipe>
    suspend fun createRecipe(recipe: Recipe): AppResult<Recipe>
    suspend fun updateRecipe(recipe: Recipe): AppResult<Recipe>
    suspend fun deleteRecipe(id: String): AppResult<Unit>
}
