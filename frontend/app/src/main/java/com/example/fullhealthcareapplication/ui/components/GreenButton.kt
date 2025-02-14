package com.example.fullhealthcareapplication.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GreenButton(
    text: String,
    onClick: () -> Unit
){
    Button(
        onClick = { onClick() },
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .padding(top = 28.dp)
    ) {
        Text(text)
    }
}