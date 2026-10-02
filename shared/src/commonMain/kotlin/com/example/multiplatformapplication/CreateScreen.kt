package com.example.multiplatformapplication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.toMutableStateList

//Simple dataclass holding the values inputted by a user which represent an item to be "sold"
data class Item(
    val title: String,
    val description: String,
    val price: Double,
    val category: String
)

///This function is the main screen manager, it handles all the creation of the ui, allowing for user inputs, and updating it
///The function also allows for the creation of Items which are then added to a list of other items. This list is then displayed to the user.
///Validation and screen rotation is also handled here, ensuring the user has a smooth experience.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateSellItemForm(
    modifier: Modifier = Modifier,
    onItemCreated: (Item) -> Unit = {}
) {
    val navigator = LocalNavigator.current

    //The saved inputs from the user, using rememberSaveable to ensure they remain on screen rotation
    var titleInput by rememberSaveable { mutableStateOf("") };
    var descInput by rememberSaveable { mutableStateOf("") };
    var priceInput by rememberSaveable { mutableStateOf("") };

    //The dropdown menu stuff, such as the options, the selected option, and the expanded state bool
    val categories = listOf("Books", "Clothing", "Electronics", "School Supplies", "Other");
    var selectedCategory by rememberSaveable { mutableStateOf("") };
    var expanded by rememberSaveable { mutableStateOf(false) };

    //The bools holding the error states for each input
    var titleError by rememberSaveable { mutableStateOf(false) };
    var descError by rememberSaveable { mutableStateOf(false) };
    var priceError by rememberSaveable { mutableStateOf(false) };
    var categoryError by rememberSaveable { mutableStateOf(false) };

    val itemList = rememberSaveable(
        saver = listSaver(
            save = { stateList -> //triggers before the screen is reset/rotated
                stateList.flatMap { listOf(it.title, it.description, it.price, it.category) } //takes the object to flatten it into a continuous list, giving is all the properties with the listof
            },
            restore = { savedList -> //this triggers once the rotation is done
                val restoredList = mutableStateListOf<Item>()
                for (i in savedList.indices step 4) { //go through the flattened list and readd the items (step size 4 as we have 4 properties per item)
                    restoredList.add(
                        Item(
                            title = savedList[i] as String,
                            description = savedList[i + 1] as String,
                            price = savedList[i + 2] as Double,
                            category = savedList[i + 3] as String
                        )
                    )
                }
                restoredList
            }
        )
    ) {
        mutableStateListOf<Item>()
    }


    Column {
        Button(onClick = { navigator.pop() }) { Text("Back to Marketplace") }
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(15.dp), //alignment and padding to make everything look nice! Hopefully lol
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        item {
            Column(
                modifier = Modifier.fillMaxWidth().padding(top = 14.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                ){
                Text(
                    text = "Fill out your item's information below", //simple title message to the user, using a text style
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                OutlinedTextField(
                    value = titleInput, //updating the value to the new value from the user
                    onValueChange = { titleInput = it }, //saving the value inputted from the user
                    label = { Text("Enter the title of your item")}, //the label telling the user what to input
                    modifier = Modifier.fillMaxWidth().padding(8.dp), //padding and alignment
                    maxLines = 2, //the max number of lines the box will expand to
                    supportingText = {if (titleError) Text(text = "Title cannot be empty", color = Color.Red) }
                    //the above displays the error message as supporting text if an error is detected when the create button is pressed
                    )
                OutlinedTextField(
                    value = descInput,
                    onValueChange = { descInput = it },
                    label = { Text("Enter the description of your item")},
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    maxLines = 4,
                    supportingText = {if (descError) Text(text = "Description cannot be empty", color = Color.Red) }
                )
                OutlinedTextField(
                    value = priceInput,
                    onValueChange = { priceInput = it },
                    label = { Text("Enter the price of your item")},
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    maxLines = 1,
                    supportingText = {if (priceError) Text(text = "Price must be a number more than 0", color = Color.Red) }
                )
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                ) {
                    OutlinedTextField(
                        modifier = Modifier.menuAnchor(),
                        value = selectedCategory,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        supportingText = {if (categoryError) Text(text = "Title cannot be empty", color = Color.Red) }
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false } //updating the expanded state
                    ) {
                        categories.forEach { category -> //go over each option from the list of categories
                            DropdownMenuItem(
                                text = { Text(text = category) },
                                onClick = {
                                    selectedCategory = category
                                    expanded = false
                                } //saving the choice selected and updating expanded back down to false
                            )
                        }
                    }
                }
                Button(onClick = {
                    var isValid = true; //reset at the start

                    if (titleInput.isEmpty()) { //check if title is empty
                        isValid = false; //if so set isvalid to false so we dont create the item
                        titleError = true; //and set title error to true so that the user knows whats wrong
                    } else { //this means the title is valid
                        titleError = false;  //so we can reset the error to false!
                    }

                    if (descInput.isEmpty()) {
                        isValid = false;
                        descError = true;
                    } else {
                        descError = false;
                    }

                    //The below checks if the price is a number and more than 0 as well as empty
                    if (priceInput.isEmpty() || priceInput.toDoubleOrNull() == null || priceInput.toDouble() <= 0) {
                        isValid = false;
                        priceError = true;
                    } else {
                        priceError = false;
                    }

                    if (selectedCategory.isEmpty()) {
                        isValid = false;
                        categoryError = true;
                    } else {
                        categoryError = false;
                    }

                    if(isValid) //if all the inputs are valid, create the item
                    {
                        val newItem = Item(titleInput, descInput, priceInput.toDouble(), selectedCategory)
                        itemList.add(newItem)
                        onItemCreated(newItem)
                        titleInput = "" //reset all the fields to nothing so the user can create another item
                        descInput = ""
                        priceInput = ""
                        selectedCategory = ""
                    }
                }) {
                    Text(text = "Create Item")
                }
            }
        }

        if (itemList.isNotEmpty()) //if we have an item created, display the text
        {
            item {
                Text(
                    text = "Recently Created Items",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        items(itemList) { item -> //for each of the items in the list, display the item using the card
            Card(
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = "Title: ${item.title}") //the fields the card is displaying
                    Text(text = "Description: ${item.description}")
                    Text(text = "Price: $${item.price}")
                    Text(text = "Category: ${item.category}")
                }
            }
        }
    }
}
}
