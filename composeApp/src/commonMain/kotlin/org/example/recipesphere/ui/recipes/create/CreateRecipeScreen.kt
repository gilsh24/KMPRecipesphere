package org.example.recipesphere.ui.recipes.create

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject
import org.example.recipesphere.domain.repository.RecipeRepository
import org.example.recipesphere.data.remote.PhotoUploader
import org.example.recipesphere.ui.components.ImagePickerButton
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import coil3.compose.AsyncImagePainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.KeyboardOptions
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
            onCreated(id); viewModel.consumeNavigation()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Recipe") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, null) }
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
            // Photo section
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ImagePickerButton(
                    enabled = !ui.isUploadingPhoto && !ui.isSubmitting
                ) { bytes ->
                    viewModel.onImagePicked(bytes)
                }
                if (ui.isUploadingPhoto) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                }
            }

            if (ui.photoUrl.isNotBlank()) {
                Poster(
                    url = ui.photoUrl,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                )
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
