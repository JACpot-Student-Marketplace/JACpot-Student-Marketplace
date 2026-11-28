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
import androidx.compose.ui.unit.dp
import com.example.multiplatformapplication.api.ApiException
import com.example.multiplatformapplication.model.RegisterRequest
import com.example.multiplatformapplication.navigation.LocalAuth
import com.example.multiplatformapplication.navigation.LocalNavigator
import com.example.multiplatformapplication.ui.components.ButtonStyle
import com.example.multiplatformapplication.ui.components.JACpotButton
import com.example.multiplatformapplication.ui.components.JACpotTextField
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

@Composable
fun CreateAccountScreen() {
    val navigator = LocalNavigator.current
    val auth = LocalAuth.current
    val scope = rememberCoroutineScope()
    var firstName by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var submitted by rememberSaveable { mutableStateOf(false) }
    var serverError by rememberSaveable { mutableStateOf<String?>(null) }
    var busy by remember { mutableStateOf(false) }

    fun check() = validateCreateAccount(firstName, lastName, email, password)
    val errors = if (submitted) check() else CreateAccountErrors()

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        AuthHeader()
        Text("Create an account", style = MaterialTheme.typography.headlineMedium)
        Text(
            "Use your John Abbott email.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        JACpotTextField(
            value = firstName,
            onValueChange = { firstName = it },
            modifier = Modifier.fillMaxWidth(),
            label = "First name",
            errorText = errors.firstName,
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        )
        JACpotTextField(
            value = lastName,
            onValueChange = { lastName = it },
            modifier = Modifier.fillMaxWidth(),
            label = "Last name",
            errorText = errors.lastName,
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        )
        JACpotTextField(
            value = email,
            onValueChange = { email = it },
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
            onValueChange = { password = it },
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
        serverError?.let {
            Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
        }
        JACpotButton(
            text = "Create account",
            onClick = {
                submitted = true
                serverError = null
                if (!check().any && !busy) {
                    busy = true
                    scope.launch {
                        try {
                            auth.api.register(
                                RegisterRequest(
                                    firstName = firstName.trim(),
                                    lastName = lastName.trim(),
                                    email = email.trim(),
                                    password = password,
                                ),
                            )
                            navigator.pop()
                        } catch (error: ApiException) {
                            serverError = error.message
                        } catch (error: CancellationException) {
                            throw error
                        } catch (_: Throwable) {
                            serverError = "Could not reach JACpot. Try again."
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
            text = "Log in",
            onClick = { navigator.pop() },
            modifier = Modifier.fillMaxWidth(),
            style = ButtonStyle.Outlined,
        )
    }
}
