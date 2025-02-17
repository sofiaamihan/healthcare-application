package com.example.fullhealthcareapplication.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StepCounterBarGraph(stepCounts: List<Int>, days: List<String>) {
    val maxSteps = stepCounts.maxOrNull() ?: 1
    val barWidth = 40.dp
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        days.forEachIndexed { index, day ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(barWidth)
            ) {
                Canvas(modifier = Modifier
                    .fillMaxHeight(0.3f)
                    .width(barWidth)) {
                    val barHeight = (stepCounts[index] / maxSteps.toFloat()) * size.height
                    drawRect(
                        color = Color(0xFF586249),
                        topLeft = Offset(0f, size.height - barHeight),
                        size = androidx.compose.ui.geometry.Size(size.width, barHeight)
                    )
                }
                Text(text = stepCounts[index].toString(), fontSize = 12.sp)
                Text(text = day, fontSize = 12.sp)
            }
        }
    }
}