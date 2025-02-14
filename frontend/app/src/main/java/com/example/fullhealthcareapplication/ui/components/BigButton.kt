package com.example.fullhealthcareapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fullhealthcareapplication.R

@Composable
fun BigButton(
    label: String,
    icon: Int,
    toSensorScreen: () -> Unit
){
    val horizontalGradientBrush = Brush.horizontalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.tertiaryContainer
        )
    )
    Card(
        modifier = Modifier
            .height(120.dp)
            .clickable{toSensorScreen()}
            .fillMaxWidth(0.8f),
        shape = RoundedCornerShape(32.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(horizontalGradientBrush, alpha = 0.5f)
                .height(100.dp)
                .fillMaxWidth(0.8f),
        ){
            Row (
                modifier = Modifier
                    .padding(start = 40.dp, end = 40.dp)
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Card ( // The Icon Box
                    modifier = Modifier
                        .height(50.dp)
                        .width(50.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 12.dp
                    ),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondaryContainer)
                ){
                    Column (
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ){
                        Icon(
                            painter = painterResource(icon),
                            contentDescription = "Icon",
                            modifier = Modifier
                                .width(40.dp)
                                .height(40.dp)
                        )
                    }
                }
                Text(label)
            }
        }

    }




}