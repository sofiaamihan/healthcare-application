package com.example.fullhealthcareapplication.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fullhealthcareapplication.ui.theme.bodyFontFamily

@Composable
fun WhiteText(text: String){
    Text(
        text = text,
        fontFamily = bodyFontFamily,
        color = Color.White,
        fontSize = 28.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 5.sp,
        modifier = Modifier.padding(bottom = 24.dp),
        textAlign = TextAlign.Center
    )
}

@Composable
fun BlackText(
    text: String,
    onClick: () -> Unit
){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text = text,
            textAlign = TextAlign.Start,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        IconButton(
            onClick = onClick
        ) {
            Icon(Icons.Default.Edit, contentDescription = "Edit")
        }
    }
}

@Composable
fun BlogTitle(
    text: String
){
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, bottom = 16.dp),
        fontSize = 24.sp
    )
}