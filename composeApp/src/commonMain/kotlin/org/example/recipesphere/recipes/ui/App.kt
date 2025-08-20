package org.example.recipesphere.recipes.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.example.recipesphere.ui.navigation.AppNavGraph

@Composable
fun App() {
    val appTitle = remember { "Recipespere" }

    MaterialTheme {
        AppNavGraph()
    }

}