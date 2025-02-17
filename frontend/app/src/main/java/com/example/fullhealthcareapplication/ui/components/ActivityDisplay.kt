package com.example.fullhealthcareapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ActivityDisplay(
    category: String,
    caloriesBurnt: Double,
    stepCount: Double,
    distance: Double,
    walkingSpeed: Double,
    walkingSteadiness: Double
){
    Card (
        modifier = Modifier
            .padding(top = 16.dp, bottom = 16.dp)
            .fillMaxWidth(0.8f),
        shape = RoundedCornerShape(28.dp)
    ){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
        ){
            Column (
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = category,
                        textAlign = TextAlign.Start,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.fillMaxWidth(0.7f)
                    )
                    IconButton(
                        onClick = {},
                        modifier = Modifier
                            .height(20.dp)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(
                        onClick = {},
                        modifier = Modifier
                            .height(20.dp)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
                if(caloriesBurnt != 0.0){
                    Row (
                        modifier = Modifier
                            .padding(bottom = 5.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(
                            "Calories Burnt",
                            fontSize = 12.sp,
                            lineHeight = 14.sp
                        )
                        Text(
                            "$caloriesBurnt",
                            fontSize = 12.sp,
                            lineHeight = 14.sp
                        )
                    }
                }
                if(stepCount != 0.0){
                    Row (
                        modifier = Modifier
                            .padding(bottom = 5.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(
                            "Step Count",
                            fontSize = 12.sp,
                            lineHeight = 14.sp
                        )
                        Text(
                            "$stepCount",
                            fontSize = 12.sp,
                            lineHeight = 14.sp
                        )
                    }
                }
                if(distance != 0.0){
                    Row (
                        modifier = Modifier
                            .padding(bottom = 5.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(
                            "Distance",
                            fontSize = 12.sp,
                            lineHeight = 14.sp
                        )
                        Text(
                            "$distance",
                            fontSize = 12.sp,
                            lineHeight = 14.sp
                        )
                    }
                }
                if(walkingSpeed != 0.0){
                    Row (
                        modifier = Modifier
                            .padding(bottom = 5.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(
                            "Walking Speed",
                            fontSize = 12.sp,
                            lineHeight = 14.sp
                        )
                        Text(
                            "$walkingSpeed",
                            fontSize = 12.sp,
                            lineHeight = 14.sp
                        )
                    }
                }
                if(walkingSteadiness != 0.0){
                    Row (
                        modifier = Modifier
                            .padding(bottom = 5.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(
                            "Walking Steadiness",
                            fontSize = 12.sp,
                            lineHeight = 14.sp
                        )
                        Text(
                            "$walkingSteadiness",
                            fontSize = 12.sp,
                            lineHeight = 14.sp
                        )
                    }
                }
            }
        }
    }
}