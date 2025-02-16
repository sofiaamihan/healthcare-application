package com.example.fullhealthcareapplication.ui.screens

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
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fullhealthcareapplication.ui.components.AppBar
import com.example.fullhealthcareapplication.ui.components.Graph
import com.example.fullhealthcareapplication.ui.components.GraphAppearance
import kotlin.random.Random

@Composable
fun MobilityScreen(
    toHome: () -> Unit
){
    val horizontalGradientBrush = Brush.horizontalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.tertiaryContainer
        )
    )
    val mobilityDescription = "Walking Steadiness is your ability to walk under control in a variety of conditions. By measuring the way a person walks, you can get a good idea of how balanced and efficient it is. Exercises such as Tai Chi and Yoga are beneficial for improving strength and balance."
    AppBar(
        title = "Mobility",
        toHome = {toHome()}
    )
    Box(
        modifier = Modifier
            .padding(top = 110.dp)
            .clip(RoundedCornerShape(bottomStart = 38.dp, bottomEnd = 38.dp))
            .height(20.dp)
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.secondaryContainer)
    )
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 150.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Card (
            modifier = Modifier
                .height(160.dp)
                .fillMaxWidth(0.8f),
            shape = RoundedCornerShape(32.dp)
        ){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(horizontalGradientBrush, alpha = 0.5f)
                    .height(100.dp)
                    .fillMaxWidth(0.8f)
            ){
                Column (
                    modifier = Modifier
                        .padding(start = 30.dp, end = 30.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(
                        text = "About Mobility",
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = mobilityDescription,
                        fontSize = 12.sp,
                        lineHeight = 14.sp
                    )
                }
            }
        }
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, start = 24.dp, end = 24.dp, bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "Today",
                textAlign = TextAlign.Start,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Row (
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp, bottom = 5.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                "Walking Speed",
                fontSize = 12.sp,
                lineHeight = 14.sp
            )
            Text(
                "200",
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
                "Walking Steadiness",
                fontSize = 12.sp,
                lineHeight = 14.sp
            )
            Text(
                "4000",
                fontSize = 12.sp,
                lineHeight = 14.sp
            )
        }
        // ---------- TEMP GRAPH ----------
        val yStep = 50
        val random = Random
        val points = (0..9).map {
            var num = random.nextInt(350)
            if (num <= 50)
                num += 100
            num.toFloat()
        }
        Box(
            modifier = Modifier.fillMaxSize().background(Color.DarkGray)
        ) {
            Graph(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp),
                xValues = (0..9).map { it + 1 },
                yValues = (0..6).map { (it + 1) * yStep },
                points = points,
                paddingSpace = 16.dp,
                verticalStep = yStep,
                graphAppearance = GraphAppearance(
                    Color.White,
                    MaterialTheme.colorScheme.primary,
                    1f,
                    true,
                    MaterialTheme.colorScheme.primary,
                    false,
                    MaterialTheme.colorScheme.secondary,
                    MaterialTheme.colorScheme.background
                )
            )
        }
        // ---------- TEMP GRAPH ----------
    }
}