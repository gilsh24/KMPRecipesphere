package org.example.recipesphere.data.repository

import org.example.recipesphere.domain.model.Recipe
import org.example.recipesphere.domain.repository.RecipeRepository
import org.example.recipesphere.ui.recipes.list.FakeRecipes
import org.example.recipesphere.util.AppResult

class FakeRecipeRepository : RecipeRepository {

    private val data: MutableList<Recipe> = FakeRecipes.sampleList().toMutableList()

    override suspend fun listRecipes(forceRefresh: Boolean): AppResult<List<Recipe>> {
        // ignore forceRefresh in fake
        return AppResult.Ok(data.sortedByDescending { it.createdAtEpochMs })
    }

    override suspend fun getRecipe(id: String, forceRefresh: Boolean): AppResult<Recipe> {
        val found = data.firstOrNull { it.id == id }
        return if (found != null) AppResult.Ok(found) else AppResult.Err(NoSuchElementException("Recipe $id"))
    }

    override suspend fun createRecipe(recipe: Recipe): AppResult<Recipe> {
        data.removeAll { it.id == recipe.id }
        data.add(recipe)
        return AppResult.Ok(recipe)
    }

    override suspend fun updateRecipe(recipe: Recipe): AppResult<Recipe> = createRecipe(recipe)

    override suspend fun deleteRecipe(id: String): AppResult<Unit> {
        val removed = data.removeAll { it.id == id }
        return if (removed) AppResult.Ok(Unit) else AppResult.Err(NoSuchElementException("Recipe $id"))
    }
}
