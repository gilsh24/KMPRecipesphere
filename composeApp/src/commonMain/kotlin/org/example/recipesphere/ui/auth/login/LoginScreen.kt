package org.example.recipesphere.ui.auth.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = remember { LoginViewModel() }
) {
    val ui by viewModel.ui.collectAsState()

    // Navigate once when we flip to logged-in
    LaunchedEffect(ui.isLoggedIn) {
        if (ui.isLoggedIn) {
            onLoginSuccess()
            viewModel.acknowledgeNavigation()
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Login") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = ui.email,
                onValueChange = viewModel::onEmailChange,
                label = { Text("Email") },
                isError = ui.emailError != null,
                supportingText = { if (ui.emailError != null) Text(ui.emailError!!) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = ui.password,
                onValueChange = viewModel::onPasswordChange,
                label = { Text("Password") },
                isError = ui.passwordError != null,
                supportingText = { if (ui.passwordError != null) Text(ui.passwordError!!) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(20.dp))

            Button(
                onClick = { viewModel.submit() },
                enabled = ui.canSubmit,
                modifier = Modifier.fillMaxWidth()
            ) { Text(if (ui.isLoading) "Signing in…" else "Sign in") }

            Spacer(Modifier.height(8.dp))

            TextButton(
                onClick = onLoginSuccess, // "guest" path for now
                enabled = !ui.isLoading
            ) { Text("Continue as guest") }

            if (ui.isLoading) {
                Spacer(Modifier.height(16.dp))
                CircularProgressIndicator()
            }
        }
    }
}
