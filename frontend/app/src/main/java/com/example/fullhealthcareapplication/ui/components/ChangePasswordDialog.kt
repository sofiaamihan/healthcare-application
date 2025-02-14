package com.example.fullhealthcareapplication.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

@Composable
fun ChangePasswordDialog(
    onDismiss: () -> Unit,
    onChangePassword: (String, String, String, String) -> Unit,
    nric: String,
    role: String,
    password: String,
    onPasswordChange: (String) -> Unit,
    newPassword: String,
    onNewPasswordChange: (String) -> Unit,
    confirmNewPassword: String,
    onConfirmNewPasswordChange: (String) -> Unit,
){
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Change Password") },
        text = {
            Column {
                OutlinedTextField(
                    value = password,
                    onValueChange = onPasswordChange,
                    label = { Text("Old Password") },
                )
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = onNewPasswordChange,
                    label = { Text("New Password") },
                )
                OutlinedTextField(
                    value = confirmNewPassword,
                    onValueChange = onConfirmNewPasswordChange,
                    label = { Text("Confirm New Password") },
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onChangePassword(nric, role, password, newPassword)
                }
            ) {
                Text("Change Password")
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