package org.example.recipesphere.ui.recipes.list

import org.example.recipesphere.domain.model.Recipe
import org.example.recipesphere.util.nowEpochMs

internal object FakeRecipes {
    fun sampleList(): List<Recipe> {
        val now = nowEpochMs()
        return listOf(
            Recipe(
                id = "1",
                title = "Shakshuka",
                photoUrl = "https://picsum.photos/seed/shakshuka/1200/675",
                timeMinutes = 25,
                instructions = "Sauté onions & peppers, add tomatoes, crack eggs, cover.",
                location = "Tel Aviv, IL",
                authorId = "mock",
                createdAtEpochMs = now - 86_400_000L
            ),
            Recipe(
                id = "2",
                title = "Hummus",
                photoUrl = "https://picsum.photos/seed/hummus/1200/675",
                timeMinutes = 10,
                instructions = "Blend chickpeas, tahini, lemon, garlic, salt.",
                location = "Haifa, IL",
                authorId = "mock",
                createdAtEpochMs = now - 43_200_000L
            ),
            Recipe(
                id = "3",
                title = "Pasta al Limone",
                photoUrl = "https://picsum.photos/seed/limone/1200/675",
                timeMinutes = 18,
                instructions = "Butter, lemon zest, cream, parmesan; toss with pasta.",
                location = "Rome, IT",
                authorId = "mock",
                createdAtEpochMs = now - 3_600_000L
            ),
            Recipe(
                id = "4",
                title = "Chicken Shawarma",
                photoUrl = "https://picsum.photos/seed/shawarma/1200/675",
                timeMinutes = 40,
                instructions = "Marinate, roast, slice; serve with pita & tahini.",
                location = "Jerusalem, IL",
                authorId = "mock",
                createdAtEpochMs = now
            )
        )
    }
}
