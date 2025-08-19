package org.example.recipesphere.data.remote

import org.example.recipesphere.domain.model.Recipe
import org.example.recipesphere.ui.recipes.list.FakeRecipes

class RecipesRemoteDataSourceFake {
    suspend fun fetchAll(): List<Recipe> = FakeRecipes.sampleList()
    suspend fun fetchById(id: String): Recipe? =
        FakeRecipes.sampleList().firstOrNull { it.id == id }
}
