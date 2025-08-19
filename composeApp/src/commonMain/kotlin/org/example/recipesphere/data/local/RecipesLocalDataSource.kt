package org.example.recipesphere.data.local
import org.example.recipesphere.domain.model.Recipe

// Import your generated SQLDelight types.
// If your database class/package differs, adjust these two imports:
import org.example.recipesphere.AppDatabase
import org.example.recipesphere.RecipesQueries  // generated interface
import org.example.recipesphere.Recipes        // generated row type for 'recipes' table

class RecipesLocalDataSource(
    private val queries: RecipesQueries
) {
    fun isEmpty(): Boolean =
        queries.selectAll().executeAsList().isEmpty()

    fun getAll(): List<Recipe> =
        queries.selectAll().executeAsList().map { it.toDomain() }

    fun getById(id: String): Recipe? =
        queries.selectById(id).executeAsOneOrNull()?.toDomain()

    fun upsertAll(recipes: List<Recipe>) {
        queries.transaction {
            recipes.forEach { r ->
                queries.insertOrReplace(
                    id = r.id,
                    title = r.title,
                    photoUrl = r.photoUrl,
                    timeMinutes = r.timeMinutes.toLong(), // INTEGER -> Long
                    instructions = r.instructions,
                    location = r.location,
                    authorId = r.authorId,
                    createdAtEpochMs = r.createdAtEpochMs
                )
            }
        }
    }

    fun replaceAll(recipes: List<Recipe>) {
        queries.transaction {
            queries.deleteAll()
            upsertAll(recipes)
        }
    }
}

// --- mappers ---
private fun Recipes.toDomain(): Recipe =
    Recipe(
        id = id,
        title = title,
        photoUrl = photoUrl,
        timeMinutes = timeMinutes.toInt(),
        instructions = instructions,
        location = location,
        authorId = authorId,
        createdAtEpochMs = createdAtEpochMs
    )
