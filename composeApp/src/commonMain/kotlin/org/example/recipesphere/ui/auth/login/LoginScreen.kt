package org.example.recipesphere.ui.auth.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject
import org.example.recipesphere.domain.repository.AuthRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = run {
        val authRepo: AuthRepository = koinInject()
        remember { LoginViewModel(authRepo) }
    }
) {
    val ui by viewModel.ui.collectAsState()

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
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            if (ui.errorMessage != null) {
                Text(ui.errorMessage!!, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(8.dp))
            }

            Button(
                onClick = { viewModel.signIn() },
                enabled = ui.canSubmit && !ui.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) { Text(if (ui.isLoading) "Signing in…" else "Sign in") }

            Spacer(Modifier.height(8.dp))

            OutlinedButton(
                onClick = { viewModel.signUp() },
                enabled = ui.canSubmit && !ui.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Create account") }

            Spacer(Modifier.height(8.dp))

            TextButton(
                onClick = onLoginSuccess, // keep guest path if you like
                enabled = !ui.isLoading
            ) { Text("Continue as guest") }
        }
    }
}
