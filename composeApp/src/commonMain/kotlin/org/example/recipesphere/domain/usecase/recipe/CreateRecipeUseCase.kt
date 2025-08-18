package org.example.recipesphere.domain.usecase.recipe

import org.example.recipesphere.domain.model.Recipe
import org.example.recipesphere.domain.repository.RecipeRepository
import org.example.recipesphere.util.AppResult

class CreateRecipeUseCase(private val repository: RecipeRepository) {
    suspend operator fun invoke(recipe: Recipe): AppResult<Recipe> = repository.createRecipe(recipe)
}
