package com.example.fullhealthcareapplication.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun DeleteAccountDialog(
    onDismiss: () -> Unit,
    onDeleteAccount: (String, String, String) -> Unit,
    toSignOut: () -> Unit,
    nric: String,
    role: String,
    password: String,
    onPasswordChange: (String) -> Unit
){
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("WARNING: Permanently Delete Account") },
        text = {
            Column {
                OutlinedTextField(
                    value = password,
                    onValueChange = onPasswordChange,
                    label = { Text("Enter Password") },
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onDeleteAccount(nric, role, password)
                    toSignOut()
                }
            ) {
                Text("Delete Account")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        },
        icon = { Icon(Icons.Default.Info, contentDescription = "Delete Warning") }
    )
}