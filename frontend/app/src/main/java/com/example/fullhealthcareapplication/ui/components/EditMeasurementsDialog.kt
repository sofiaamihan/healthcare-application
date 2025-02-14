package com.example.fullhealthcareapplication.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun EditMeasurementsDialog(
    onDismiss: () -> Unit,
    onEditMeasurements: (Int, String, String, Int, String, Double, Double) -> Unit,
    id: Int,
    nric: String,
    onNricChange: (String) -> Unit,
    role: String,
    onRoleChange: (String) -> Unit,
    age: Int,
    onAgeChange: (Int) -> Unit,
    gender: String,
    onGenderChange: (String) -> Unit,
    weight: Double,
    onWeightChange: (Double) -> Unit,
    height: Double,
    onHeightChange: (Double) -> Unit
){
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Measurements") },
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
                    value = age.takeIf { it != 0 }?.toString() ?: "",
                    onValueChange = { newValue ->
                        onAgeChange(newValue.toIntOrNull() ?: 0)
                    },
                    label = { Text("Age") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = gender,
                    onValueChange = onGenderChange,
                    label = { Text("Gender") },
                )
                OutlinedTextField(
                    value = weight.takeIf { it != 0.0 }?.toString() ?: "",
                    onValueChange = { newValue ->
                        val parsedValue = newValue.toDoubleOrNull()
                        if (parsedValue != null) onWeightChange(parsedValue)
                    },
                    label = { Text("Weight") }
                )
                OutlinedTextField(
                    value = height.takeIf { it != 0.0 }?.toString() ?: "",
                    onValueChange = { newValue ->
                        val parsedValue = newValue.toDoubleOrNull()
                        if (parsedValue != null) onHeightChange(parsedValue)
                    },
                    label = { Text("Height") }
                )

            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onEditMeasurements(id, nric, role, age, gender, weight, height)
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