package org.example.recipesphere.ui.components

import androidx.compose.runtime.Composable

@Composable
expect fun LocationPickerButton(
    enabled: Boolean,
    onPicked: (label: String?, lat: Double?, lon: Double?) -> Unit
)
