package com.example.fullhealthcareapplication.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.fullhealthcareapplication.R
import com.example.fullhealthcareapplication.data.factory.HealthServiceViewModelFactory
import com.example.fullhealthcareapplication.data.preferences.TokenDataStore
import com.example.fullhealthcareapplication.data.viewmodel.GetActivitiesViewModel
import com.example.fullhealthcareapplication.data.viewmodel.GetAllActivitiesViewModel
import com.example.fullhealthcareapplication.data.viewmodel.GetAllCategoriesViewModel
import com.example.fullhealthcareapplication.data.viewmodel.GetAllMedicationsViewModel
import com.example.fullhealthcareapplication.data.viewmodel.GetAllTimesViewModel
import com.example.fullhealthcareapplication.data.viewmodel.GetCategoriesViewModel
import com.example.fullhealthcareapplication.data.viewmodel.GetMedicationsViewModel
import com.example.fullhealthcareapplication.data.viewmodel.GetTimesViewModel
import com.example.fullhealthcareapplication.data.viewmodel.GetUserIdViewModel
import com.example.fullhealthcareapplication.ui.components.BigButton
import com.example.fullhealthcareapplication.ui.components.NavigationDrawer
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@SuppressLint("CoroutineCreationDuringComposition")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    toProfile: () -> Unit,
    toHealthLogs: () -> Unit,
    toHealthReport: () -> Unit,
    toHome: () -> Unit,
    toPhysical: () -> Unit,
    toMobility: () -> Unit,
    toMedication: () -> Unit,
    healthServiceViewModelFactory: HealthServiceViewModelFactory,
    tokenDataStore: TokenDataStore
){
    var search = remember { mutableStateOf("") }
    val buttons = listOf(
        Triple("Physical", R.drawable.physical, toPhysical),
        Triple("Mobility", R.drawable.mobility, toMobility),
        Triple("Medicine", R.drawable.medicine, toMedication)
    )

    // Filter buttons based on the search input
    val filteredButtons = buttons.filter {
        it.first.contains(search.value, ignoreCase = true)
    }

    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
    val dateKeyFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val dateKey = LocalDateTime.now().format(dateKeyFormatter)
    val current = LocalDateTime.now().format(formatter)
    val getCategoriesViewModel: GetCategoriesViewModel = viewModel(factory = healthServiceViewModelFactory)
    val getAllCategoriesViewModel: GetAllCategoriesViewModel = viewModel(factory = healthServiceViewModelFactory)
    val getTimesViewModel: GetTimesViewModel = viewModel(factory = healthServiceViewModelFactory)
    val getAllTimesViewModel: GetAllTimesViewModel = viewModel(factory = healthServiceViewModelFactory)
    val getMedicationsViewModel: GetMedicationsViewModel = viewModel(factory = healthServiceViewModelFactory)
    val getAllMedicationsViewModel: GetAllMedicationsViewModel = viewModel(factory = healthServiceViewModelFactory)
    val getActivitiesViewModel: GetActivitiesViewModel = viewModel(factory = healthServiceViewModelFactory)
    val getAllActivitiesViewModel: GetAllActivitiesViewModel = viewModel(factory = healthServiceViewModelFactory)

    val remoteState = getCategoriesViewModel.state
    val localState = getAllCategoriesViewModel.state
    val remoteTState = getTimesViewModel.state
    val localTState = getAllTimesViewModel.state
    val remoteMState = getMedicationsViewModel.state
    val localMState = getAllMedicationsViewModel.state
    val remoteAState = getActivitiesViewModel.state
    val localAState = getAllActivitiesViewModel.state
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        getCategoriesViewModel.getCategories()
        getTimesViewModel.getTimes()
        getMedicationsViewModel.getMedications(tokenDataStore.getId.first()?.toInt() ?: 0)
        getActivitiesViewModel.getActivities(tokenDataStore.getId.first()?.toInt() ?: 0,
            dateKey.toString()
        )
        Log.d("Launch", "${remoteState.categoryList}")
        Log.d("Launch T", "${remoteTState.timeList}")
        Log.d("Launch M", "${remoteMState.medicationList}")
        Log.d("Launch A", "${remoteAState.activityList}")
    }
    LaunchedEffect(localState){
        // TODO - you have to reload this page to properly get
        // TODO - if i use scope.launch this thing goes on infinitely
        getAllCategoriesViewModel.getAllCategories()
        getAllTimesViewModel.getAllTimes()
        getAllMedicationsViewModel.getAllMedications(tokenDataStore.getId.first()?.toInt() ?: 0)
        getAllActivitiesViewModel.getAllActivities(tokenDataStore.getId.first()?.toInt() ?: 0,
            dateKey.toString()
        )
        Log.d("Scope", "${localState.cachedCategoryList}")
        Log.d("Scope2", "${localTState.cachedTimeList}")
        Log.d("Scope3", "${localMState.cachedMedicationList}")
        Log.d("Scope4", "${localAState.cachedActivityList}")
    }

    NavigationDrawer(
        title = "Welcome Home",
        toProfile = {toProfile()},
        toHealthLogs = {toHealthLogs()},
        toHealthReport = {toHealthReport()},
        toHome = {toHome()}
    ){ padding ->
        Box(
            modifier = Modifier
                .padding(top = 200.dp)
                .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                .fillMaxWidth()
                .height(80.dp)
                .background(color = MaterialTheme.colorScheme.secondaryContainer),
        ){
            Column (
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(0.9f),
                    value = search.value,
                    onValueChange = { search.value = it },
                    label = { Text("Search") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                    shape = RoundedCornerShape(32.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background
                    ),
                    singleLine = true
                )
            }
        }
        Text(
            text = "Today, $current",
            modifier = Modifier
                .padding(top = 290.dp, start = 32.dp)
        )
        Column (
            modifier = Modifier
                .padding(top = 320.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ){
            filteredButtons.forEach { (label, icon, action) ->
                Row (
                    modifier = Modifier.padding(bottom = 10.dp)
                ){
                    BigButton(label, icon, toSensorScreen = { action() })
                }
            }
        }

    }

}