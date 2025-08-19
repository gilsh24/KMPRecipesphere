package org.example.recipesphere.data.remote

import dev.gitlive.firebase.storage.Data

// Factory we can call from commonMain
expect fun storageDataOf(bytes: ByteArray): Data
