package com.example.multiplatformapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.multiplatformapplication.api.ApiException
import com.example.multiplatformapplication.model.LoginRequest
import com.example.multiplatformapplication.navigation.CreateAccountScreenKey
import com.example.multiplatformapplication.navigation.HomeScreenKey
import com.example.multiplatformapplication.navigation.LocalAuth
import com.example.multiplatformapplication.navigation.LocalNavigator
import com.example.multiplatformapplication.ui.components.ButtonStyle
import com.example.multiplatformapplication.ui.components.JACpotButton
import com.example.multiplatformapplication.ui.components.JACpotTextField
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

@Composable
fun LoginScreen() {
    val navigator = LocalNavigator.current
    val auth = LocalAuth.current
    val scope = rememberCoroutineScope()
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var submitted by rememberSaveable { mutableStateOf(false) }
    var credentialsError by rememberSaveable { mutableStateOf<String?>(null) }
    var busy by remember { mutableStateOf(false) }

    fun check() = validateLogin(email, password)
    val errors = if (submitted) check() else LoginErrors()

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        AuthHeader()
        Text("Your campus.\nYour marketplace.", style = MaterialTheme.typography.headlineMedium)
        Text(
            "Buy used. Sell what you no longer need. Find it at John Abbott.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            shape = MaterialTheme.shapes.medium,
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary,
                    shape = MaterialTheme.shapes.small,
                ) {
                    Text(
                        "JOHN ABBOTT",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
                Text("Made for student-to-student exchanges.", style = MaterialTheme.typography.bodyMedium)
            }
        }
        Text("Welcome back", style = MaterialTheme.typography.titleLarge)
        JACpotTextField(
            value = email,
            onValueChange = {
                email = it
                credentialsError = null
            },
            modifier = Modifier.fillMaxWidth(),
            label = "Student email",
            placeholder = "you@johnabbottcollege.net",
            errorText = errors.email,
            singleLine = true,
            keyboardOptions =
                KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                ),
        )
        JACpotTextField(
            value = password,
            onValueChange = {
                password = it
                credentialsError = null
            },
            modifier = Modifier.fillMaxWidth(),
            label = "Password",
            errorText = errors.password,
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions =
                KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                ),
        )
        credentialsError?.let {
            Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
        }
        Text(
            "Forgot password?",
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.End,
        )
        JACpotButton(
            text = "Log in",
            onClick = {
                submitted = true
                credentialsError = null
                if (!check().any && !busy) {
                    busy = true
                    scope.launch {
                        try {
                            val response = auth.api.login(LoginRequest(email.trim(), password))
                            auth.save(response)
                            navigator.replace(HomeScreenKey)
                        } catch (error: ApiException) {
                            credentialsError = error.message
                        } catch (error: CancellationException) {
                            throw error
                        } catch (_: Throwable) {
                            credentialsError = "Could not reach JACpot. Try again."
                        } finally {
                            busy = false
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !busy,
        )
        JACpotButton(
            text = "Create an account",
            onClick = { navigator.navigate(CreateAccountScreenKey) },
            modifier = Modifier.fillMaxWidth(),
            style = ButtonStyle.Outlined,
        )
        Text(
            "By continuing, you agree to the Terms and Privacy Policy.",
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}
