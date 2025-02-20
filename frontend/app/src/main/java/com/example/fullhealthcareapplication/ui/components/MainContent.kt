package com.example.fullhealthcareapplication.ui.components

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.fullhealthcareapplication.ui.graphs.HomeNavGraph
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fullhealthcareapplication.data.preferences.TokenDataStore
import com.example.fullhealthcareapplication.data.factory.DiscoverServiceViewModelFactory
import com.example.fullhealthcareapplication.data.factory.HealthServiceViewModelFactory
import com.example.fullhealthcareapplication.data.factory.UserInfoViewModelFactory
import com.example.fullhealthcareapplication.data.viewmodel.AddActivityViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fullhealthcareapplication.data.viewmodel.SensorViewModel
import com.example.fullhealthcareapplication.ui.components.BottomNavigationScreen
import kotlinx.coroutines.flow.first

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainContent(
    navController: NavHostController = rememberNavController(),
    discoverServiceViewModelFactory: DiscoverServiceViewModelFactory,
    userInfoViewModelFactory: UserInfoViewModelFactory,
    healthServiceViewModelFactory: HealthServiceViewModelFactory,
    tokenDataStore: TokenDataStore,
    toSignOut: () -> Unit,
    toHome: () -> Unit,
    sensorViewModel: SensorViewModel
){
    var search = remember { mutableStateOf("") }
    val addActivityViewModel: AddActivityViewModel = viewModel(factory = healthServiceViewModelFactory)
    val showAddActivityDialog = remember { mutableStateOf(false) }

    val userId = remember { mutableIntStateOf(0) }
    val activityCategoryId = remember { mutableIntStateOf(0) }
    val caloriesBurnt = remember { mutableDoubleStateOf(0.0) }
    val stepCount = remember { mutableDoubleStateOf(0.0) }
    val distance = remember { mutableDoubleStateOf(0.0) }
    val walkingSpeed = remember { mutableDoubleStateOf(0.0) }
    val walkingSteadiness = remember { mutableDoubleStateOf(0.0) }

    LaunchedEffect(Unit) {
        userId.intValue = tokenDataStore.getId.first()?.toInt()!!
    }

    Scaffold(
        bottomBar = { BottomBar(navController = navController)},
        floatingActionButton = {
            FloatingActionButton (
                onClick = {
                    showAddActivityDialog.value = true
                },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier
                    .offset(y = 50.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) {
        HomeNavGraph(
            navController = navController,
            discoverServiceViewModelFactory = discoverServiceViewModelFactory,
            userInfoViewModelFactory = userInfoViewModelFactory,
            tokenDataStore = tokenDataStore,
            toSignOut = toSignOut,
            healthServiceViewModelFactory = healthServiceViewModelFactory,
            sensorViewModel = sensorViewModel
        )
    }

    if(showAddActivityDialog.value){
        AddActivityDialog(
            onDismiss = { showAddActivityDialog.value = false },
            onAddActivity = { userId, activityCategoryId, timeTaken, caloriesBurnt, stepCount, distance, walkingSpeed, walkingSteadiness ->
                addActivityViewModel.addActivity(userId, activityCategoryId, timeTaken, caloriesBurnt, stepCount, distance, walkingSpeed, walkingSteadiness)
                showAddActivityDialog.value = false
                toHome()
            },
            userId = userId.intValue,
            activityCategoryId = activityCategoryId.intValue,
            onActivityCategoryIdChange = { activityCategoryId.intValue = it },
            caloriesBurnt = caloriesBurnt.doubleValue,
            onCaloriesBurntChange = { caloriesBurnt.doubleValue = it },
            stepCount = stepCount.doubleValue,
            onStepCountChange = { stepCount.doubleValue = it },
            distance = distance.doubleValue,
            onDistanceChange = { distance.doubleValue = it },
            walkingSpeed = walkingSpeed.doubleValue,
            onWalkingSpeedChange = { walkingSpeed.doubleValue = it },
            walkingSteadiness = walkingSteadiness.doubleValue,
            onWalkingSteadinessChange = { walkingSteadiness.doubleValue = it },

        )
    }
}


@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomNavigationScreen.Home,
        BottomNavigationScreen.Discover,
    )
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination

    val bottomBarDestination = screens.any { it.route == currentDestination?.route  }
    if(bottomBarDestination){
        NavigationBar {
            screens.forEach{ screen ->
                AddItem (
                    screen = screen,
                    currentDestination = currentDestination,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomNavigationScreen,
    currentDestination: NavDestination?,
    navController: NavHostController
){
    NavigationBarItem(
        label = {
            Text(text = screen.title)
        },
        icon = {
            Icon(
                imageVector = screen.icon,
                contentDescription = "Navigation Icon"
            )
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        }
    )
}