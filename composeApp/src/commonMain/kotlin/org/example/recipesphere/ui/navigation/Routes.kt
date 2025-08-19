package org.example.recipesphere.ui.navigation

import kotlinx.serialization.Serializable

@Serializable object SplashRoute
@Serializable object LoginRoute
@Serializable object RecipesListRoute
@Serializable data class RecipeDetailRoute(val id: String)
@Serializable object ProfileRoute
@Serializable object CreateRecipeRoute      // <-- add this
