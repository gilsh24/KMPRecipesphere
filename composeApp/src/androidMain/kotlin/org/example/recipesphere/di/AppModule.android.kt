package org.example.recipesphere.di

import android.app.Application
import app.cash.sqldelight.db.SqlDriver
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.example.recipesphere.data.dao.DatabaseDriverFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.dsl.KoinAppDeclaration

actual val platformModule: Module = module {
    // Http engine for Android
    single<HttpClientEngine> { OkHttp.create() }

    // SQLDelight driver (needs Android Context)
    single<SqlDriver> { DatabaseDriverFactory(get()).createDriver() }
}

// Helper to pass Application into startKoin from MainActivity
fun platformKoinConfig(app: Application): KoinAppDeclaration = {
    androidContext(app)
}
