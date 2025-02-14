package com.example.fullhealthcareapplication.data.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fullhealthcareapplication.data.repository.UserInfoRepository
import kotlinx.coroutines.launch

data class ResultState(
    var loadingState: Boolean = false,
    var errorState: Boolean = false,
    var successState: Boolean = false,
    var errorMessage: String? = null
)


class LoginViewModel(
    private val userInfoRepository: UserInfoRepository,
) : ViewModel() {
    var state by mutableStateOf(ResultState())

    fun loginUser (nric:String, password: String) {
        viewModelScope.launch {
            state = state.copy(loadingState = true)
            val response = userInfoRepository.userLogin(nric, password)
            if(response != null){
                state = state.copy(successState = true)
                // I can also access the token, nric, and role from the response
            } else {
                state = state.copy(errorState = true, errorMessage = "Login failed")
            }
            state = state.copy(loadingState = false)
        }
    }
}

class SignUpViewModel(
    private val userInfoRepository: UserInfoRepository
) : ViewModel() {
    var state by mutableStateOf(ResultState())

    fun signUpUser(
        nric: String,
        role: String,
        email: String,
        fullname: String,
        password: String
    ) {
        viewModelScope.launch {
            state = state.copy(loadingState = true)

            val result = userInfoRepository.userSignUp(nric, role, email, fullname, password)
            if(result != null){
                state = state.copy(successState = true)
            } else {
                state = state.copy(errorState = true, errorMessage = "Sign Up Failed")
            }
            state = state.copy(loadingState = false)
        }
    }
}

class UpdateProfileViewModel(
    private val userInfoRepository: UserInfoRepository
) : ViewModel() {
    var state by mutableStateOf(ResultState())

    fun updateProfile(
        nric: String,
        role: String,
        email: String,
        fullname: String
    ) {
        viewModelScope.launch {
            val result = userInfoRepository.userUpdate(nric, role, email, fullname)
            if(result != null){
                state = state.copy(successState = true)
            } else {
                state = state.copy(errorState = true, errorMessage = "Update Failed")
            }
            state = state.copy(loadingState = false)
        }
    }
}

class ChangePasswordViewModel(
    private val userInfoRepository: UserInfoRepository
): ViewModel() {
    var state by mutableStateOf(ResultState())

    fun changePassword(
        nric: String,
        role: String,
        password: String,
        newPassword: String
    ){
        viewModelScope.launch{
            val result = userInfoRepository.userChangePassword(nric, role, password, newPassword)
            if(result != null){
                state = state.copy(successState = true)
            } else {
                state = state.copy(errorState = true, errorMessage = "Change Password Failed")
            }
            state = state.copy(loadingState = false)
        }
    }
}

class DeleteAccountViewModel(
    private val userInfoRepository: UserInfoRepository
): ViewModel() {
    var state by mutableStateOf(ResultState())

    fun deleteAccount(
        nric: String,
        role: String,
        password: String
    ){
        viewModelScope.launch{
            val result = userInfoRepository.userDeleteAccount(nric, role, password)
            if(result != null){
                state = state.copy(successState = true)
            } else {
                state = state.copy(errorState = true, errorMessage = "Delete Account Failed")
            }
            state = state.copy(loadingState = false)
        }
    }
}