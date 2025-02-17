package com.example.fullhealthcareapplication.ui.components

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun LimitedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    maxLength: Int,
    modifier: Modifier = Modifier,
    maxLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = {
            if (it.length <= maxLength) {
                onValueChange(it)
            }
        },
        label = { Text(label) },
        supportingText = { Text("${value.length} / $maxLength") },
        modifier = modifier,
        maxLines = maxLines,
        singleLine = maxLines == 1
    )
}