package com.example.fullhealthcareapplication.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun AddContentDialog(
    onDismiss: () -> Unit,
    onAddContent: (Int, String, String, String, String) -> Unit,
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
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Content") },
        text = {
            Column {
                OutlinedTextField(
                    value = contentCategoryId.takeIf { it != 0 }?.toString() ?: "",
                    onValueChange = { newValue ->
                        onContentCategoryIdChange(newValue.toIntOrNull() ?: 0)
                    },
                    label = { Text("Activity Category Id") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(
                    value = title,
                    onValueChange = onTitleChange,
                    label = { Text("Title") }
                )
                OutlinedTextField(
                    value = summary,
                    onValueChange = onSummaryChange,
                    label = { Text("Summary") }
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = onDescriptionChange,
                    label = { Text("Description") }
                )
                OutlinedTextField(
                    value = picture,
                    onValueChange = onPictureChange,
                    label = { Text("Picture") }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onAddContent(contentCategoryId, title, summary, description, picture)
                }
            ) {
                Text("Add")
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