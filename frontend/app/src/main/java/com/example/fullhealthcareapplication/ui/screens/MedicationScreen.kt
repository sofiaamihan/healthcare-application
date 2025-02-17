package com.example.fullhealthcareapplication.ui.screens

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fullhealthcareapplication.data.entity.Activity
import com.example.fullhealthcareapplication.data.entity.Medication
import com.example.fullhealthcareapplication.data.factory.HealthServiceViewModelFactory
import com.example.fullhealthcareapplication.data.preferences.TokenDataStore
import com.example.fullhealthcareapplication.data.viewmodel.AddMedicationViewModel
import com.example.fullhealthcareapplication.data.viewmodel.DeleteMedicationViewModel
import com.example.fullhealthcareapplication.data.viewmodel.EditMedicationViewModel
import com.example.fullhealthcareapplication.data.viewmodel.GetAllMedicationsViewModel
import com.example.fullhealthcareapplication.data.viewmodel.GetAllTimesViewModel
import com.example.fullhealthcareapplication.data.viewmodel.GetMedicationsViewModel
import com.example.fullhealthcareapplication.ui.components.AddMedicationDialog
import com.example.fullhealthcareapplication.ui.components.AppBar
import com.example.fullhealthcareapplication.ui.components.DeleteMedicationDialog
import com.example.fullhealthcareapplication.ui.components.EditMedicationDialog
import com.example.fullhealthcareapplication.ui.components.MedicationDisplay
import kotlinx.coroutines.flow.first
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MedicationScreen(
    toHome: () -> Unit,
    healthServiceViewModelFactory: HealthServiceViewModelFactory,
    tokenDataStore: TokenDataStore
){
    val showMedicationModal = remember { mutableStateOf(false) }
    val userId = remember { mutableIntStateOf(0) }
    val timeId = remember { mutableIntStateOf(0) }
    val name = remember { mutableStateOf("") }
    val type = remember { mutableStateOf("") }
    val measureAmount = remember { mutableDoubleStateOf(0.0) }
    val measureUnit = remember { mutableStateOf("") }
    val frequency = remember { mutableStateOf("") }

    val getAllTimesViewModel: GetAllTimesViewModel = viewModel(factory = healthServiceViewModelFactory)
    val timeState = getAllTimesViewModel.state

    val addMedicationViewModel: AddMedicationViewModel = viewModel(factory = healthServiceViewModelFactory)
    val getAllMedicationsViewModel: GetAllMedicationsViewModel = viewModel(factory = healthServiceViewModelFactory)
    val state = getAllMedicationsViewModel.state

    val showEditDialog = remember { mutableStateOf(false) }
    val medicationToEdit = remember { mutableStateOf<Medication?>(null) }
    val editMedicationViewModel: EditMedicationViewModel = viewModel(factory = healthServiceViewModelFactory)

    val showDeleteDialog = remember { mutableStateOf(false) }
    val medicationToDelete = remember { mutableStateOf<Medication?>(null)}
    val deleteMedicationViewModel: DeleteMedicationViewModel = viewModel(factory = healthServiceViewModelFactory)

    LaunchedEffect(Unit) {
        userId.intValue = tokenDataStore.getId.first()?.toInt()!!
        getAllMedicationsViewModel.getAllMedications(userId.intValue)
        getAllTimesViewModel.getAllTimes()
    }
    val horizontalGradientBrush = Brush.horizontalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.tertiaryContainer
        )
    )
    val medicationDescription = "Keep in mind that medication tracking, while useful, is only one tool in safely managing your medication schedule. Please consult your doctor, pharmacist, family members and any caregivers to create a plan that works best for you."


    AppBar(
        title = "Medication",
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
                        text = "About Physical",
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = medicationDescription,
                        fontSize = 12.sp,
                        lineHeight = 14.sp
                    )
                }
            }
        }
        Button(
            onClick = {
                showMedicationModal.value = true
            },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(top = 20.dp),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.tertiary)
        ) {
            Text("Add Medication")
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp, top = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(state.cachedMedicationList.size) { index ->
                val timeSlot = remember {  mutableStateOf("") }
                timeState.cachedTimeList.forEachIndexed { i, iItem ->
                    if (state.cachedMedicationList[index].timeId == timeState.cachedTimeList[i].id){
                        timeSlot.value = timeState.cachedTimeList[i].time
                    }
                }
                MedicationDisplay(
                    formatTime(timeSlot.value),
                    state.cachedMedicationList[index].name,
                    state.cachedMedicationList[index].type,
                    state.cachedMedicationList[index].measureAmount,
                    state.cachedMedicationList[index].measureUnit,
                    state.cachedMedicationList[index].frequency,
                    {
                        medicationToEdit.value = state.cachedMedicationList[index]
                        showEditDialog.value = true
                    },
                    {
                        medicationToDelete.value = state.cachedMedicationList[index]
                        showDeleteDialog.value = true
                    }
                )
            }}
    }

    if(showMedicationModal.value){
        AddMedicationDialog(
            onDismiss = { showMedicationModal.value = false },
            onAddMedication = { userId, timeId, name, type, measureAmount, measureUnit, frequency ->
                addMedicationViewModel.addMedication(userId, timeId, name, type, measureAmount, measureUnit, frequency)
                showMedicationModal.value = false
                toHome()
            },
            userId = userId.intValue,
            timeId = timeId.intValue,
            onTimeIdChange = { timeId.intValue = it },
            name = name.value,
            onNameChange = { name.value = it },
            type = type.value,
            onTypeChange = { type.value = it },
            measureAmount = measureAmount.doubleValue,
            onMeasureAmountChange = { measureAmount.doubleValue = it },
            measureUnit = measureUnit.value,
            onMeasureUnitChange = { measureUnit.value = it },
            frequency = frequency.value,
            onFrequencyChange = { frequency.value = it }

        )
    }

    medicationToEdit.value?.let {medication ->
        if(showEditDialog.value){
            LaunchedEffect(showEditDialog) {
                timeId.intValue = medication.timeId
                name.value = medication.name
                type.value = medication.type
                measureAmount.doubleValue = medication.measureAmount
                measureUnit.value = medication.measureUnit
                frequency.value = medication.frequency

            }
            EditMedicationDialog(
                onDismiss = { showEditDialog.value = false },
                onEditMedication = { id, userId, timeId, name, type, measureAmount, measureUnit, frequency ->
                    editMedicationViewModel.editMedication(id, userId, timeId, name, type, measureAmount, measureUnit, frequency)
                    showEditDialog.value = false
                    toHome()
                },
                id = medication.id,
                userId = medication.userId,
                timeId = timeId.intValue,
                onTimeIdChange = { timeId.intValue = it },
                name = name.value,
                onNameChange = { name.value = it },
                type = type.value,
                onTypeChange = { type.value = it },
                measureAmount = measureAmount.doubleValue,
                onMeasureAmountChange = { measureAmount.doubleValue = it },
                measureUnit = measureUnit.value,
                onMeasureUnitChange = { measureUnit.value = it },
                frequency = frequency.value,
                onFrequencyChange = { frequency.value = it }
            )
        }
    }

    medicationToDelete.value?.let {medication ->
        if(showDeleteDialog.value){
            DeleteMedicationDialog(
                onDismiss = { showDeleteDialog.value = false },
                onDeleteMedication = { id ->
                    deleteMedicationViewModel.deleteMedication(id)
                    showDeleteDialog.value = false
                    toHome()
                },
                id = medication.id
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatTime(timestamp: String): String {
    return try {
        val dateTime = OffsetDateTime.parse(timestamp)
        val formatter = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH)
        dateTime.format(formatter)
    } catch (e: Exception) {
        "Invalid Time"
    }
}