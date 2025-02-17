package com.example.fullhealthcareapplication.ui.components

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMedicationDialog(
    onDismiss: () -> Unit,
    onEditMedication: (Int,Int, Int, String, String, Double, String, String) -> Unit,
    id: Int,
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
    val context = LocalContext.current
    val timeSlots = listOf(
        "00:00",
        "01:00",
        "02:00",
        "03:00",
        "04:00",
        "05:00",
        "06:00",
        "07:00",
        "08:00",
        "09:00",
        "10:00",
        "11:00",
        "12:00",
        "13:00",
        "14:00",
        "15:00",
        "16:00",
        "17:00",
        "18:00",
        "19:00",
        "20:00",
        "21:00",
        "22:00",
        "23:00",
    )
    val types = listOf("Capsule", "Tablet", "Liquid")
    val units = listOf("mg", "mcg", "g", "ml", "%")
    val frequencies = listOf("Daily", "Weekly")
    val timeSlotsState = remember { mutableStateOf("") }
    val typesState = remember { mutableStateOf("") }
    val unitsState = remember { mutableStateOf("") }
    val frequenciesState = remember { mutableStateOf("") }
    val selectedTextState = remember { mutableStateOf("Select Time Slot") }
    val selectedText2State = remember { mutableStateOf("Select Type") }
    val selectedText3State = remember { mutableStateOf("Select Unit") }
    val selectedText4State = remember { mutableStateOf("Select Frequency") }
    val expanded = remember { mutableStateOf(false) }
    val expanded2 = remember { mutableStateOf(false) }
    val expanded3 = remember { mutableStateOf(false) }
    val expanded4 = remember { mutableStateOf(false) }
    val isFormValid = remember(timeId, name, type, measureAmount, measureUnit, frequency) {
        timeId > 0 &&
                name.isNotBlank() &&
                type.isNotBlank() &&
                measureAmount > 0 &&
                measureUnit.isNotBlank() &&
                frequency.isNotBlank()
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Medication") },
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
                        timeSlots.forEachIndexed { index, item ->
                            DropdownMenuItem(
                                text = { Text(text = item) },
                                onClick = {
                                    selectedTextState.value = item
                                    onTimeIdChange(index+1)
                                    timeSlotsState.value = item
                                    expanded.value = false
                                    Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                }
                LimitedTextField(
                    value = name,
                    onValueChange = onNameChange,
                    label = "Name",
                    maxLength = 100,
                    maxLines = 1
                )
                ExposedDropdownMenuBox(
                    expanded = expanded2.value,
                    onExpandedChange = {
                        expanded2.value = !expanded2.value
                    }
                ) {
                    TextField(
                        value = selectedText2State.value,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded2.value) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded2.value,
                        onDismissRequest = { expanded2.value = false }
                    ) {
                        types.forEachIndexed { index, item ->
                            DropdownMenuItem(
                                text = { Text(text = item) },
                                onClick = {
                                    selectedText2State.value = item
                                    onTypeChange(item)
                                    typesState.value = item
                                    expanded2.value = false
                                    Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                }
                OutlinedTextField(
                    value = measureAmount.takeIf { it != 0.0 }?.toString() ?: "",
                    onValueChange = { newValue ->
                        val parsedValue = newValue.toDoubleOrNull()
                        if (parsedValue != null) onMeasureAmountChange(parsedValue)
                    },
                    label = { Text("Measure Amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                ExposedDropdownMenuBox(
                    expanded = expanded3.value,
                    onExpandedChange = {
                        expanded3.value = !expanded3.value
                    }
                ) {
                    TextField(
                        value = selectedText3State.value,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded3.value) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded3.value,
                        onDismissRequest = { expanded3.value = false }
                    ) {
                        units.forEachIndexed { index, item ->
                            DropdownMenuItem(
                                text = { Text(text = item) },
                                onClick = {
                                    selectedText3State.value = item
                                    onMeasureUnitChange(item)
                                    unitsState.value = item
                                    expanded3.value = false
                                    Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                }
                ExposedDropdownMenuBox(
                    expanded = expanded4.value,
                    onExpandedChange = {
                        expanded4.value = !expanded4.value
                    }
                ) {
                    TextField(
                        value = selectedText4State.value,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded4.value) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded4.value,
                        onDismissRequest = { expanded4.value = false }
                    ) {
                        frequencies.forEachIndexed { index, item ->
                            DropdownMenuItem(
                                text = { Text(text = item) },
                                onClick = {
                                    selectedText4State.value = item
                                    onFrequencyChange(item)
                                    frequenciesState.value = item
                                    expanded4.value = false
                                    Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onEditMedication(id, userId, timeId, name, type, measureAmount, measureUnit, frequency)
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