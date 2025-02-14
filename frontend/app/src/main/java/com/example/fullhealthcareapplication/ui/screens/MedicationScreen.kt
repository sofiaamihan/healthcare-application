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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
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
import com.example.fullhealthcareapplication.data.factory.HealthServiceViewModelFactory
import com.example.fullhealthcareapplication.data.preferences.TokenDataStore
import com.example.fullhealthcareapplication.data.viewmodel.AddMedicationViewModel
import com.example.fullhealthcareapplication.ui.components.AddMedicationDialog
import com.example.fullhealthcareapplication.ui.components.AppBar
import kotlinx.coroutines.flow.first

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

    val addMedicationViewModel: AddMedicationViewModel = viewModel(factory = healthServiceViewModelFactory)
    LaunchedEffect(Unit) {
        userId.intValue = tokenDataStore.getId.first()?.toInt()!!
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
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, start = 24.dp, end = 24.dp, bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "Iron Supplements",
                textAlign = TextAlign.Start,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Row (
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp, bottom = 5.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                "Type",
                fontSize = 12.sp,
                lineHeight = 14.sp
            )
            Text(
                "Capsule",
                fontSize = 12.sp,
                lineHeight = 14.sp
            )
        }
        Row (
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp, bottom = 5.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                "Amount",
                fontSize = 12.sp,
                lineHeight = 14.sp
            )
            Text(
                "10 mg",
                fontSize = 12.sp,
                lineHeight = 14.sp
            )
        }
        Row (
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp, bottom = 5.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                "Frequency",
                fontSize = 12.sp,
                lineHeight = 14.sp
            )
            Text(
                "Daily",
                fontSize = 12.sp,
                lineHeight = 14.sp
            )
        }
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, start = 24.dp, end = 24.dp, bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "Magnesium Supplements",
                textAlign = TextAlign.Start,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Row (
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp, bottom = 5.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                "Type",
                fontSize = 12.sp,
                lineHeight = 14.sp
            )
            Text(
                "Capsule",
                fontSize = 12.sp,
                lineHeight = 14.sp
            )
        }
        Row (
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp, bottom = 5.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                "Amount",
                fontSize = 12.sp,
                lineHeight = 14.sp
            )
            Text(
                "5 mg",
                fontSize = 12.sp,
                lineHeight = 14.sp
            )
        }
        Row (
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp, bottom = 5.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                "Frequency",
                fontSize = 12.sp,
                lineHeight = 14.sp
            )
            Text(
                "Weekly",
                fontSize = 12.sp,
                lineHeight = 14.sp
            )
        }
    }

    if(showMedicationModal.value){
        AddMedicationDialog(
            onDismiss = { showMedicationModal.value = false },
            onAddMedication = { userId, timeId, name, type, measureAmount, measureUnit, frequency ->
                addMedicationViewModel.addMedication(userId, timeId, name, type, measureAmount, measureUnit, frequency)
                showMedicationModal.value = false
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
}
