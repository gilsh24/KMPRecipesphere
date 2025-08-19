package org.example.recipesphere.data.remote

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.Direction
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.firestore
import org.example.recipesphere.domain.model.Recipe

class RecipesRemoteDataSourceFirebase(
    private val db: FirebaseFirestore = Firebase.firestore
) : RecipesRemoteDataSource {

    override suspend fun fetchAll(): List<Recipe> {
        val snap = db.collection("recipes")
            .orderBy("createdAtEpochMs", Direction.DESCENDING)
            .get()
        return snap.documents.mapNotNull { it.data(Recipe.serializer()) }
    }

    override suspend fun fetchById(id: String): Recipe? {
        val doc = db.collection("recipes").document(id).get()
        return doc.data(Recipe.serializer())
    }
}
