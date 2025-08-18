package org.example.recipesphere.domain.usecase.recipe

import org.example.recipesphere.domain.repository.ImageRepository
import org.example.recipesphere.util.AppResult

class UploadImageUseCase(private val imageRepository: ImageRepository) {
    suspend operator fun invoke(bytes: ByteArray, fileName: String): AppResult<String> =
        imageRepository.uploadImage(bytes, fileName)
}
