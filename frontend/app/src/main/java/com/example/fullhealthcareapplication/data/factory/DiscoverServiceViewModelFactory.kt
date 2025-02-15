package com.example.fullhealthcareapplication.data.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fullhealthcareapplication.data.repository.DiscoverServiceRepository
import com.example.fullhealthcareapplication.data.viewmodel.AddContentViewModel
import com.example.fullhealthcareapplication.data.viewmodel.DeleteContentViewModel
import com.example.fullhealthcareapplication.data.viewmodel.EditContentViewModel
import com.example.fullhealthcareapplication.data.viewmodel.GetAllContentCategoriesViewModel
import com.example.fullhealthcareapplication.data.viewmodel.GetAllContentViewModel

@Suppress("UNCHECKED_CAST")
class DiscoverServiceViewModelFactory(
    private val discoverServiceRepository: DiscoverServiceRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(GetAllContentViewModel::class.java) -> GetAllContentViewModel(
                discoverServiceRepository
            ) as T
            modelClass.isAssignableFrom(GetAllContentCategoriesViewModel::class.java) -> GetAllContentCategoriesViewModel(
                discoverServiceRepository
            ) as T
            modelClass.isAssignableFrom(AddContentViewModel::class.java) -> AddContentViewModel(
                discoverServiceRepository
            ) as T
            modelClass.isAssignableFrom(EditContentViewModel::class.java) -> EditContentViewModel(
                discoverServiceRepository
            ) as T
            modelClass.isAssignableFrom(DeleteContentViewModel::class.java) -> DeleteContentViewModel(
                discoverServiceRepository
            ) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}