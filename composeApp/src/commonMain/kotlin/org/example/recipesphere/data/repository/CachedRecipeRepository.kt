package org.example.recipesphere.data.repository

import org.example.recipesphere.domain.model.Recipe
import org.example.recipesphere.domain.repository.RecipeRepository
import org.example.recipesphere.data.local.RecipesLocalDataSource
import org.example.recipesphere.data.remote.RecipesRemoteDataSourceFake
import org.example.recipesphere.util.AppResult

class CachedRecipeRepository(
    private val local: RecipesLocalDataSource,
    private val remote: RecipesRemoteDataSourceFake
) : RecipeRepository {

    override suspend fun listRecipes(forceRefresh: Boolean): AppResult<List<Recipe>> = try {
        if (forceRefresh || local.isEmpty()) {
            val fresh = remote.fetchAll()
            local.replaceAll(fresh)
        }
        AppResult.Ok(local.getAll())
    } catch (t: Throwable) {
        // fall back to whatever local has (if any)
        val cached = local.getAll()
        if (cached.isNotEmpty()) AppResult.Ok(cached) else AppResult.Err(t)
    }

    override suspend fun getRecipe(id: String, forceRefresh: Boolean): AppResult<Recipe> {
        return try {
            val cached = local.getById(id)
            if (cached != null && !forceRefresh) return AppResult.Ok(cached)

            val fromRemote = remote.fetchById(id)
            if (fromRemote != null) {
                local.upsertAll(listOf(fromRemote))
                AppResult.Ok(local.getById(id) ?: fromRemote)
            } else {
                cached?.let { AppResult.Ok(it) } ?: AppResult.Err(NoSuchElementException("Recipe $id"))
            }
        } catch (t: Throwable) {
            val cached = local.getById(id)
            cached?.let { AppResult.Ok(it) } ?: AppResult.Err(t)
        }
    }

    override suspend fun createRecipe(recipe: Recipe): AppResult<Recipe> {
        local.upsertAll(listOf(recipe))
        return AppResult.Ok(recipe)
    }

    override suspend fun updateRecipe(recipe: Recipe): AppResult<Recipe> = createRecipe(recipe)

    override suspend fun deleteRecipe(id: String): AppResult<Unit> {
        // Add a delete query if you want strict behavior; for now use replaceAll(getAll()-removed)
        val current = local.getAll().toMutableList()
        val removed = current.removeAll { it.id == id }
        if (!removed) return AppResult.Err(NoSuchElementException("Recipe $id"))
        local.replaceAll(current)
        return AppResult.Ok(Unit)
    }
}
