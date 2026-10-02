package com.example.multiplatformapplication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen() {
    val navigator = LocalNavigator.current
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var isLoggedIn by rememberSaveable { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf("") }

    val history: SnapshotStateList<String> =
        rememberSaveable(
            saver =
                listSaver(
                    save = { it.toList() },
                    restore = { it.toMutableStateList() },
                ),
        ) { mutableStateListOf() }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Card(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            colors =
                CardDefaults.cardColors(
                    containerColor = Color(0xFFF5F7FA),
                ),
            elevation =
                CardDefaults.cardElevation(
                    defaultElevation = 4.dp,
                ),
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    "JAC STUDENT MARKETPLACE",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(32.dp))

                if (!isLoggedIn) {
                    TextField(
                        value = username,
                        onValueChange = { username = it },
                        textStyle = TextStyle(textAlign = TextAlign.Center),
                        label = {
                            Text("Please enter your username")
                        },
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    TextField(
                        value = password,
                        onValueChange = { password = it },
                        textStyle = TextStyle(textAlign = TextAlign.Center),
                        label = {
                            Text("Please enter your password")
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions =
                            KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done,
                            ),
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            if (username.isEmpty() || password.isEmpty()) {
                                errorMessage = "Enter a username and password."
                            } else {
                                val success = authenticate(username, password)
                                history.add(0, formatAttempt(username, success))

                                if (success) {
                                    isLoggedIn = true
                                    navigator.replace(MarketplaceScreenKey)
                                    errorMessage = ""
                                } else {
                                    errorMessage = "Wrong username or password."
                                }

                                password = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("Log In")
                    }

                    if (errorMessage.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(errorMessage)
                    }
                } else {
                    Text("Welcome $username!")

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            isLoggedIn = false
                            username = ""
                            password = ""
                            errorMessage = ""
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("Log Out")
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
        Text(
            "History:",
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(8.dp))

        for (entry in history) {
            Text(entry)
        }
    }
}
