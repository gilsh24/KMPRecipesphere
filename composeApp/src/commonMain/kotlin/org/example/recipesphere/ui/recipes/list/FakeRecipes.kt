package org.example.recipesphere.ui.recipes.list

import org.example.recipesphere.domain.model.Recipe

internal object FakeRecipes {
    fun sampleList(): List<Recipe> = listOf(
        Recipe(
            id = "1",
            title = "Shakshuka",
            photoUrl = null, // mock: no image loader yet
            timeMinutes = 25,
            instructions = "Sauté onions & peppers, add tomatoes, crack eggs, cover.",
            location = "Tel Aviv, IL",
            authorId = "mock",
            createdAtEpochMs = System.currentTimeMillis() - 86_400_000L
        ),
        Recipe(
            id = "2",
            title = "Hummus",
            photoUrl = null,
            timeMinutes = 10,
            instructions = "Blend chickpeas, tahini, lemon, garlic, salt.",
            location = "Haifa, IL",
            authorId = "mock",
            createdAtEpochMs = System.currentTimeMillis() - 43_200_000L
        ),
        Recipe(
            id = "3",
            title = "Pasta al Limone",
            photoUrl = null,
            timeMinutes = 18,
            instructions = "Butter, lemon zest, cream, parmesan; toss with pasta.",
            location = "Rome, IT",
            authorId = "mock",
            createdAtEpochMs = System.currentTimeMillis() - 3_600_000L
        ),
        Recipe(
            id = "4",
            title = "Chicken Shawarma",
            photoUrl = null,
            timeMinutes = 40,
            instructions = "Marinate, roast, slice; serve with pita & tahini.",
            location = "Jerusalem, IL",
            authorId = "mock",
            createdAtEpochMs = System.currentTimeMillis()
        )
    )
}
