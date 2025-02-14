package com.example.fullhealthcareapplication.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.fullhealthcareapplication.R

@Composable
@Preview(showSystemUi = true)
fun Background(
){
    Image(
        painter = painterResource(R.drawable.background),
        contentDescription = "Background",
        contentScale = ContentScale.FillBounds,
        modifier = Modifier.fillMaxSize()
    )
}