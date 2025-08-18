package org.example.recipesphere.recipes.ui

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import org.example.recipesphere.recipes.ui.screens.splash.SplashScreen
import org.example.recipesphere.ui.recipes.list.RecipesListScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val appTitle = remember { "Recipespere" }
    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text(appTitle) })
            }
        ) {
            RecipesListScreen()
        }
    }
}