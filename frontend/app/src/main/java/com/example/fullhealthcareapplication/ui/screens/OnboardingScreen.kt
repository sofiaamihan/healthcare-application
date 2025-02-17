package com.example.fullhealthcareapplication.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.fullhealthcareapplication.R
import com.example.fullhealthcareapplication.data.factory.HealthServiceViewModelFactory
import com.example.fullhealthcareapplication.data.viewmodel.AddUserMeasurementsViewModel
import com.example.fullhealthcareapplication.ui.components.Back
import com.example.fullhealthcareapplication.ui.components.CustomTextField
import com.example.fullhealthcareapplication.ui.components.OnboardingItems
import com.example.fullhealthcareapplication.ui.components.WhiteText
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import java.time.format.TextStyle

@ExperimentalPagerApi
@Composable
fun OnboardingScreen(
    nric: String,
    role: String,
    healthServiceViewModelFactory: HealthServiceViewModelFactory,
    toLogin: () -> Unit
) {
    val items = OnboardingItems.getData()
    val scope = rememberCoroutineScope()
    val pageState = rememberPagerState(pageCount = items.size)
    val onboardingState = remember { mutableStateOf(OnboardingState()) }

    val addUserMeasurementsViewModel: AddUserMeasurementsViewModel = viewModel(factory = healthServiceViewModelFactory)
    val state = addUserMeasurementsViewModel.state

    Back(
        toBack = {},
        color = Color.White
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopSection(
                onSkipClick = {
                    if (pageState.currentPage + 1 < items.size) scope.launch {
                        pageState.scrollToPage(items.size - 1)
                    }
                }
            )

            HorizontalPager(
                state = pageState,
                modifier = Modifier
                    .fillMaxHeight(0.6f)
                    .fillMaxWidth()
            ) { page ->
                OnboardingItem(items = items[page], onboardingState = onboardingState.value, onStateChange = { newState ->
                    onboardingState.value = newState
                })
            }

            Box() {
                Indicators(size = items.size, index = pageState.currentPage)
            }

            Button(

                onClick = {
                    if (pageState.currentPage + 1 < items.size) {
                        Log.d("Checks", nric)
                        scope.launch {
                            pageState.scrollToPage(pageState.currentPage + 1)
                        }
                    } else {
                        // On final page, perform your action (e.g., save data to database)
//                        saveToDatabase(onboardingState.value)
                        addUserMeasurementsViewModel.addUserMeasurements(
                            nric,
                            role,
                            onboardingState.value.age.toInt(),
                            onboardingState.value.gender.toString(),
                            onboardingState.value.weight.toDouble(),
                            onboardingState.value.height.toDouble()
                        )
                        if (addUserMeasurementsViewModel.state.successState) {
                            toLogin()
                        }
                    }
                },
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth(0.3f)
            ) {
                Text(text = if (pageState.currentPage == items.size - 1) "Submit" else "Next")
            }
        }
    }
}

@ExperimentalPagerApi
@Composable
fun TopSection(onSkipClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        TextButton(
            onClick = onSkipClick,
            modifier = Modifier.align(Alignment.CenterEnd),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(text = "Skip", color = MaterialTheme.colorScheme.onBackground)
        }
    }}

@Composable
fun BoxScope.Indicators(size: Int, index: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.align(Alignment.CenterStart)
    ) {
        repeat(size) {
            Indicator(isSelected = it == index)
        }
    }}

@Composable
fun Indicator(isSelected: Boolean) {
    val width = animateDpAsState(
        targetValue = if (isSelected) 25.dp else 10.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )
    Box(
        modifier = Modifier
            .height(10.dp)
            .width(width.value)
            .clip(CircleShape)
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color(0XFFF8E2E7)
            )
    ) {
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingItem(items: OnboardingItems, onboardingState: OnboardingState, onStateChange: (OnboardingState) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = "Logo"
        )
        WhiteText(items.title)

        when (items.title) {
            "What is your age?" -> {
                OutlinedTextField(
                    value = onboardingState.age,
                    onValueChange = { newAge -> onStateChange(onboardingState.copy(age = newAge)) },
                    label = { Text("Age") },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedIndicatorColor = Color.White,
                        unfocusedIndicatorColor = Color.White,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
            "What is your gender?" -> {
                val context = LocalContext.current
                val genders = listOf("M", "F", "PNTS")
                val genderState = remember { mutableStateOf(onboardingState.gender) }
                val selectedTextState = remember { mutableStateOf(genderState.value.ifEmpty { "Select Gender" }) }
                val expanded = remember { mutableStateOf(false) }

                ExposedDropdownMenuBox(
                    expanded = expanded.value,
                    onExpandedChange = {
                        expanded.value = !expanded.value
                    }
                ) {
                    TextField(
                        value = selectedTextState.value,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded.value,
                        onDismissRequest = { expanded.value = false }
                    ) {
                        genders.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(text = item) },
                                onClick = {
                                    // Update both roleState and selectedTextState with the selected item
                                    selectedTextState.value = item
                                    genderState.value = item

                                    // Update onboardingState by passing the new gender value
                                    onStateChange(onboardingState.copy(gender = item))

                                    expanded.value = false
                                    Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                }

            }
            "What is your height?" -> {
                OutlinedTextField(
                    value = onboardingState.height,
                    onValueChange = { newHeight -> onStateChange(onboardingState.copy(height = newHeight)) },
                    label = { Text("Height (cm)") },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedIndicatorColor = Color.White,
                        unfocusedIndicatorColor = Color.White,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
            "What is your weight?" -> {
                OutlinedTextField(
                    value = onboardingState.weight,
                    onValueChange = { newWeight -> onStateChange(onboardingState.copy(weight = newWeight)) },
                    label = { Text("Weight (kg)") },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedIndicatorColor = Color.White,
                        unfocusedIndicatorColor = Color.White,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        }
    }
}


data class OnboardingState(
    var age: String = "",
    var gender: String = "",
    var height: String = "",
    var weight: String = ""
)
