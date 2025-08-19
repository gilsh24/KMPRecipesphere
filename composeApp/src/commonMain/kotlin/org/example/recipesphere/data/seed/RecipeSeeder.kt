package org.example.recipesphere.data.seed

import dev.gitlive.firebase.firestore.FirebaseFirestore
import org.example.recipesphere.domain.model.Recipe
import org.example.recipesphere.util.nowEpochMs

class RecipeSeeder(
    private val db: FirebaseFirestore
) {
    private val collection = db.collection("recipes")

    /**
     * Writes demo recipes. Returns how many were seeded.
     */
    suspend fun seed(): Int {
        val now = nowEpochMs()
        val recipes = listOf(
            Recipe(
                id = "seed-1",
                title = "Shakshuka",
                photoUrl = "https://picsum.photos/seed/shakshuka/1200/675",
                timeMinutes = 25,
                instructions = "Sauté onions & peppers, add tomatoes, crack eggs, cover.",
                location = "Tel Aviv, IL",
                authorId = "seed",
                createdAtEpochMs = now - 86_400_000L
            ),
            Recipe(
                id = "seed-2",
                title = "Hummus",
                photoUrl = "https://picsum.photos/seed/hummus/1200/675",
                timeMinutes = 10,
                instructions = "Blend chickpeas, tahini, lemon, garlic, salt.",
                location = "Haifa, IL",
                authorId = "seed",
                createdAtEpochMs = now - 43_200_000L
            ),
            Recipe(
                id = "seed-3",
                title = "Pasta al Limone",
                photoUrl = "https://picsum.photos/seed/limone/1200/675",
                timeMinutes = 18,
                instructions = "Butter, lemon zest, cream, parmesan; toss with pasta.",
                location = "Rome, IT",
                authorId = "seed",
                createdAtEpochMs = now - 3_600_000L
            ),
            Recipe(
                id = "seed-4",
                title = "Chicken Shawarma",
                photoUrl = "https://picsum.photos/seed/shawarma/1200/675",
                timeMinutes = 40,
                instructions = "Marinate, roast, slice; serve with pita & tahini.",
                location = "Jerusalem, IL",
                authorId = "seed",
                createdAtEpochMs = now
            )
        )

        // Upsert each by its id
        for (r in recipes) {
            collection.document(r.id).set(r)
        }
        return recipes.size
    }
}
