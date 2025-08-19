package org.example.recipesphere.data.remote

import org.example.recipesphere.domain.model.Recipe
import org.example.recipesphere.ui.recipes.list.FakeRecipes

class RecipesRemoteDataSourceFake : RecipesRemoteDataSource {
    private val data = FakeRecipes.sampleList().toMutableList()

    override suspend fun fetchAll(): List<Recipe> = data.toList()

    override suspend fun fetchById(id: String): Recipe? = data.firstOrNull { it.id == id }

    override suspend fun upsert(recipe: Recipe) {
        data.removeAll { it.id == recipe.id }
        data.add(recipe)
    }

    override suspend fun delete(id: String) {
        data.removeAll { it.id == id }
    }
}
