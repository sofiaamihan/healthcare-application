package com.example.fullhealthcareapplication.ui.components

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditActivityDialog(
    onDismiss: () -> Unit,
    onEditActivity: (Int, Int, Int, String, Double, Double, Double, Double, Double) -> Unit,
    id: Int,
    userId: Int,
    date: String,
    activityCategoryId: Int,
    onActivityCategoryIdChange: (Int) -> Unit,
    caloriesBurnt: Double,
    onCaloriesBurntChange: (Double) -> Unit,
    stepCount: Double,
    onStepCountChange: (Double) -> Unit,
    distance: Double,
    onDistanceChange: (Double) -> Unit,
    walkingSpeed: Double,
    onWalkingSpeedChange: (Double) -> Unit,
    walkingSteadiness: Double,
    onWalkingSteadinessChange: (Double) -> Unit
){
    val context = LocalContext.current
    val categories = listOf("Swim", "Bike", "Outdoor Run", "Indoor Run", "Strength", "Walk", "Yoga", "Cardio", "Other")
    val categoriesState = remember { mutableStateOf("") }
    val selectedTextState = remember {
        mutableStateOf(categories.getOrElse(activityCategoryId - 1) { "Select Category" })
    }
    val expanded = remember { mutableStateOf(false) }
    val isFormValid = remember(activityCategoryId){
        activityCategoryId > 0
    }
    val date = LocalDateTime.now().toString()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Activity") },
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
                                    onActivityCategoryIdChange(index+1)
                                    categoriesState.value = item
                                    expanded.value = false
                                    Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                }
                OutlinedTextField(
                    value = caloriesBurnt.takeIf { it != 0.0 }?.toString() ?: "",
                    onValueChange = { newValue ->
                        val parsedValue = newValue.toDoubleOrNull()
                        if (parsedValue != null) onCaloriesBurntChange(parsedValue)
                    },
                    label = { Text("Calories Burnt (kcal)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = stepCount.takeIf { it != 0.0 }?.toString() ?: "",
                    onValueChange = { newValue ->
                        val parsedValue = newValue.toDoubleOrNull()
                        if (parsedValue != null) onStepCountChange(parsedValue)
                    },
                    label = { Text("Step Count") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = distance.takeIf { it != 0.0 }?.toString() ?: "",
                    onValueChange = { newValue ->
                        val parsedValue = newValue.toDoubleOrNull()
                        if (parsedValue != null) onDistanceChange(parsedValue)
                    },
                    label = { Text("Distance (m)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = walkingSpeed.takeIf { it != 0.0 }?.toString() ?: "",
                    onValueChange = { newValue ->
                        val parsedValue = newValue.toDoubleOrNull()
                        if (parsedValue != null) onWalkingSpeedChange(parsedValue)
                    },
                    label = { Text("Walking Speed (m/s)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = walkingSteadiness.takeIf { it != 0.0 }?.toString() ?: "",
                    onValueChange = { newValue ->
                        val parsedValue = newValue.toDoubleOrNull()
                        if (parsedValue != null) onWalkingSteadinessChange(parsedValue)
                    },
                    label = { Text("Walking Steadiness") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onEditActivity(id, userId, activityCategoryId, date, caloriesBurnt, stepCount, distance, walkingSpeed, walkingSteadiness)
                },
                enabled = isFormValid
            ) {
                Text("Edit Activity")
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