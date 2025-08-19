package org.example.recipesphere.ui.components

import androidx.compose.runtime.Composable

@Composable
expect fun ImagePickerButton(
    enabled: Boolean,
    onPick: (ByteArray) -> Unit
)
