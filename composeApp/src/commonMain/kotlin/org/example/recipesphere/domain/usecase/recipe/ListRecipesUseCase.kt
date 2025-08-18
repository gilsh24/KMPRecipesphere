package org.example.recipesphere.domain.usecase.recipe

import org.example.recipesphere.domain.model.Recipe
import org.example.recipesphere.domain.repository.RecipeRepository
import org.example.recipesphere.util.AppResult

class ListRecipesUseCase(private val repository: RecipeRepository) {
    suspend operator fun invoke(forceRefresh: Boolean = false): AppResult<List<Recipe>> =
        repository.listRecipes(forceRefresh)
}
