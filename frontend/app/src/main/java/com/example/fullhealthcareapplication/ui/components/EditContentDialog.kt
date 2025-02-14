package com.example.fullhealthcareapplication.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

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
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Content") },
        text = {
            Column {
                OutlinedTextField(
                    value = contentCategoryId.toString(),
                    onValueChange = { newValue -> onContentCategoryIdChange(newValue.toIntOrNull() ?: 0) },
                    label = { Text("Content Category Id") }
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
                    onEditContent(id, contentCategoryId, title, summary, description, picture)
                }
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