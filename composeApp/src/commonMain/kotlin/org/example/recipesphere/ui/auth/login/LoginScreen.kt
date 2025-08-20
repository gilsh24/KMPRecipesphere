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

    // Navigate ONLY after a verified sign-in/sign-up success
    LaunchedEffect(ui.isLoggedIn) {
        if (ui.isLoggedIn) {
            onLoginSuccess()
            viewModel.acknowledgeNavigation()
        }
    }

    Scaffold(topBar = { TopAppBar(title = { Text("Login") }) }) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
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
                supportingText = { ui.emailError?.let { Text(it) } },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = ui.password,
                onValueChange = viewModel::onPasswordChange,
                label = { Text("Password") },
                isError = ui.passwordError != null,
                supportingText = { ui.passwordError?.let { Text(it) } },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            ui.errorMessage?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(8.dp))
            }

            Button(
                onClick = viewModel::signIn,
                enabled = ui.canSubmit && !ui.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) { Text(if (ui.isLoading) "Signing in…" else "Sign in") }

            Spacer(Modifier.height(8.dp))

            // Keep explicit sign-up. If the user isn't registered, sign-in will fail and they won't enter.
            OutlinedButton(
                onClick = viewModel::signUp,
                enabled = ui.canSubmit && !ui.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Create account") }
        }
    }
}
