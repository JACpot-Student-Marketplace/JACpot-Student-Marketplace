package com.example.multiplatformapplication.ui.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction

/**
 * Search field for any filterable list (listings, requests, ...).
 *
 * [query] is owned by the caller. That same string is what the screen uses to
 * decide which items stay visible, and the caller stores it with
 * rememberSaveable so a rotation does not wipe what the user typed.
 * [placeholder] says what is being searched, e.g. "Search listings".
 * [onQueryChange] runs on every text change, including the clear button,
 * so the parent can write the new value into state and recompose.
 */
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier,
        singleLine = true,
        placeholder = { Text(placeholder) },
        trailingIcon = {
            if (query.isNotEmpty()) {
                JACpotButton("Clear", onClick = { onQueryChange("") }, style = ButtonStyle.Text)
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions =
            KeyboardActions(
                onSearch = { keyboardController?.hide() },
            ),
    )
}
