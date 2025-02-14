package com.example.fullhealthcareapplication.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavigationScreen (
    val route: String,
    val title: String,
    val icon: ImageVector
){
    object Home: BottomNavigationScreen(
        route = "HOME",
        title = "HOME",
        icon = Icons.Filled.Home
    )

    object Discover: BottomNavigationScreen(
        route = "DISCOVER",
        title = "DISCOVER",
        icon = Icons.Filled.Info
    )
}