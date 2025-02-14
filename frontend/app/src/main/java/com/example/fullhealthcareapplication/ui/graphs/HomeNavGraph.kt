package com.example.fullhealthcareapplication.ui.graphs

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.fullhealthcareapplication.data.preferences.TokenDataStore
import com.example.fullhealthcareapplication.data.factory.DiscoverServiceViewModelFactory
import com.example.fullhealthcareapplication.data.factory.HealthServiceViewModelFactory
import com.example.fullhealthcareapplication.data.factory.UserInfoViewModelFactory
import com.example.fullhealthcareapplication.ui.components.BottomNavigationScreen
import com.example.fullhealthcareapplication.ui.screens.BlogScreen
import com.example.fullhealthcareapplication.ui.screens.DiscoverScreen
import com.example.fullhealthcareapplication.ui.screens.HealthLogsScreen
import com.example.fullhealthcareapplication.ui.screens.HealthReportScreen
import com.example.fullhealthcareapplication.ui.screens.HomeScreen
import com.example.fullhealthcareapplication.ui.screens.MedicationScreen
import com.example.fullhealthcareapplication.ui.screens.MobilityScreen
import com.example.fullhealthcareapplication.ui.screens.PhysicalScreen
import com.example.fullhealthcareapplication.ui.screens.ProfileScreen

@SuppressLint("ContextCastToActivity")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeNavGraph(
    navController: NavHostController,
    discoverServiceViewModelFactory: DiscoverServiceViewModelFactory,
    userInfoViewModelFactory: UserInfoViewModelFactory,
    healthServiceViewModelFactory: HealthServiceViewModelFactory,
    tokenDataStore: TokenDataStore,
    toSignOut: () -> Unit
    ){
    NavHost(
        navController = navController,
        route = Graph.HOME,
        startDestination = BottomNavigationScreen.Home.route
    ) {
        composable(route = BottomNavigationScreen.Home.route){
            HomeScreen(
                toProfile = {
                    navController.navigate(DrawerScreen.Profile.route)
                },
                toHealthLogs = {
                    navController.navigate(DrawerScreen.HealthLogs.route)
                },
                toHealthReport = {
                    navController.navigate(DrawerScreen.HealthReport.route)
                },
                toHome = {
                    navController.navigate(BottomNavigationScreen.Home.route)
                },
                toPhysical = {
                    navController.navigate(SensorScreen.Physical.route)
                },
                toMobility = {
                    navController.navigate(SensorScreen.Mobility.route)
                },
                toMedication = {
                    navController.navigate(SensorScreen.Medication.route)
                },
                healthServiceViewModelFactory = healthServiceViewModelFactory,
                tokenDataStore = tokenDataStore
            )
        }
        composable(route = BottomNavigationScreen.Discover.route){
            DiscoverScreen(
                toProfile = {
                    navController.navigate(DrawerScreen.Profile.route)
                },
                toHealthLogs = {
                    navController.navigate(DrawerScreen.HealthLogs.route)
                },
                toHealthReport = {
                    navController.navigate(DrawerScreen.HealthReport.route)
                },
                toHome = {
                    navController.navigate(BottomNavigationScreen.Home.route)
                },
                viewModelFactory = discoverServiceViewModelFactory,
            ){ id, title, summary, description, contentCategoryId ->
                navController.navigate("${DiscoverScreen.Blog.route}/$id/$title/$summary/$description/$contentCategoryId")
            }
        }
        composable(route = "${DiscoverScreen.Blog.route}/{id}/{title}/{summary}/{description}/{contentCategoryId}"){ backStackEntry ->
            val id: String = backStackEntry.arguments?.getString("id") ?: "0"
            val title: String = backStackEntry.arguments?.getString("title") ?: ""
            val summary: String = backStackEntry.arguments?.getString("summary") ?: ""
            val description: String = backStackEntry.arguments?.getString("description") ?: ""
            val contentCategoryId: String = backStackEntry.arguments?.getString("contentCategoryId") ?: "0"
            BlogScreen(
                id = id.toInt(),
                title = title,
                summary = summary,
                description = description,
                contentCategoryId = contentCategoryId.toInt(),
                viewModelFactory = discoverServiceViewModelFactory,
                toDiscoverScreen = {
                    navController.navigate(BottomNavigationScreen.Discover.route)
                }
            )
        }
        composable(route = DrawerScreen.Profile.route){
            ProfileScreen(
                toProfile = {
                    navController.navigate(DrawerScreen.Profile.route)
                },
                toHealthLogs = {
                    navController.navigate(DrawerScreen.HealthLogs.route)
                },
                toHealthReport = {
                    navController.navigate(DrawerScreen.HealthReport.route)
                },
                toHome = {
                    navController.navigate(BottomNavigationScreen.Home.route)
                },
                tokenDataStore = tokenDataStore,
                viewModelFactory = userInfoViewModelFactory,
                toSignOut = toSignOut,
                healthServiceViewModelFactory = healthServiceViewModelFactory
            )
        }
        composable(route = DrawerScreen.HealthLogs.route){
            HealthLogsScreen(
                toProfile = {
                    navController.navigate(DrawerScreen.Profile.route)
                },
                toHealthLogs = {
                    navController.navigate(DrawerScreen.HealthLogs.route)
                },
                toHealthReport = {
                    navController.navigate(DrawerScreen.HealthReport.route)
                },
                toHome = {
                    navController.navigate(BottomNavigationScreen.Home.route)
                }
            )
        }
        composable(route = DrawerScreen.HealthReport.route){
            HealthReportScreen()
        }
        composable(route = SensorScreen.Physical.route){
            PhysicalScreen(
                toHome = {
                    navController.navigate(BottomNavigationScreen.Home.route)
                }
            )
        }
        composable(route = SensorScreen.Mobility.route){
            MobilityScreen(
                toHome = {
                    navController.navigate(BottomNavigationScreen.Home.route)
                }
            )
        }
        composable(route = SensorScreen.Medication.route){
            MedicationScreen(
                toHome = {
                    navController.navigate(BottomNavigationScreen.Home.route)
                },
                healthServiceViewModelFactory = healthServiceViewModelFactory,
                tokenDataStore = tokenDataStore
            )
        }
        // Include nested nav graph for screens only accessed from home screen or something
    }
}

sealed class DrawerScreen(val route: String){
    object Profile: DrawerScreen(route = "PROFILE")
    object HealthLogs: DrawerScreen(route = "HEALTH_LOGS")
    object HealthReport: DrawerScreen(route = "HEALTH_REPORT")
}

sealed class DiscoverScreen(val route: String){
    object Blog: DiscoverScreen(route = "BLOG")
}

sealed class SensorScreen(val route: String){
    object Physical: SensorScreen(route = "PHYSICAL")
    object Mobility: SensorScreen(route = "MOBILITY")
    object Medication: SensorScreen(route = "MEDICATION")
}