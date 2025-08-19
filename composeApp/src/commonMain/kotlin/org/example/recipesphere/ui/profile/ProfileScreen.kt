package org.example.recipesphere.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject
import org.example.recipesphere.domain.repository.AuthRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onLoggedOut: () -> Unit, // navigate after real sign-out
    viewModel: ProfileViewModel = run {
        val auth: AuthRepository = koinInject()
        remember { ProfileViewModel(auth) }
    }
) {
    val ui by viewModel.ui.collectAsState()

    // Navigate back to Login when logout completes
    LaunchedEffect(ui.loggedOut) {
        if (ui.loggedOut) {
            onLoggedOut()
            viewModel.consumeLoggedOut()
        }
    }

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
                Text(
                    text = ui.email?.let { "Signed in as: $it" } ?: "Not signed in",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = { viewModel.signOut() },
                    enabled = !ui.isLoading
                ) {
                    Text(if (ui.isLoading) "Signing out…" else "Log out")
                }

                if (ui.error != null) {
                    Spacer(Modifier.height(12.dp))
                    Text(ui.error!!, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
