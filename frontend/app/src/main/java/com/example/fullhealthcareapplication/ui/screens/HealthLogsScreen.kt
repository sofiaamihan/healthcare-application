package com.example.fullhealthcareapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.fullhealthcareapplication.ui.components.NavigationDrawer

//@Composable
//fun HealthLogsScreen(
//    toProfile: () -> Unit,
//    toHealthLogs: () -> Unit,
//    toHealthReport: () -> Unit,
//    toHome: () -> Unit,
//){
//    NavigationDrawer(
//        title = "Health Logs",
//        toProfile = {toProfile()},
//        toHealthLogs = {toHealthLogs()},
//        toHealthReport = {toHealthReport()},
//        toHome = {toHome()}
//    ){ padding ->
//        Box(
//            modifier = Modifier
//                .padding(top = 200.dp)
//                .clip(RoundedCornerShape(bottomStart = 38.dp, bottomEnd = 38.dp))
//                .height(20.dp)
//                .fillMaxWidth()
//                .background(color = MaterialTheme.colorScheme.primaryContainer)
//        )
//    }
//}