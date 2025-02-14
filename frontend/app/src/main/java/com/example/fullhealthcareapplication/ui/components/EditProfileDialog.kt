package com.example.fullhealthcareapplication.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun EditProfileDialog(
    onDismiss: () -> Unit,
    onEditProfile: (String, String, String, String) -> Unit,
    nric: String,
    onNricChange: (String) -> Unit,
    role: String,
    onRoleChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    fullName: String,
    onFullNameChange: (String) -> Unit,
){
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Profile") },
        text = {
            Column {
                OutlinedTextField(
                    value = nric,
                    onValueChange = onNricChange,
                    label = { Text("NRIC") },
                    enabled = false,
                )
                OutlinedTextField(
                    value = role,
                    onValueChange = onRoleChange,
                    label = { Text("Role") },
                    enabled = false
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = onEmailChange,
                    label = { Text("Email") }
                )
                OutlinedTextField(
                    value = fullName,
                    onValueChange = onFullNameChange,
                    label = { Text("Full Name") }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onEditProfile(nric, role, email, fullName)
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