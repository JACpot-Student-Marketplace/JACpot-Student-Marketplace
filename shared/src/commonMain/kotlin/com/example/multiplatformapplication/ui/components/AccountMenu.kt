package com.example.multiplatformapplication.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.multiplatformapplication.api.ApiException
import com.example.multiplatformapplication.model.ChangePasswordRequest
import com.example.multiplatformapplication.navigation.LocalAuth
import com.example.multiplatformapplication.navigation.LocalNavigator
import com.example.multiplatformapplication.navigation.LoginScreenKey
import com.example.multiplatformapplication.ui.screens.ChangePasswordErrors
import com.example.multiplatformapplication.ui.screens.validateChangePassword
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

/**
 * Top-right account control for the signed-in student: name, email, log out, and change password.
 */
@Composable
fun AccountMenu() {
    val auth = LocalAuth.current
    val navigator = LocalNavigator.current
    val scope = rememberCoroutineScope()
    var expanded by remember { mutableStateOf(false) }
    var logoutError by remember { mutableStateOf<String?>(null) }
    var loggingOut by remember { mutableStateOf(false) }
    var showPasswordDialog by remember { mutableStateOf(false) }
    val user = auth.session?.user

    if (user != null) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(Modifier.weight(1f))
            Box {
                JACpotButton(
                    text = user.firstName,
                    onClick = { expanded = true },
                    style = ButtonStyle.Text,
                )
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    Text(
                        "${user.firstName} ${user.lastName}",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        user.email,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    logoutError?.let {
                        Text(
                            it,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                    DropdownMenuItem(
                        text = { Text("Log out") },
                        enabled = !loggingOut,
                        onClick = {
                            if (!loggingOut) {
                                loggingOut = true
                                logoutError = null
                                scope.launch {
                                    try {
                                        auth.api.logout()
                                        auth.clear()
                                        navigator.reset(LoginScreenKey)
                                    } catch (error: ApiException) {
                                        logoutError = error.message
                                    } catch (error: CancellationException) {
                                        throw error
                                    } catch (_: Throwable) {
                                        logoutError = "Could not reach JACpot. Try again."
                                    } finally {
                                        loggingOut = false
                                    }
                                }
                            }
                        },
                    )
                    DropdownMenuItem(
                        text = { Text("Change password") },
                        onClick = {
                            expanded = false
                            showPasswordDialog = true
                        },
                    )
                }
            }
        }
        if (showPasswordDialog) {
            ChangePasswordDialog(
                onDismiss = { showPasswordDialog = false },
                onSubmit = { currentPassword, newPassword ->
                    auth.api.changePassword(
                        ChangePasswordRequest(
                            currentPassword = currentPassword,
                            newPassword = newPassword,
                        ),
                    )
                },
            )
        }
    }
}

@Composable
private fun ChangePasswordDialog(
    onDismiss: () -> Unit,
    onSubmit: suspend (currentPassword: String, newPassword: String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var submitted by remember { mutableStateOf(false) }
    var serverError by remember { mutableStateOf<String?>(null) }
    var busy by remember { mutableStateOf(false) }
    val errors = if (submitted) validateChangePassword(currentPassword, newPassword) else ChangePasswordErrors()

    fun submit() {
        submitted = true
        serverError = null
        if (validateChangePassword(currentPassword, newPassword).any || busy) return
        busy = true
        scope.launch {
            try {
                onSubmit(currentPassword, newPassword)
                onDismiss()
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

    AlertDialog(
        onDismissRequest = { if (!busy) onDismiss() },
        title = { Text("Change password") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                JACpotTextField(
                    value = currentPassword,
                    onValueChange = {
                        currentPassword = it
                        serverError = null
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = "Current password",
                    errorText = errors.currentPassword,
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                )
                JACpotTextField(
                    value = newPassword,
                    onValueChange = {
                        newPassword = it
                        serverError = null
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = "New password",
                    errorText = errors.newPassword,
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                )
                serverError?.let {
                    Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
                }
            }
        },
        confirmButton = {
            JACpotButton("Change password", onClick = ::submit, enabled = !busy)
        },
        dismissButton = {
            JACpotButton("Cancel", onClick = onDismiss, style = ButtonStyle.Text, enabled = !busy)
        },
    )
}
