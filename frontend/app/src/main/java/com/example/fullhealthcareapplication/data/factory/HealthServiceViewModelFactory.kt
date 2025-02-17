package com.example.fullhealthcareapplication.data.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fullhealthcareapplication.data.repository.HealthServiceRepository
import com.example.fullhealthcareapplication.data.viewmodel.AddActivityViewModel
import com.example.fullhealthcareapplication.data.viewmodel.AddMedicationViewModel
import com.example.fullhealthcareapplication.data.viewmodel.AddUserMeasurementsViewModel
import com.example.fullhealthcareapplication.data.viewmodel.DeleteActivityViewModel
import com.example.fullhealthcareapplication.data.viewmodel.DeleteMedicationViewModel
import com.example.fullhealthcareapplication.data.viewmodel.EditActivityViewModel
import com.example.fullhealthcareapplication.data.viewmodel.EditMedicationViewModel
import com.example.fullhealthcareapplication.data.viewmodel.EditUserMeasurementsViewModel
import com.example.fullhealthcareapplication.data.viewmodel.GetActivitiesViewModel
import com.example.fullhealthcareapplication.data.viewmodel.GetAllActivitiesViewModel
import com.example.fullhealthcareapplication.data.viewmodel.GetAllCategoriesViewModel
import com.example.fullhealthcareapplication.data.viewmodel.GetAllMedicationsViewModel
import com.example.fullhealthcareapplication.data.viewmodel.GetAllTimesViewModel
import com.example.fullhealthcareapplication.data.viewmodel.GetCategoriesViewModel
import com.example.fullhealthcareapplication.data.viewmodel.GetMedicationsViewModel
import com.example.fullhealthcareapplication.data.viewmodel.GetOnboardingStatusViewModel
import com.example.fullhealthcareapplication.data.viewmodel.GetTimesViewModel
import com.example.fullhealthcareapplication.data.viewmodel.GetUserIdViewModel
import com.example.fullhealthcareapplication.data.viewmodel.GetUserMeasurementsViewModel
import com.example.fullhealthcareapplication.data.viewmodel.UpdateOnboardingStatusViewModel

@Suppress("UNCHECKED_CAST")
class HealthServiceViewModelFactory(
    private val healthServiceRepository: HealthServiceRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(GetUserIdViewModel::class.java) -> GetUserIdViewModel(
                healthServiceRepository
            ) as T
            modelClass.isAssignableFrom(AddActivityViewModel::class.java) -> AddActivityViewModel(
                healthServiceRepository
            ) as T
            modelClass.isAssignableFrom(EditActivityViewModel::class.java) -> EditActivityViewModel(
                healthServiceRepository
            ) as T
            modelClass.isAssignableFrom(DeleteActivityViewModel::class.java) -> DeleteActivityViewModel(
                healthServiceRepository
            ) as T
            modelClass.isAssignableFrom(AddMedicationViewModel::class.java) -> AddMedicationViewModel(
                healthServiceRepository
            ) as T
            modelClass.isAssignableFrom(EditMedicationViewModel::class.java) -> EditMedicationViewModel(
                healthServiceRepository
            ) as T
            modelClass.isAssignableFrom(DeleteMedicationViewModel::class.java) -> DeleteMedicationViewModel(
                healthServiceRepository
            ) as T
            modelClass.isAssignableFrom(GetUserMeasurementsViewModel::class.java) -> GetUserMeasurementsViewModel(
                healthServiceRepository
            ) as T
            modelClass.isAssignableFrom(EditUserMeasurementsViewModel::class.java) -> EditUserMeasurementsViewModel(
                healthServiceRepository
            ) as T
            modelClass.isAssignableFrom(GetCategoriesViewModel::class.java) -> GetCategoriesViewModel(
                healthServiceRepository
            ) as T
            modelClass.isAssignableFrom(GetAllCategoriesViewModel::class.java) -> GetAllCategoriesViewModel(
                healthServiceRepository
            ) as T
            modelClass.isAssignableFrom(GetTimesViewModel::class.java) -> GetTimesViewModel(
                healthServiceRepository
            ) as T
            modelClass.isAssignableFrom(GetAllTimesViewModel::class.java) -> GetAllTimesViewModel(
                healthServiceRepository
            ) as T
            modelClass.isAssignableFrom(GetMedicationsViewModel::class.java) -> GetMedicationsViewModel(
                healthServiceRepository
            ) as T
            modelClass.isAssignableFrom(GetAllMedicationsViewModel::class.java) -> GetAllMedicationsViewModel(
                healthServiceRepository
            ) as T
            modelClass.isAssignableFrom(GetActivitiesViewModel::class.java) -> GetActivitiesViewModel(
                healthServiceRepository
            ) as T
            modelClass.isAssignableFrom(GetAllActivitiesViewModel::class.java) -> GetAllActivitiesViewModel(
                healthServiceRepository
            ) as T
            modelClass.isAssignableFrom(GetOnboardingStatusViewModel::class.java) -> GetOnboardingStatusViewModel(
                healthServiceRepository
            ) as T
            modelClass.isAssignableFrom(UpdateOnboardingStatusViewModel::class.java) -> UpdateOnboardingStatusViewModel(
                healthServiceRepository
            ) as T
            modelClass.isAssignableFrom(AddUserMeasurementsViewModel::class.java) -> AddUserMeasurementsViewModel(
                healthServiceRepository
            ) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}