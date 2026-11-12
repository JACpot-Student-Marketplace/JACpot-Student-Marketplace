package com.example.multiplatformapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.multiplatformapplication.model.Request
import com.example.multiplatformapplication.navigation.LocalNavigator
import com.example.multiplatformapplication.ui.components.ButtonStyle
import com.example.multiplatformapplication.ui.components.JACpotButton
import com.example.multiplatformapplication.ui.components.JACpotDropdown
import com.example.multiplatformapplication.ui.components.JACpotTextField
import kotlin.time.Clock

private val monthNames =
    listOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")

// First entry of the optional date dropdowns: picking it clears the choice.
private const val NONE = "—"

@Composable
fun CreateRequestScreen(onSubmit: (Request) -> Unit) {
    val navigator = LocalNavigator.current
    var title by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var courseCode by rememberSaveable { mutableStateOf("") }
    var budget by rememberSaveable { mutableStateOf("") }
    var month by rememberSaveable { mutableStateOf<Int?>(null) }
    var day by rememberSaveable { mutableStateOf<Int?>(null) }
    var year by rememberSaveable { mutableStateOf<Int?>(null) }
    var submitted by rememberSaveable { mutableStateOf(false) }

    // Re-checked on every change once Post was pressed, so errors vanish as soon as the input is fixed.
    fun check() = validateRequestForm(title, description, courseCode, budget, month, day, year, Clock.System.now().toEpochMilliseconds())
    val errors = if (submitted) check() else RequestFormErrors()

    val thisYear = currentYear()
    val pickedMonth = month
    val dayCount = if (pickedMonth == null) 31 else daysInMonth(pickedMonth, year)

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        JACpotButton("Back", onClick = { navigator.pop() }, style = ButtonStyle.Outlined)
        Text("Post a request", style = MaterialTheme.typography.headlineMedium)
        Text(
            "Tell students what you need. Add a course code, a maximum budget, or both.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        JACpotTextField(title, { title = it }, Modifier.fillMaxWidth(), label = "Item title", maxLines = 2, errorText = errors.title)
        JACpotTextField(description, {
            description = it
        }, Modifier.fillMaxWidth(), label = "Description", maxLines = 4, errorText = errors.description)
        JACpotTextField(courseCode, {
            courseCode = it
        }, Modifier.fillMaxWidth(), label = "Course code", singleLine = true, errorText = errors.courseOrBudget)
        JACpotTextField(
            budget,
            { budget = it },
            Modifier.fillMaxWidth(),
            label = "Maximum budget (CAD)",
            errorText = errors.budget ?: errors.courseOrBudget,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        )

        Text("Needed by (optional)", style = MaterialTheme.typography.titleMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            JACpotDropdown(
                label = "Month",
                options = listOf(NONE) + monthNames,
                selected = month?.let { monthNames[it - 1] } ?: "",
                onSelect = { picked ->
                    month = monthNames.indexOf(picked).takeIf { it >= 0 }?.plus(1)
                    // A shorter month can make the chosen day invalid (e.g. 31 -> February).
                    day = day?.takeIf { month == null || it <= daysInMonth(month!!, year) }
                },
                modifier = Modifier.weight(1.4f),
            )
            JACpotDropdown(
                label = "Day",
                options = listOf(NONE) + (1..dayCount).map { it.toString() },
                selected = day?.toString() ?: "",
                onSelect = { day = it.toIntOrNull() },
                modifier = Modifier.weight(1f),
            )
            JACpotDropdown(
                label = "Year",
                options = listOf(NONE, thisYear.toString(), (thisYear + 1).toString()),
                selected = year?.toString() ?: "",
                onSelect = { picked ->
                    year = picked.toIntOrNull()
                    day = day?.takeIf { month == null || it <= daysInMonth(month!!, year) }
                },
                modifier = Modifier.weight(1f),
            )
        }
        errors.neededBy?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error) }

        JACpotButton(
            "Post request",
            onClick = {
                submitted = true
                if (!check().any) {
                    val now = Clock.System.now().toEpochMilliseconds()
                    onSubmit(buildRequest(title, description, courseCode, budget, month, day, year, now))
                }
            },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
