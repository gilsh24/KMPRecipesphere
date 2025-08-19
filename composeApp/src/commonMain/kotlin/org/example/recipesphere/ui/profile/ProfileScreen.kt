package org.example.recipesphere.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onLogoutClick: () -> Unit = {},
    // inject the VM using koinInject to keep your DI style
    viewModel: ProfileViewModel = run {
        val seeder = koinInject<org.example.recipesphere.data.seed.RecipeSeeder>()
        remember { ProfileViewModel(seeder) }
    }
) {
    val ui by viewModel.ui.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Profile") }) }
    ) { inner ->
        Box(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Hello, guest 👋", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(12.dp))
                Button(onClick = onLogoutClick, enabled = !ui.isSeeding) { Text("Log out (mock)") }
                Spacer(Modifier.height(20.dp))
                Button(onClick = { viewModel.seed() }, enabled = !ui.isSeeding) {
                    Text(if (ui.isSeeding) "Seeding…" else "Seed sample recipes")
                }
                if (ui.message != null) {
                    Spacer(Modifier.height(12.dp))
                    Text(ui.message!!, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
