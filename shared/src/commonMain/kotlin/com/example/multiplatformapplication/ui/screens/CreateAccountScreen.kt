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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.multiplatformapplication.navigation.LocalNavigator
import com.example.multiplatformapplication.ui.components.ButtonStyle
import com.example.multiplatformapplication.ui.components.JACpotButton
import com.example.multiplatformapplication.ui.components.JACpotTextField

@Composable
fun CreateAccountScreen() {
    val navigator = LocalNavigator.current
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var submitted by rememberSaveable { mutableStateOf(false) }

    fun check() = validateCreateAccount(name, email, password)
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
            value = name,
            onValueChange = { name = it },
            modifier = Modifier.fillMaxWidth(),
            label = "Name",
            errorText = errors.name,
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
        JACpotButton(
            text = "Create account",
            onClick = {
                submitted = true
                if (!check().any) navigator.pop()
            },
            modifier = Modifier.fillMaxWidth(),
        )
        JACpotButton(
            text = "Log in",
            onClick = { navigator.pop() },
            modifier = Modifier.fillMaxWidth(),
            style = ButtonStyle.Outlined,
        )
    }
}
