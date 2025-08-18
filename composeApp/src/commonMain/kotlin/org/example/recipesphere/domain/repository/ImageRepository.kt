package org.example.recipesphere.domain.repository

import org.example.recipesphere.util.AppResult

interface ImageRepository {
    /**
     * @param byteArray image bytes (already compressed as JPEG/PNG/WebP)
     * @param fileName suggested file name
     * @return public URL of the uploaded image
     */
    suspend fun uploadImage(byteArray: ByteArray, fileName: String): AppResult<String>
}
