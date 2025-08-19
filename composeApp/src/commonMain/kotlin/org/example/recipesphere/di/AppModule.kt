package org.example.recipesphere.di

import app.cash.sqldelight.db.SqlDriver
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.example.recipesphere.AppDatabase
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import org.example.recipesphere.data.dao.SqlDelightDatabase
import org.example.recipesphere.data.local.RecipesLocalDataSource
import org.example.recipesphere.data.remote.RecipesRemoteDataSource
import org.example.recipesphere.data.remote.RecipesRemoteDataSourceFake
import org.example.recipesphere.data.remote.RecipesRemoteDataSourceFirebase
import org.example.recipesphere.data.repository.CachedRecipeRepository
import org.example.recipesphere.data.repository.FakeRecipeRepository
import org.example.recipesphere.domain.repository.RecipeRepository
import org.koin.core.qualifier.named

// Your entry points
fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)     // e.g., androidContext(app) on Android
        modules(appModules())
    }
}

fun initKoin() = initKoin {}

// Bundle the modules
fun appModules() = listOf(commonModule, platformModule, domainModule)

// Platform-specific bindings live here:
expect val platformModule: Module

// Domain contracts-only for now (Stage B). Add factories later as needed.
val domainModule = module { /* use-cases will be wired in Stage D/E */ }

// Common, platform-agnostic singletons
val commonModule = module {
    // JSON
    singleOf(::createJson)

    // Ktor client built with a platform engine (OkHttp/Darwin supplied by platformModule)
    single { createHttpClient(get(), get()) }

    // SQLDelight database
//    single { SqlDelightDatabase(get<SqlDriver>()) }

    // Optional: expose generated queries if you like
    single { SqlDelightDatabase(get()) }

    single { AppDatabase(get()) }
    single { get<AppDatabase>().recipesQueries }
    single { Firebase.firestore }

    single { RecipesLocalDataSource(get()) }
//    single { RecipesRemoteDataSourceFake() }

    single<RecipesRemoteDataSource> { RecipesRemoteDataSourceFirebase(get()) }
    single<RecipeRepository> { CachedRecipeRepository(local = get(), remote = get()) }

//    single(named("FirebaseApiKey")) { "<PUT_YOUR_FIREBASE_WEB_API_KEY>" }
    // --- add Auth API ---
//    single { AuthApi(http = get<HttpClient>(), apiKey = get(named("FirebaseApiKey"))) }

    // --- add Auth Repository (no SecureStore) ---
//    single<AuthRepository> { AuthRepositoryImpl(authApi = get()) }
}

// Helpers
fun createJson(): Json = Json {
    ignoreUnknownKeys = true
    prettyPrint = true
    isLenient = true
}

fun createHttpClient(engine: HttpClientEngine, json: Json) = HttpClient(engine) {
    install(Logging) {
        level = LogLevel.ALL
        logger = Logger.DEFAULT
    }
    install(ContentNegotiation) { json(json) }
}
