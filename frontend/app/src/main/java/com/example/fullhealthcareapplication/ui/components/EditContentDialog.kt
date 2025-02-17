package com.example.fullhealthcareapplication.ui.components

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditContentDialog (
    onDismiss: () -> Unit,
    onEditContent: (Int, Int, String, String, String, String) -> Unit,
    id: Int,
    contentCategoryId: Int,
    onContentCategoryIdChange: (Int) -> Unit,
    title: String,
    onTitleChange: (String) -> Unit,
    summary: String,
    onSummaryChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    picture: String,
    onPictureChange: (String) -> Unit,
){
    val context = LocalContext.current
    val categories = listOf("Diet", "Exercise", "Medicine", "Mental Health", "Environment", "Disease")
    val categoriesState = remember { mutableStateOf("") }
    val selectedTextState = remember(contentCategoryId) {
        mutableStateOf(categories.getOrNull(contentCategoryId - 1) ?: "Select Category")
    }
    val expanded = remember { mutableStateOf(false) }
    val isFormValid = remember(title, summary, description, contentCategoryId){
        title.isNotBlank() && summary.isNotBlank() && description.isNotBlank() && contentCategoryId > 0
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Content") },
        text = {
            Column {
                ExposedDropdownMenuBox(
                    expanded = expanded.value,
                    onExpandedChange = {
                        expanded.value = !expanded.value
                    }
                ) {
                    TextField(
                        value = selectedTextState.value,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded.value,
                        onDismissRequest = { expanded.value = false }
                    ) {
                        categories.forEachIndexed { index, item ->
                            DropdownMenuItem(
                                text = { Text(text = item) },
                                onClick = {
                                    selectedTextState.value = item
                                    onContentCategoryIdChange(index+1)
                                    categoriesState.value = item
                                    expanded.value = false
                                    Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                }
                LimitedTextField(
                    value = title,
                    onValueChange = onTitleChange,
                    label = "Title",
                    maxLength = 100
                )

                LimitedTextField(
                    value = summary,
                    onValueChange = onSummaryChange,
                    label = "Summary",
                    maxLength = 200,
                    modifier = Modifier.height(150.dp),
                    maxLines = 5
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = onDescriptionChange,
                    label = { Text("Description") },
                    modifier = Modifier.height(200.dp),
                    maxLines = 20
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onEditContent(id, contentCategoryId, title, summary, description, picture)
                },
                enabled = isFormValid
            ) {
                Text("Edit")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
    )

}