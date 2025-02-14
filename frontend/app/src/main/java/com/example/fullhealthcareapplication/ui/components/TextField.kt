package com.example.fullhealthcareapplication.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

fun validateNRIC(nric: String): String? {
    val regex = Regex("^[A-Za-z]\\d{7}[A-Za-z]$")
    if (!regex.matches(nric)) return "Invalid NRIC format."
    return null
}

fun validateEmail(email: String): String? {
    if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) return "Invalid Email Format"
    // Add database check for existing user
    return null
}

fun validatePassword(password: String): String? {
    if(password.length < 8) return "Password must be at least 8 characters"
    if (!password.any{ it.isUpperCase() }) return "Password must contain at least one uppercase letter"
    if (!password.any{ it.isLowerCase() }) return "Password must contain at least one lowercase letter"
    if (!password.any{ it.isDigit() }) return "Password must contain at least one number"
    if (!password.any{ !it.isLetterOrDigit() }) return "Password must contain at least one special character"
    return null
}

data class FieldInput(
    var value: MutableState<String> = mutableStateOf(""),
    var hasInteracted: MutableState<Boolean> = mutableStateOf(false)
)

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    label: String,
    fieldInput: FieldInput,
    isPasswordField: Boolean = false,
    validation: (String) -> String? = { null },
) {
    val passwordVisible = remember { mutableStateOf(false) }
    val errorMessage = remember { mutableStateOf<String?>(null) }

    OutlinedTextField(
        modifier = modifier,
        value = fieldInput.value.value,
        onValueChange = {
            fieldInput.value.value = it
            fieldInput.hasInteracted.value = true
            errorMessage.value = validation(it)
        },
        label = { Text(label) },
        visualTransformation = if (isPasswordField && !passwordVisible.value) PasswordVisualTransformation() else VisualTransformation.None,
        supportingText = {
            errorMessage.value?.let { Text(text = it, color = Color.Red) }
        },
        trailingIcon = {
            if (isPasswordField) {
                val icon: ImageVector = if (passwordVisible.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                    Icon(imageVector = icon, contentDescription = "Toggle Password Visibility")
                }
            }
        },
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedIndicatorColor = Color.White,
            unfocusedIndicatorColor = Color.White,
            focusedLabelColor = Color.White,
            unfocusedLabelColor = Color.White,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent
        )
    )
}