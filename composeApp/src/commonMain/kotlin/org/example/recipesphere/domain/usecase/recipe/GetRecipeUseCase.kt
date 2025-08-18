package org.example.recipesphere.domain.usecase.recipe

import org.example.recipesphere.domain.model.Recipe
import org.example.recipesphere.domain.repository.RecipeRepository
import org.example.recipesphere.util.AppResult

class GetRecipeUseCase(private val repository: RecipeRepository) {
    suspend operator fun invoke(id: String, forceRefresh: Boolean = false): AppResult<Recipe> =
        repository.getRecipe(id, forceRefresh)
}
