package org.example.recipesphere.ui.recipes.detail

import org.example.recipesphere.domain.model.Recipe

sealed class RecipeDetailState {
    data object Loading : RecipeDetailState()
    data class Loaded(val recipe: Recipe) : RecipeDetailState()
    data class Error(val errorMessage: String) : RecipeDetailState()
}
