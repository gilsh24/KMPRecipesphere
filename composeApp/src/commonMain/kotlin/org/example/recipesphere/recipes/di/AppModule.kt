package org.example.recipesphere.recipes.di

import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

object KoinStarter {
    private val coreModule = module {
        // put simple shared singletons here later (e.g., Logger)
    }

    fun start() {
        // idempotent-ish start for demo
        runCatching { stopKoin() }
        startKoin {
            modules(coreModule)
        }
    }
}
