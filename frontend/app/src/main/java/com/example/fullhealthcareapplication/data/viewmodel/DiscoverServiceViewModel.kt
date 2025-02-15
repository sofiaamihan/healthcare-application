package com.example.fullhealthcareapplication.data.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.fullhealthcareapplication.data.repository.DiscoverServiceRepository
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.fullhealthcareapplication.data.repository.ContentCategoriesResponse
import com.example.fullhealthcareapplication.data.repository.ContentResponse
import com.example.fullhealthcareapplication.data.repository.Result
import kotlinx.coroutines.launch

data class DiscoverResultState(
    var loadingState: Boolean = false,
    var errorState: Boolean = false,
    var successState: Boolean = false,
    var errorMessage: String? = null,
    var contentList: List<ContentResponse> = emptyList(),
    var categoriesList: List<ContentCategoriesResponse> = emptyList()
)

class GetAllContentViewModel(
    private val discoverServiceRepository: DiscoverServiceRepository,
) : ViewModel() {
    var state by mutableStateOf(DiscoverResultState())

    fun getAllContent() {
        viewModelScope.launch {
            state = state.copy(loadingState = true)

            when (val response = discoverServiceRepository.getAllContent()) {
                is Result.Success -> {
                    state = state.copy(
                        successState = true,
                        contentList = response.data
                    )
                }
                is Result.Error -> {
                    state = state.copy(
                        errorState = true,
                        errorMessage = "Get Content failed: ${response.exception.localizedMessage}"
                    )
                }
            }

            state = state.copy(loadingState = false)
        }
    }
}

class GetAllContentCategoriesViewModel(
    private val discoverServiceRepository: DiscoverServiceRepository,
) : ViewModel() {
    var state by mutableStateOf(DiscoverResultState())

    fun getAllContentCategories() {
        viewModelScope.launch {
            state = state.copy(loadingState = true)

            when (val response = discoverServiceRepository.getAllContentCategories()) {
                is Result.Success -> {
                    state = state.copy(
                        successState = true,
                        categoriesList = response.data
                    )
                }
                is Result.Error -> {
                    state = state.copy(
                        errorState = true,
                        errorMessage = "Get Categories failed: ${response.exception.localizedMessage}"
                    )
                }
            }

            state = state.copy(loadingState = false)
        }
    }
}

class AddContentViewModel(
    private val discoverServiceRepository: DiscoverServiceRepository
): ViewModel() {
    var state by mutableStateOf(DiscoverResultState())

    fun addContent(
        contentCategoryId: Int,
        title: String,
        summary: String,
        description: String,
        picture: String
    ) {
        viewModelScope.launch{
            state = state.copy(loadingState = true)

            val result = discoverServiceRepository.addContent(contentCategoryId, title, summary, description, picture)
            if(result != null){
                state = state.copy(successState = true)
            } else {
                state = state.copy(errorState = true, errorMessage = "Add Content Failed")
            }
            state = state.copy(loadingState = false)
        }
    }
}

class EditContentViewModel(
    private val discoverServiceRepository: DiscoverServiceRepository
): ViewModel() {
    var state by mutableStateOf(DiscoverResultState())

    fun editContent(
        id: Int,
        contentCategoryId: Int,
        title: String,
        summary: String,
        description: String,
        picture: String
    ){
        viewModelScope.launch{
            state = state.copy(loadingState = true)

            val result = discoverServiceRepository.editContent(id, contentCategoryId, title, summary, description, picture)
            if(result != null){
                state = state.copy(successState = true)
            } else {
                state = state.copy(errorState = true, errorMessage = "Edit Content Failed")
            }
            state = state.copy(loadingState = false)
        }
    }

}

class DeleteContentViewModel(
    private val discoverServiceRepository: DiscoverServiceRepository
): ViewModel() {
    var state by mutableStateOf(DiscoverResultState())

    fun deleteContent(
        id: Int
    ){
        viewModelScope.launch{
            state = state.copy(loadingState = true)

            val result = discoverServiceRepository.deleteContent(id)
            if(result != null){
                state = state.copy(successState = true)
            } else {
                state = state.copy(errorState = true, errorMessage = "Delete Content Failed")
            }
            state = state.copy(loadingState = false)
        }
    }
}
