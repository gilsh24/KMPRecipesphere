package org.example.recipesphere.ui.recipes.detail

import org.example.recipesphere.domain.model.Recipe
import org.example.recipesphere.ui.recipes.list.FakeRecipes

internal object FakeRecipeSource {
    fun loadById(id: String): Recipe? = FakeRecipes
        .sampleList()
        .firstOrNull { it.id == id }
}
