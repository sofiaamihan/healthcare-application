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
fun AddMedicationDialog(
    onDismiss: () -> Unit,
    onAddMedication: (Int, Int, String, String, Double, String, String) -> Unit,
    userId: Int,
    timeId: Int,
    onTimeIdChange: (Int) -> Unit,
    name: String,
    onNameChange: (String) -> Unit,
    type: String,
    onTypeChange: (String) -> Unit,
    measureAmount: Double,
    onMeasureAmountChange: (Double) -> Unit,
    measureUnit: String,
    onMeasureUnitChange: (String) -> Unit,
    frequency: String,
    onFrequencyChange: (String) -> Unit
){
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Medication") },
        text = {
            Column {
                OutlinedTextField(
                    value = timeId.takeIf { it != 0 }?.toString() ?: "",
                    onValueChange = { newValue ->
                        onTimeIdChange(newValue.toIntOrNull() ?: 0)
                    },
                    label = { Text("Time Id") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = name,
                    onValueChange = onNameChange,
                    label = { Text("Name") }
                )
                OutlinedTextField(
                    value = type,
                    onValueChange = onTypeChange,
                    label = { Text("Type") }
                )
                OutlinedTextField(
                    value = measureAmount.takeIf { it != 0.0 }?.toString() ?: "",
                    onValueChange = { newValue ->
                        val parsedValue = newValue.toDoubleOrNull()
                        if (parsedValue != null) onMeasureAmountChange(parsedValue)
                    },
                    label = { Text("Measure Amount") }
                )
                OutlinedTextField(
                    value = measureUnit,
                    onValueChange = onMeasureUnitChange,
                    label = { Text("Measure Unit") }
                )
                OutlinedTextField(
                    value = frequency,
                    onValueChange = onFrequencyChange,
                    label = { Text("Frequency") }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onAddMedication(userId, timeId, name, type, measureAmount, measureUnit, frequency)
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