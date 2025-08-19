package org.example.recipesphere.data.remote

import dev.gitlive.firebase.storage.FirebaseStorage
import dev.gitlive.firebase.storage.storageMetadata

class FirebasePhotoUploader(
    private val storage: FirebaseStorage
) : PhotoUploader {

    override suspend fun upload(bytes: ByteArray, fileName: String, contentType: String): String {
        val ref = storage.reference("recipes/$fileName")
        val meta = storageMetadata { this.contentType = contentType }
        ref.putData(storageDataOf(bytes), meta)
        return ref.getDownloadUrl()
    }
}
