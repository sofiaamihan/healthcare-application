package com.example.fullhealthcareapplication.data.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fullhealthcareapplication.data.repository.UserInfoRepository
import com.example.fullhealthcareapplication.data.viewmodel.ChangePasswordViewModel
import com.example.fullhealthcareapplication.data.viewmodel.DeleteAccountViewModel
import com.example.fullhealthcareapplication.data.viewmodel.LoginViewModel
import com.example.fullhealthcareapplication.data.viewmodel.SignUpViewModel
import com.example.fullhealthcareapplication.data.viewmodel.UpdateProfileViewModel

@Suppress("UNCHECKED_CAST")
class UserInfoViewModelFactory(
    private val userInfoRepository: UserInfoRepository
) : ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(
                userInfoRepository
            ) as T
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> SignUpViewModel(
                userInfoRepository
            ) as T
            modelClass.isAssignableFrom(UpdateProfileViewModel::class.java) -> UpdateProfileViewModel(
                userInfoRepository
            ) as T
            modelClass.isAssignableFrom(ChangePasswordViewModel::class.java) -> ChangePasswordViewModel(
                userInfoRepository
            ) as T
            modelClass.isAssignableFrom(DeleteAccountViewModel::class.java) -> DeleteAccountViewModel(
                userInfoRepository
            ) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}