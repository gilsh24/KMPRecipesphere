package org.example.recipesphere.data.remote

interface PhotoUploader {
    /**
     * Uploads raw bytes and returns a public download URL.
     * @param fileName suggested name (e.g. "<id>.jpg")
     * @param contentType e.g. "image/jpeg" / "image/png"
     */
    suspend fun upload(bytes: ByteArray, fileName: String, contentType: String = "image/jpeg"): String
}
