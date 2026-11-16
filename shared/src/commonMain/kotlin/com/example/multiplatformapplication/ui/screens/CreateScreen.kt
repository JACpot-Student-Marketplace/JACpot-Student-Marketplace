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
import androidx.compose.material3.HorizontalDivider
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
import com.example.multiplatformapplication.model.ItemCondition
import com.example.multiplatformapplication.model.ListingCategory
import com.example.multiplatformapplication.model.label
import com.example.multiplatformapplication.navigation.LocalNavigator
import com.example.multiplatformapplication.state.LocalListings
import com.example.multiplatformapplication.ui.components.ButtonStyle
import com.example.multiplatformapplication.ui.components.JACpotButton
import com.example.multiplatformapplication.ui.components.JACpotDropdown
import com.example.multiplatformapplication.ui.components.JACpotTextField

// First entry of the optional dropdowns: picking it clears the choice.
private const val NONE = "—"
private const val ACCESS_USED = "Used"
private const val ACCESS_UNUSED = "Not used"

/**
 * Create Listing form. Fields are saveable so a rotation keeps what was typed; errors appear after the
 * first Post press and disappear as each field is fixed. Book fields only show for Textbooks.
 * A valid post is saved through the shared ViewModel, then [onCreated] tells Router to leave the form.
 */
@Composable
fun CreateSellItemForm(
    modifier: Modifier = Modifier,
    onCreated: () -> Unit = {},
) {
    val navigator = LocalNavigator.current
    val listings = LocalListings.current
    var title by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var price by rememberSaveable { mutableStateOf("") }
    var selectedCategory by rememberSaveable { mutableStateOf("") }
    var selectedCondition by rememberSaveable { mutableStateOf("") }
    var courseCode by rememberSaveable { mutableStateOf("") }
    var author by rememberSaveable { mutableStateOf("") }
    var releaseYear by rememberSaveable { mutableStateOf<Int?>(null) }
    var accessCodeUsed by rememberSaveable { mutableStateOf<Boolean?>(null) }
    var photos by rememberSaveable { mutableStateOf("") }
    var submitted by rememberSaveable { mutableStateOf(false) }

    val categories = ListingCategory.entries.map { it.label }
    val isTextbook = selectedCategory == ListingCategory.TEXTBOOKS.label

    // Re-checked on every change once Post was pressed, so errors vanish as soon as the input is fixed.
    fun check() = validateListingForm(title, description, price, selectedCategory, selectedCondition, photos)
    val errors = if (submitted) check() else ListingFormErrors()

    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        JACpotButton("Back to Marketplace", onClick = { navigator.pop() }, style = ButtonStyle.Outlined)
        Text("Post a listing", style = MaterialTheme.typography.headlineMedium)
        Text(
            "Fill out your item's details so students can find and buy it.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        JACpotTextField(
            value = title,
            onValueChange = { title = it },
            modifier = Modifier.fillMaxWidth(),
            label = "Item title",
            maxLines = 2,
            errorText = errors.title,
        )
        JACpotTextField(
            value = description,
            onValueChange = { description = it },
            modifier = Modifier.fillMaxWidth(),
            label = "Description",
            maxLines = 4,
            errorText = errors.description,
        )
        JACpotTextField(
            value = price,
            onValueChange = { price = it },
            modifier = Modifier.fillMaxWidth(),
            label = "Price (CAD)",
            singleLine = true,
            errorText = errors.price,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        )
        JACpotDropdown(
            label = "Category",
            options = categories,
            selected = selectedCategory,
            onSelect = { selectedCategory = it },
            modifier = Modifier.fillMaxWidth(),
            errorText = errors.category,
        )
        JACpotDropdown(
            label = "Condition",
            options = ItemCondition.entries.map { it.label },
            selected = selectedCondition,
            onSelect = { selectedCondition = it },
            modifier = Modifier.fillMaxWidth(),
            errorText = errors.condition,
        )
        JACpotTextField(
            value = courseCode,
            onValueChange = { courseCode = it },
            modifier = Modifier.fillMaxWidth(),
            label = "Course code (optional)",
            placeholder = "e.g. 201-NYA-05",
            singleLine = true,
        )

        // Only textbooks get book fields, matching the listings_book_details database rule.
        if (isTextbook) {
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Text("Book details", style = MaterialTheme.typography.titleMedium)
            JACpotTextField(
                value = author,
                onValueChange = { author = it },
                modifier = Modifier.fillMaxWidth(),
                label = "Author (optional)",
                singleLine = true,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                val thisYear = currentYear() // Luca's helper in DateUtils.kt
                JACpotDropdown(
                    label = "Release year",
                    options = listOf(NONE) + (thisYear downTo 1980).map { it.toString() },
                    selected = releaseYear?.toString() ?: "",
                    onSelect = { releaseYear = it.toIntOrNull() },
                    modifier = Modifier.weight(1f),
                )
                JACpotDropdown(
                    label = "Online access code",
                    options = listOf(NONE, ACCESS_USED, ACCESS_UNUSED),
                    selected =
                        when (accessCodeUsed) {
                            true -> ACCESS_USED
                            false -> ACCESS_UNUSED
                            null -> ""
                        },
                    onSelect = { picked ->
                        accessCodeUsed =
                            when (picked) {
                                ACCESS_USED -> true
                                ACCESS_UNUSED -> false
                                else -> null
                            }
                    },
                    modifier = Modifier.weight(1f),
                )
            }
        }

        // shortcut: photo links stand in for uploads until Supabase storage is connected.
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        JACpotTextField(
            value = photos,
            onValueChange = { photos = it },
            modifier = Modifier.fillMaxWidth(),
            label = "Photo links (optional)",
            maxLines = 5,
            errorText = errors.photos,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
        )
        Text(
            "One link per line, up to 5. The first one is the cover.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        JACpotButton(
            text = "Post listing",
            onClick = {
                submitted = true
                if (!check().any) {
                    // The ViewModel sets the id, owner and timestamps.
                    listings.addListing(
                        title = title.trim(),
                        description = description.trim(),
                        category = ListingCategory.entries.first { it.label == selectedCategory },
                        condition = ItemCondition.entries.first { it.label == selectedCondition },
                        priceCents = requireNotNull(parseBudgetCents(price)),
                        courseCode = courseCode.trim().ifBlank { null },
                        // Book fields are saved only for textbooks, even if typed before switching category.
                        author = author.trim().ifBlank { null }.takeIf { isTextbook },
                        releaseDate = releaseYear?.let { "$it-01-01" }.takeIf { isTextbook },
                        accessCodeUsed = accessCodeUsed.takeIf { isTextbook },
                        photoUrls = parsePhotoUrls(photos),
                    )
                    onCreated()
                }
            },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
