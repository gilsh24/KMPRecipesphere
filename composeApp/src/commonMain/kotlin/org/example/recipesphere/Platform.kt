package org.example.recipesphere

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform