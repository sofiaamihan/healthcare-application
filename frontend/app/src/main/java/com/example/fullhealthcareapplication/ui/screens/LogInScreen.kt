package com.example.fullhealthcareapplication.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fullhealthcareapplication.R
import com.example.fullhealthcareapplication.data.factory.HealthServiceViewModelFactory
import com.example.fullhealthcareapplication.data.viewmodel.LoginViewModel
import com.example.fullhealthcareapplication.data.factory.UserInfoViewModelFactory
import com.example.fullhealthcareapplication.data.viewmodel.GetUserIdViewModel
import com.example.fullhealthcareapplication.ui.components.Back
import com.example.fullhealthcareapplication.ui.components.CustomTextField
import com.example.fullhealthcareapplication.ui.components.FieldInput
import com.example.fullhealthcareapplication.ui.components.GreenButton
import com.example.fullhealthcareapplication.ui.components.WhiteText
import com.example.fullhealthcareapplication.ui.components.validateNRIC
import com.example.fullhealthcareapplication.ui.components.validatePassword
import kotlinx.coroutines.launch

@Composable
fun LogInScreen(
    userInfoViewModelFactory: UserInfoViewModelFactory,
    healthServiceViewModelFactory: HealthServiceViewModelFactory,
    toWelcome: () -> Unit,
    toHome: () -> Unit
){
    var nric = remember { FieldInput() }
    var password = remember { FieldInput() }

    val context = LocalContext.current
    val loginViewModel: LoginViewModel = viewModel(factory = userInfoViewModelFactory)
    val getUserIdViewModel: GetUserIdViewModel = viewModel(factory = healthServiceViewModelFactory)


    Back(
        toBack = {toWelcome()},
        color = Color.White
    ){ padding ->
        Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "Logo"
            )
            WhiteText(stringResource(R.string.log_in_capital))
            CustomTextField(
                label = stringResource(R.string.nric),
                fieldInput = nric,
                validation = { validateNRIC(it) }
            )
            CustomTextField(
                label = stringResource(R.string.password),
                fieldInput = password,
                validation = { validatePassword(it) },
                isPasswordField = true
            )
            GreenButton(
                text = stringResource(R.string.log_in),
                onClick = {
                    loginViewModel.loginUser (nric.value.value, password.value.value)
                    if (loginViewModel.state.successState) {
                        toHome()
                    }
                }
            )
        }
    }

}