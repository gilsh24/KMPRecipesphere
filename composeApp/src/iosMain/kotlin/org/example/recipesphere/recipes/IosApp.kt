package org.example.recipesphere.recipes

import androidx.compose.ui.window.ComposeUIViewController
import org.example.recipesphere.recipes.di.KoinStarter
import org.example.recipesphere.recipes.ui.App
import platform.UIKit.UIViewController

// Exposed to Swift as MainViewController()
fun MainViewController(): UIViewController = ComposeUIViewController { App() }

// Small helper to trigger Koin from Swift
class SharedKoinStarter {
    fun start() = KoinStarter.start()
}