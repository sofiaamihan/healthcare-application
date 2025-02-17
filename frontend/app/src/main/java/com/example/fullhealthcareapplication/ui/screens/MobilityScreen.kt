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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.style.TextAlign
import com.example.fullhealthcareapplication.data.viewmodel.SensorViewModel
import com.example.fullhealthcareapplication.ui.components.AppBar
import com.example.fullhealthcareapplication.ui.components.Graph
import com.example.fullhealthcareapplication.ui.components.GraphAppearance
import kotlin.random.Random

@Composable
fun MobilityScreen(
    toHome: () -> Unit,
    sensorViewModel: SensorViewModel
){
    val accValues by sensorViewModel.accelerometerData.collectAsState()
    val gyroValues by sensorViewModel.gyroscopeData.collectAsState()


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
                        text = "Monitor Your Mobility",
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
        LazyColumn {
            item{
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 32.dp, top = 10.dp, bottom = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = "Accelerometer",
                        textAlign = TextAlign.Start,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.fillMaxWidth(0.7f)
                    )
                }
            }
            item{
                Graph(
                    modifier = Modifier.fillMaxSize(),
                    xValues = (0..2).map { it + 1 },
                    yValues = (-10..10).map { it },
                    points = accValues,
                    paddingSpace = 16.dp,
                    verticalStep = 1,
                    graphAppearance = GraphAppearance(
                        graphColor = MaterialTheme.colorScheme.secondary,
                        graphAxisColor = Color.Black,
                        graphThickness = 4f,
                        iscolorAreaUnderChart = true,
                        colorAreaUnderChart = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
                        isCircleVisible = true,
                        circleColor = MaterialTheme.colorScheme.primary,
                        backgroundColor = MaterialTheme.colorScheme.background)
                )
            }
            item{
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 32.dp, top = 10.dp, bottom = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = "Gyroscope",
                        textAlign = TextAlign.Start,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.fillMaxWidth(0.7f)
                    )
                }
            }
            item{
                Graph(
                    modifier = Modifier
                        .padding(bottom = 64.dp)
                        .fillMaxSize(),
                    xValues = (0..2).map { it + 1 },
                    yValues = (-10..10).map { it },
                    points = gyroValues,
                    paddingSpace = 16.dp,
                    verticalStep = 1,
                    graphAppearance = GraphAppearance(
                        graphColor = MaterialTheme.colorScheme.secondary,
                        graphAxisColor = Color.Black,
                        graphThickness = 4f,
                        iscolorAreaUnderChart = true,
                        colorAreaUnderChart = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
                        isCircleVisible = true,
                        circleColor = MaterialTheme.colorScheme.primary,
                        backgroundColor = MaterialTheme.colorScheme.background)
                )
            }
        }
//        Row (
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 10.dp, bottom = 10.dp),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ){
//            Text(
//                text = "Accelerometer",
//                textAlign = TextAlign.Start,
//                fontSize = 16.sp,
//                fontWeight = FontWeight.SemiBold,
//                modifier = Modifier.fillMaxWidth(0.7f)
//            )
//        }
//        Graph(
//            modifier = Modifier.fillMaxSize(),
//            xValues = (0..2).map { it + 1 },
//            yValues = (-10..10).map { it },
//            points = accValues,
//            paddingSpace = 16.dp,
//            verticalStep = 1,
//            graphAppearance = GraphAppearance(
//                graphColor = MaterialTheme.colorScheme.secondary,
//                graphAxisColor = Color.Black,
//                graphThickness = 4f,
//                iscolorAreaUnderChart = true,
//                colorAreaUnderChart = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
//                isCircleVisible = true,
//                circleColor = MaterialTheme.colorScheme.primary,
//                backgroundColor = MaterialTheme.colorScheme.background)
//        )
//        Row (
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 10.dp, bottom = 10.dp),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ){
//            Text(
//                text = "Gyroscope",
//                textAlign = TextAlign.Start,
//                fontSize = 16.sp,
//                fontWeight = FontWeight.SemiBold,
//                modifier = Modifier.fillMaxWidth(0.7f)
//            )
//        }
//        Graph(
//            modifier = Modifier.fillMaxSize(),
//            xValues = (0..2).map { it + 1 },
//            yValues = (-10..10).map { it },
//            points = gyroValues,
//            paddingSpace = 16.dp,
//            verticalStep = 1,
//            graphAppearance = GraphAppearance(
//                graphColor = MaterialTheme.colorScheme.secondary,
//                graphAxisColor = Color.Black,
//                graphThickness = 4f,
//                iscolorAreaUnderChart = true,
//                colorAreaUnderChart = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
//                isCircleVisible = true,
//                circleColor = MaterialTheme.colorScheme.primary,
//                backgroundColor = MaterialTheme.colorScheme.background)
//        )
//
    }
}