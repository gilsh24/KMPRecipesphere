package org.example.recipesphere.domain.usecase.recipe

import org.example.recipesphere.domain.repository.RecipeRepository
import org.example.recipesphere.util.AppResult

class DeleteRecipeUseCase(private val repository: RecipeRepository) {
    suspend operator fun invoke(id: String): AppResult<Unit> = repository.deleteRecipe(id)
}
