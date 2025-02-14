package com.example.fullhealthcareapplication.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType
import java.time.LocalDateTime

@Composable
fun AddActivityDialog(
    onDismiss: () -> Unit,
    onAddActivity: (Int, Int, String, Double, Double, Double, Double, Double) -> Unit,
    userId: Int,
    activityCategoryId: Int,
    onActivityCategoryIdChange: (Int) -> Unit,
    timeTaken: String,
    onTimeTakenChange: (String) -> Unit,
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
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Activity") },
        text = {
            Column {
                OutlinedTextField(
                    value = activityCategoryId.takeIf { it != 0 }?.toString() ?: "",
                    onValueChange = { newValue ->
                        onActivityCategoryIdChange(newValue.toIntOrNull() ?: 0)
                    },
                    label = { Text("Activity Category Id") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = timeTaken,
                    onValueChange = onTimeTakenChange,
                    label = { Text("Time Taken") }
                )
                OutlinedTextField(
                    value = caloriesBurnt.takeIf { it != 0.0 }?.toString() ?: "",
                    onValueChange = { newValue ->
                        val parsedValue = newValue.toDoubleOrNull()
                        if (parsedValue != null) onCaloriesBurntChange(parsedValue)
                    },
                    label = { Text("Calories Burnt") }
                )
                OutlinedTextField(
                    value = stepCount.takeIf { it != 0.0 }?.toString() ?: "",
                    onValueChange = { newValue ->
                        val parsedValue = newValue.toDoubleOrNull()
                        if (parsedValue != null) onStepCountChange(parsedValue)
                    },
                    label = { Text("Step Count") }
                )
                OutlinedTextField(
                    value = distance.takeIf { it != 0.0 }?.toString() ?: "",
                    onValueChange = { newValue ->
                        val parsedValue = newValue.toDoubleOrNull()
                        if (parsedValue != null) onDistanceChange(parsedValue)
                    },
                    label = { Text("Distance") }
                )
                OutlinedTextField(
                    value = walkingSpeed.takeIf { it != 0.0 }?.toString() ?: "",
                    onValueChange = { newValue ->
                        val parsedValue = newValue.toDoubleOrNull()
                        if (parsedValue != null) onWalkingSpeedChange(parsedValue)
                    },
                    label = { Text("Walking Speed") }
                )
                OutlinedTextField(
                    value = walkingSteadiness.takeIf { it != 0.0 }?.toString() ?: "",
                    onValueChange = { newValue ->
                        val parsedValue = newValue.toDoubleOrNull()
                        if (parsedValue != null) onWalkingSteadinessChange(parsedValue)
                    },
                    label = { Text("Walking Distance") }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onAddActivity(userId, activityCategoryId, timeTaken, caloriesBurnt, stepCount, distance, walkingSpeed, walkingSteadiness)
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