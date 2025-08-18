package org.example.recipesphere.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onLogoutClick: () -> Unit = {}
) {
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
                Button(onClick = onLogoutClick) { Text("Log out (mock)") }
            }
        }
    }
}
