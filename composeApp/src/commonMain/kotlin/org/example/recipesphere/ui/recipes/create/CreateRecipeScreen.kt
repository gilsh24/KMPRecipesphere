package org.example.recipesphere.ui.recipes.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.remember
import org.koin.compose.koinInject
import org.example.recipesphere.domain.repository.RecipeRepository
import org.example.recipesphere.data.remote.PhotoUploader
import org.example.recipesphere.ui.components.ImagePickerButton
import org.example.recipesphere.ui.components.LocationPickerButton
import org.example.recipesphere.ui.components.Poster

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateRecipeScreen(
    onBack: () -> Unit = {},
    onCreated: (String) -> Unit = {},
    viewModel: CreateRecipeViewModel = run {
        val repo: RecipeRepository = koinInject()
        val uploader: PhotoUploader = koinInject()
        remember { CreateRecipeViewModel(repo, uploader) }
    }
) {
    val ui by viewModel.ui.collectAsState()

    LaunchedEffect(ui.createdId) {
        ui.createdId?.let { id ->
            onCreated(id)
            viewModel.consumeNavigation()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Recipe") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, contentDescription = "Back") }
                }
            )
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Image + Location actions
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ImagePickerButton(
                    enabled = !ui.isUploadingPhoto && !ui.isSubmitting
                ) { bytes ->
                    viewModel.onImagePicked(bytes)
                }
                if (ui.isUploadingPhoto) {
                    CircularProgressIndicator(modifier = Modifier.height(24.dp))
                }

                Spacer(modifier = Modifier.weight(1f))

                // Device location (Android actual)
                LocationPickerButton(
                    enabled = !ui.isUploadingPhoto && !ui.isSubmitting
                ) { label, lat, lon ->
                    viewModel.setLocation(label, lat, lon)
                }
            }

            // Show chosen photo preview
            if (ui.photoUrl.isNotBlank()) {
                Poster(
                    url = ui.photoUrl,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                )
            }

            // Show chosen location (if any)
            ui.locationLabel?.let {
                Text("Location: $it", style = MaterialTheme.typography.bodyMedium)
            }

            OutlinedTextField(
                value = ui.title,
                onValueChange = viewModel::onTitleChange,
                label = { Text("Title") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = ui.timeMinutesText,
                onValueChange = viewModel::onTimeMinutesChange,
                label = { Text("Time (minutes)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = ui.instructions,
                onValueChange = viewModel::onInstructionsChange,
                label = { Text("Instructions") },
                minLines = 5,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
            )

            if (ui.error != null) {
                Text(ui.error!!, color = MaterialTheme.colorScheme.error)
            }

            Button(
                onClick = viewModel::submit,
                enabled = ui.canSubmit && !ui.isSubmitting,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (ui.isSubmitting) "Saving…" else "Save")
            }
        }
    }
}
