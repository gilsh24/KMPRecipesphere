package org.example.recipesphere.data.repository

import org.example.recipesphere.domain.model.Recipe
import org.example.recipesphere.domain.repository.RecipeRepository
import org.example.recipesphere.data.local.RecipesLocalDataSource
import org.example.recipesphere.data.remote.RecipesRemoteDataSource
import org.example.recipesphere.util.AppResult

class CachedRecipeRepository(
    private val local: RecipesLocalDataSource,
    private val remote: RecipesRemoteDataSource
) : RecipeRepository {

    override suspend fun listRecipes(forceRefresh: Boolean): AppResult<List<Recipe>> = try {
        if (forceRefresh || local.isEmpty()) {
            val fresh = remote.fetchAll()
            local.replaceAll(fresh)
        }
        AppResult.Ok(local.getAll())
    } catch (t: Throwable) {
        val cached = local.getAll()
        if (cached.isNotEmpty()) AppResult.Ok(cached) else AppResult.Err(t)
    }

    override suspend fun getRecipe(id: String, forceRefresh: Boolean): AppResult<Recipe> {
        return try {
            val cached = local.getById(id)
            if (cached != null && !forceRefresh) return AppResult.Ok(cached)
            val remoteOne = remote.fetchById(id)
            if (remoteOne != null) {
                local.upsertAll(listOf(remoteOne))
                AppResult.Ok(local.getById(id) ?: remoteOne)
            } else cached?.let { AppResult.Ok(it) } ?: AppResult.Err(NoSuchElementException("Recipe $id"))
        } catch (t: Throwable) {
            local.getById(id)?.let { AppResult.Ok(it) } ?: AppResult.Err(t)
        }
    }

    override suspend fun createRecipe(recipe: Recipe): AppResult<Recipe> = try {
        remote.upsert(recipe)                 // write remote
        local.upsertAll(listOf(recipe))       // cache locally
        AppResult.Ok(recipe)
    } catch (t: Throwable) {
        AppResult.Err(t)
    }

    override suspend fun updateRecipe(recipe: Recipe): AppResult<Recipe> = createRecipe(recipe)

    override suspend fun deleteRecipe(id: String): AppResult<Unit> = try {
        remote.delete(id)
        val remain = local.getAll().filterNot { it.id == id }
        local.replaceAll(remain)
        AppResult.Ok(Unit)
    } catch (t: Throwable) {
        AppResult.Err(t)
    }
}
