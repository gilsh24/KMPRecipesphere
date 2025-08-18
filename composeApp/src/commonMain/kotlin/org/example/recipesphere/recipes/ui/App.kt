package org.example.recipesphere.recipes.ui

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import org.example.recipesphere.recipes.ui.screens.splash.SplashScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val appTitle = remember { "Recipes CMP" }
    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text(appTitle) })
            }
        ) { innerPadding ->
            // For Stage A, show Splash only. Navigation comes later.
            SplashScreen(innerPadding = innerPadding)
        }
    }
}