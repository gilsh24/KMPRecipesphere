package org.example.recipesphere.ui.recipes.list

import org.example.recipesphere.domain.model.Recipe

sealed class RecipesListState {
    data object Loading : RecipesListState()
    data class Loaded(val recipes: List<Recipe>) : RecipesListState()
    data class Error(val errorMessage: String) : RecipesListState()
}

// (If you want UI events later, add a sealed class RecipesListEvent here)
