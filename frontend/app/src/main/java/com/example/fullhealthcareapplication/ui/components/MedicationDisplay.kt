package com.example.fullhealthcareapplication.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MedicationDisplay(
    time: String,
    name: String,
    type: String,
    measureAmount: Double,
    measureUnit: String,
    frequency: String,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, start = 24.dp, end = 24.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text = name,
            textAlign = TextAlign.Start,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(0.7f)
        )
        IconButton(
            onClick = onEditClick,
            modifier = Modifier
                .height(20.dp)
        ) {
            Icon(Icons.Default.Edit, contentDescription = "Edit")
        }
        IconButton(
            onClick = onDeleteClick,
            modifier = Modifier
                .height(20.dp)
        ) {
            Icon(Icons.Default.Delete, contentDescription = "Delete")
        }
    }
    Row (
        modifier = Modifier
            .padding(start = 24.dp, end = 24.dp, bottom = 5.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(
            "Time",
            fontSize = 12.sp,
            lineHeight = 14.sp
        )
        Text(
            time,
            fontSize = 12.sp,
            lineHeight = 14.sp
        )
    }
    Row (
        modifier = Modifier
            .padding(start = 24.dp, end = 24.dp, bottom = 5.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(
            "Type",
            fontSize = 12.sp,
            lineHeight = 14.sp
        )
        Text(
            type,
            fontSize = 12.sp,
            lineHeight = 14.sp
        )
    }
    Row (
        modifier = Modifier
            .padding(start = 24.dp, end = 24.dp, bottom = 5.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(
            "Amount",
            fontSize = 12.sp,
            lineHeight = 14.sp
        )
        Text(
            "$measureAmount $measureUnit",
            fontSize = 12.sp,
            lineHeight = 14.sp
        )
    }
    Row (
        modifier = Modifier
            .padding(start = 24.dp, end = 24.dp, bottom = 5.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(
            "Frequency",
            fontSize = 12.sp,
            lineHeight = 14.sp
        )
        Text(
            frequency,
            fontSize = 12.sp,
            lineHeight = 14.sp
        )
    }
}