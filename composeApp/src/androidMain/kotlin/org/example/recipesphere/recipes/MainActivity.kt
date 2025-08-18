package org.example.recipesphere.recipes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import org.example.recipesphere.recipes.di.KoinStarter
import org.example.recipesphere.recipes.ui.App

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        KoinStarter.start()
        setContent { App() }
    }
}