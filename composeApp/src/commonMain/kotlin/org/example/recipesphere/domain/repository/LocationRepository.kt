package org.example.recipesphere.domain.repository

import org.example.recipesphere.domain.model.GeoPoint

interface LocationRepository {
    suspend fun lastKnownLocation(): GeoPoint?
}
