package org.example.recipesphere.ui.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.example.recipesphere.recipes.ui.screens.splash.SplashScreen
import org.example.recipesphere.ui.recipes.detail.RecipeDetailScreen
import org.example.recipesphere.ui.recipes.list.RecipesListScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavGraph() {
    val nav = rememberNavController()

    Scaffold(
        topBar = { TopAppBar(title = { Text("RecipeSphere") }) }
    ) { innerPadding ->
        NavHost(
            navController = nav,
            startDestination = RecipesListRoute // you can swap to SplashRoute later
        ) {
            composable<RecipesListRoute> {
                RecipesListScreen(
                    onRecipeClick = { recipe ->
                        nav.navigate(RecipeDetailRoute(id = recipe.id))
                    }
                )
            }
            composable<RecipeDetailRoute> { backStackEntry ->
                val args = backStackEntry.toRoute<RecipeDetailRoute>() // type-safe args
                RecipeDetailScreen(
                    recipeId = args.id,
                    onBack = { nav.popBackStack() }
                )
            }
            // You can wire these when ready:
            composable<SplashRoute> { SplashScreen(innerPadding) }
            composable<LoginRoute> { /* LoginScreen(...) */ }
            composable<ProfileRoute> { /* ProfileScreen(...) */ }
        }
    }
}
