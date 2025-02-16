package com.example.fullhealthcareapplication.ui.components

import android.R.id
import android.content.res.Resources
import android.graphics.drawable.VectorDrawable
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fullhealthcareapplication.R

@Composable
fun DiscoverButton(
    banner: Int,
    title: String,
    summary: String,
    description: String,
    contentCategoryId: Int,
    role: String,
    toBlog: () -> Unit
){
    Card (
        modifier = Modifier
            .height(200.dp)
            .clickable{toBlog()}
            .fillMaxWidth(0.8f),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondaryContainer),
    ){
        Column (
            modifier = Modifier
                .fillMaxWidth()
        ){
            Image(
                painter = painterResource(id = banner),
                contentDescription = "Banner",
                modifier = Modifier
                    .height(125.dp),
                contentScale = ContentScale.FillWidth
            )
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                lineHeight = 14.sp,
                modifier = Modifier
                    .padding(top = 10.dp, start = 10.dp, end = 10.dp)
            )
            Text(
                text = summary,
                fontSize = 11.sp,
                lineHeight = 11.sp,
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
            )
        }
    }
}