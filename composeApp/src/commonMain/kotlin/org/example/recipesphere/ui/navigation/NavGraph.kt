package org.example.recipesphere.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.example.recipesphere.recipes.ui.screens.splash.SplashScreen
import org.example.recipesphere.ui.auth.login.LoginScreen
import org.example.recipesphere.ui.profile.ProfileScreen
import org.example.recipesphere.ui.detail.RecipeDetailScreen
import org.example.recipesphere.ui.recipes.create.CreateRecipeScreen
import org.example.recipesphere.ui.recipes.list.RecipesListScreen

@Composable
fun AppNavGraph() {
    val nav = rememberNavController()
    val backStack by nav.currentBackStackEntryAsState()
    val route = backStack?.destination?.route

    val showBottomBar = remember(route) {
        val r = route.orEmpty()
        r.startsWith(RecipesListRoute::class.qualifiedName ?: "") ||
                r.startsWith(ProfileRoute::class.qualifiedName ?: "")
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    val onRecipes = {
                        nav.navigate(RecipesListRoute) {
                            popUpTo(nav.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                    val onProfile = {
                        nav.navigate(ProfileRoute) {
                            popUpTo(nav.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }

                    val selectedRecipes = route?.startsWith(RecipesListRoute::class.qualifiedName ?: "") == true
                    val selectedProfile = route?.startsWith(ProfileRoute::class.qualifiedName ?: "") == true

                    NavigationBarItem(
                        selected = selectedRecipes,
                        onClick = onRecipes,
                        icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Recipes") },
                        label = { Text("Recipes") }
                    )
                    NavigationBarItem(
                        selected = selectedProfile,
                        onClick = onProfile,
                        icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
                        label = { Text("Profile") }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = nav,
            startDestination = LoginRoute,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<LoginRoute> {
                LoginScreen(
                    onLoginSuccess = {
                        nav.navigate(RecipesListRoute) {
                            popUpTo<LoginRoute> { inclusive = true }
                        }
                    }
                )
            }
            composable<RecipesListRoute> {
                RecipesListScreen(
                    onRecipeClick = { recipe ->
                        nav.navigate(RecipeDetailRoute(id = recipe.id))
                    },
                    onCreateClick = {
                        nav.navigate(CreateRecipeRoute)     // <-- add this
                    }
                )
            }
            composable<RecipeDetailRoute> {
                val args = it.toRoute<RecipeDetailRoute>()
                RecipeDetailScreen(
                    recipeId = args.id,
                    onBack = { nav.popBackStack() }
                )
            }
            composable<CreateRecipeRoute> {
                CreateRecipeScreen(
                    onBack = { nav.popBackStack() },
                    onCreated = { id ->
                        // After creating, go to detail and clear Create from back stack
                        nav.navigate(RecipeDetailRoute(id)) {
                            popUpTo<RecipesListRoute> { inclusive = false }
                        }
                    }
                )
            }
            composable<SplashRoute> { SplashScreen(innerPadding) }
            composable<ProfileRoute> {
                ProfileScreen(
                    onLoggedOut = {
                        // mock: go back to Login and clear stack
                        nav.navigate(LoginRoute) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
