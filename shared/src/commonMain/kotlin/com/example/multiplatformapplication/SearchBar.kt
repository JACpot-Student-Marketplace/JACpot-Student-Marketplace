package com.example.multiplatformapplication

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction

/**
 * Search field for the marketplace listing list.
 *
 * [query] is owned by the caller. That same string is what the screen uses to
 * decide which listings stay visible, and the caller stores it with
 * rememberSaveable so a rotation does not wipe what the user typed.
 * [onQueryChange] runs on every text change, including the clear button,
 * so the parent can write the new value into state and recompose.
 */
@Composable
fun MarketplaceSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier,
        singleLine = true,
        placeholder = { Text("Search listings") },
        trailingIcon = {
            if (query.isNotEmpty()) {
                TextButton(onClick = { onQueryChange("") }) { Text("Clear") }
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = { keyboardController?.hide() }
        )
    )
}