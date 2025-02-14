package com.example.fullhealthcareapplication.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fullhealthcareapplication.R
import com.example.fullhealthcareapplication.data.viewmodel.SignUpViewModel
import com.example.fullhealthcareapplication.data.factory.UserInfoViewModelFactory
import com.example.fullhealthcareapplication.ui.components.Back
import com.example.fullhealthcareapplication.ui.components.CustomTextField
import com.example.fullhealthcareapplication.ui.components.FieldInput
import com.example.fullhealthcareapplication.ui.components.GreenButton
import com.example.fullhealthcareapplication.ui.components.WhiteText
import com.example.fullhealthcareapplication.ui.components.validateEmail
import com.example.fullhealthcareapplication.ui.components.validateNRIC
import com.example.fullhealthcareapplication.ui.components.validatePassword


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    viewModelFactory: UserInfoViewModelFactory,
    toWelcome: () -> Unit,
    toLogin: () -> Unit
){
    var nric = remember { FieldInput() }
    var email = remember { FieldInput() }
    var fullName = remember { FieldInput() }
    var password = remember { FieldInput() }
    var confirmPassword = remember { FieldInput() }


    val context = LocalContext.current
    val roles = listOf("User", "Admin")
    val roleState = remember { mutableStateOf("") }
    val selectedTextState = remember { mutableStateOf(roles[0]) }
    val expanded = remember { mutableStateOf(false) }

    val signUpViewModel: SignUpViewModel = viewModel(factory = viewModelFactory)

    val scrollState = rememberScrollState() // For vertical scroll in the future

    Box (
        modifier = Modifier.fillMaxSize()
    ){
        Back(
            toBack = { toWelcome() },
            modifier = Modifier.zIndex(1f),
            color = Color.White
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(0f),
//                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "Logo"
            )
            WhiteText(stringResource(R.string.sign_up_capital))
            CustomTextField(
                label = stringResource(R.string.nric),
                fieldInput = nric,
                validation = { validateNRIC(it) }
            )
            CustomTextField(
                label = stringResource(R.string.email),
                fieldInput = email,
                validation = { validateEmail(it) }
            )
            CustomTextField(
                label = stringResource(R.string.full_name),
                fieldInput = fullName,
            )
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
                    roles.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = item) },
                            onClick = {
                                selectedTextState.value = item
                                roleState.value = item
                                expanded.value = false
                                Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
            CustomTextField(
                label = stringResource(R.string.password),
                fieldInput = password,
                validation = { validatePassword(it) },
                isPasswordField = true
            )
            CustomTextField(
                label = stringResource(R.string.confirm_password),
                fieldInput = confirmPassword,
                validation = { validatePassword(it) },
                isPasswordField = true
            )
            GreenButton(
                text = stringResource(R.string.log_in),
                onClick = {

                    if (
                        nric.value.value.isEmpty() ||
                        email.value.value.isEmpty() ||
                        fullName.value.value.isEmpty() ||
                        password.value.value.isEmpty() ||
                        confirmPassword.value.value.isEmpty() ||
                        validateNRIC(nric.value.value) != null ||
                        validateEmail(email.value.value) != null ||
                        validatePassword(password.value.value) != null
                    ) {
                        Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT)
                            .show()
                        return@GreenButton
                    }

                    if (password.value.value != confirmPassword.value.value) {
                        Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                        return@GreenButton
                    }

                    // Needs checking like if there is already an account associated to the email
                    signUpViewModel.signUpUser(
                        nric.value.value,
                        roleState.value,
                        email.value.value,
                        fullName.value.value,
                        password.value.value
                    )
                    if (signUpViewModel.state.successState){
//                        Toast.makeText(context, "Sign Up Successful!!", Toast.LENGTH_SHORT).show()
                        // Make a toast to display account successfully created
                        toLogin()
                    }
                }
            )
        }
    }
}