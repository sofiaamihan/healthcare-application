package com.example.fullhealthcareapplication.data.viewmodel

import androidx.lifecycle.ViewModel
import com.example.fullhealthcareapplication.data.repository.HealthServiceRepository
import com.example.fullhealthcareapplication.data.repository.IdResponse
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.fullhealthcareapplication.data.entity.Activity
import com.example.fullhealthcareapplication.data.entity.Category
import com.example.fullhealthcareapplication.data.entity.Medication
import com.example.fullhealthcareapplication.data.entity.Time
import com.example.fullhealthcareapplication.data.entity.User
import com.example.fullhealthcareapplication.data.repository.ActivityResponse
import com.example.fullhealthcareapplication.data.repository.CategoryResponse
import com.example.fullhealthcareapplication.data.repository.MedicationResponse
import com.example.fullhealthcareapplication.data.repository.Result
import com.example.fullhealthcareapplication.data.repository.TimeResponse
import kotlinx.coroutines.launch

data class HealthResultState(
    var loadingState: Boolean = false,
    var errorState: Boolean = false,
    var successState: Boolean = false,
    var errorMessage: String? = null,
    var contentList: IdResponse = IdResponse(
        id = 0,
        nric = "",
        role = "",
        age = 0,
        gender = "",
        weight = 0.0,
        height = 0.0
    ),
    var userMeasurements: User = User(
        id = 0,
        nric = "",
        role = "",
        age = 0,
        gender = "",
        weight = 0.0,
        height = 0.0
    ),
    var categoryList: List<CategoryResponse> = listOf(CategoryResponse(
        id = 0,
        name = ""
    )),
    var cachedCategoryList: List<Category> = listOf(
        Category(
            id = 0,
            name = ""
        )
    ),
    var timeList: List<TimeResponse> = listOf(TimeResponse(
        id = 0,
        time = ""
    )),
    var cachedTimeList: List<Time> = listOf(Time(
        id = 0,
        time = ""
    )),
    var medicationList: List<MedicationResponse> = listOf(MedicationResponse(
        id = 0,
        userId = 0,
        timeId = 0,
        name = "",
        type = "",
        measureAmount = 0.0,
        measureUnit = "",
        frequency = ""
    )),
    var cachedMedicationList: List<Medication> = listOf(Medication(
        id = 0,
        userId = 0,
        timeId = 0,
        name = "",
        type = "",
        measureAmount = 0.0,
        measureUnit = "",
        frequency = ""
    )),
    var activityList: List<ActivityResponse> = listOf(ActivityResponse(
        id = 0,
        userId = 0,
        categoryId = 0,
        timeTaken = "",
        caloriesBurnt = 0.0,
        stepCount = 0.0,
        distance = 0.0,
        walkingSpeed = 0.0,
        walkingSteadiness = 0.0
    )),
    var cachedActivityList: List<Activity> = listOf(Activity(
        id = 0,
        userId = 0,
        categoryId = 0,
        timeTaken = "",
        caloriesBurnt = 0.0,
        stepCount = 0.0,
        distance = 0.0,
        walkingSpeed = 0.0,
        walkingSteadiness = 0.0
    ))
)

class GetUserIdViewModel(
    private val healthServiceRepository: HealthServiceRepository,
): ViewModel() {
    var state by mutableStateOf(HealthResultState())
    fun getUserId(
        nric: String
    ) {
        viewModelScope.launch {
            state = state.copy(loadingState = true)

            when (val response = healthServiceRepository.getUserId(nric)) {
                is Result.Success -> {
                    state = state.copy(
                        successState = true,
                        contentList = response.data
                    )
                }
                is Result.Error -> {
                    state = state.copy(
                        errorState = true,
                        errorMessage = "Get Id failed: ${response.exception.localizedMessage}"
                    )
                }
            }

            state = state.copy(loadingState = false)
        }
    }
}

class GetUserMeasurementsViewModel(
    private val healthServiceRepository: HealthServiceRepository
): ViewModel() {
    var state by mutableStateOf(HealthResultState())
    fun getUserMeasurements(
        id: Int
    ) {
        viewModelScope.launch {
            state = state.copy(loadingState = true)

            val result = healthServiceRepository.getUserMeasurements(id)
            if(result != null){
                state = state.copy(successState = true)
                state.userMeasurements = result
            } else {
                state = state.copy(errorState = true, errorMessage = "Get Measurements Failed")
            }
            state = state.copy(loadingState = false)

        }
    }
}

class EditUserMeasurementsViewModel(
    private val healthServiceRepository: HealthServiceRepository
): ViewModel() {
    var state by mutableStateOf(HealthResultState())
    fun editUserMeasurements(
        id: Int,
        nric: String,
        role: String,
        age: Int,
        gender: String,
        weight: Double,
        height: Double
    ){
        viewModelScope.launch {
            state = state.copy(loadingState = true)

            val result = healthServiceRepository.editUserMeasurements(id, nric, role, age, gender, weight, height)
            if(result != null){
                state = state.copy(successState = true)
            } else {
                state = state.copy(errorState = true, errorMessage = "Edit Measurements Failed")
            }
            state = state.copy(loadingState = false)

        }
    }
}

class AddActivityViewModel(
    private val healthServiceRepository: HealthServiceRepository
): ViewModel() {
    var state by mutableStateOf(HealthResultState())

    fun addActivity(
        userId: Int,
        activityCategoryId: Int,
        timeTaken: String,
        caloriesBurnt: Double,
        stepCount: Double,
        distance: Double,
        walkingSpeed: Double,
        walkingSteadiness: Double
    ) {
        viewModelScope.launch{
            state = state.copy(loadingState = true)

            val result = healthServiceRepository.addActivity(userId, activityCategoryId, timeTaken, caloriesBurnt, stepCount, distance, walkingSpeed, walkingSteadiness)
            if(result != null){
                state = state.copy(successState = true)
            } else {
                state = state.copy(errorState = true, errorMessage = "Add Activity Failed")
            }
            state = state.copy(loadingState = false)
        }
    }
}

class AddMedicationViewModel(
    private val healthServiceRepository: HealthServiceRepository
): ViewModel() {
    var state by mutableStateOf(HealthResultState())

    fun addMedication(
        userId: Int,
        timeId: Int,
        name: String,
        type: String,
        measureAmount: Double,
        measureUnit: String,
        frequency: String,
    ) {
        viewModelScope.launch{
            state = state.copy(loadingState = true)

            val result = healthServiceRepository.addMedication(userId, timeId, name, type, measureAmount, measureUnit, frequency)
            if(result != null){
                state = state.copy(successState = true)
            } else {
                state = state.copy(errorState = true, errorMessage = "Add Medication Failed")
            }
            state = state.copy(loadingState = false)
        }
    }
}

class GetCategoriesViewModel(
    private val healthServiceRepository: HealthServiceRepository,
): ViewModel() {
    var state by mutableStateOf(HealthResultState())
    fun getCategories() {
        viewModelScope.launch {
            state = state.copy(loadingState = true)

            when (val response = healthServiceRepository.getCategories()) {
                is Result.Success -> {
                    state = state.copy(
                        successState = true,
                        categoryList = response.data
                    )
                }
                is Result.Error -> {
                    state = state.copy(
                        errorState = true,
                        errorMessage = "Get Category failed: ${response.exception.localizedMessage}"
                    )
                }
            }

            state = state.copy(loadingState = false)
        }
    }
}

class GetAllCategoriesViewModel(
    private val healthServiceRepository: HealthServiceRepository
): ViewModel() {
    var state by mutableStateOf(HealthResultState())
    fun getAllCategories() {
        viewModelScope.launch {
            state = state.copy(loadingState = true)

            val result = healthServiceRepository.getAllCategories()
            if(result != null){
                state = state.copy(successState = true)
                state.cachedCategoryList = result
            } else {
                state = state.copy(errorState = true, errorMessage = "Get Categories Failed")
            }
            state = state.copy(loadingState = false)

        }
    }
}

class GetTimesViewModel(
    private val healthServiceRepository: HealthServiceRepository,
): ViewModel() {
    var state by mutableStateOf(HealthResultState())
    fun getTimes() {
        viewModelScope.launch {
            state = state.copy(loadingState = true)

            when (val response = healthServiceRepository.getTimes()) {
                is Result.Success -> {
                    state = state.copy(
                        successState = true,
                        timeList = response.data
                    )
                }
                is Result.Error -> {
                    state = state.copy(
                        errorState = true,
                        errorMessage = "Get Time failed: ${response.exception.localizedMessage}"
                    )
                }
            }

            state = state.copy(loadingState = false)
        }
    }
}

class GetAllTimesViewModel(
    private val healthServiceRepository: HealthServiceRepository
): ViewModel() {
    var state by mutableStateOf(HealthResultState())
    fun getAllTimes() {
        viewModelScope.launch {
            state = state.copy(loadingState = true)

            val result = healthServiceRepository.getAllTimes()
            if(result != null){
                state = state.copy(successState = true)
                state.cachedTimeList = result
            } else {
                state = state.copy(errorState = true, errorMessage = "Get Times Failed")
            }
            state = state.copy(loadingState = false)

        }
    }
}

class GetMedicationsViewModel(
    private val healthServiceRepository: HealthServiceRepository,
): ViewModel() {
    var state by mutableStateOf(HealthResultState())
    fun getMedications(
        userId: Int
    ) {
        viewModelScope.launch {
            state = state.copy(loadingState = true)

            when (val response = healthServiceRepository.getMedications(userId)) {
                is Result.Success -> {
                    state = state.copy(
                        successState = true,
                        medicationList = response.data
                    )
                }
                is Result.Error -> {
                    state = state.copy(
                        errorState = true,
                        errorMessage = "Get Medications failed: ${response.exception.localizedMessage}"
                    )
                }
            }

            state = state.copy(loadingState = false)
        }
    }
}

class GetAllMedicationsViewModel(
    private val healthServiceRepository: HealthServiceRepository
): ViewModel() {
    var state by mutableStateOf(HealthResultState())
    fun getAllMedications(
        userId: Int
    ) {
        viewModelScope.launch {
            state = state.copy(loadingState = true)

            val result = healthServiceRepository.getAllMedications(userId)
            if(result != null){
                state = state.copy(successState = true)
                state.cachedMedicationList = result
            } else {
                state = state.copy(errorState = true, errorMessage = "Get Medications Failed")
            }
            state = state.copy(loadingState = false)

        }
    }
}

class GetActivitiesViewModel(
    private val healthServiceRepository: HealthServiceRepository,
): ViewModel() {
    var state by mutableStateOf(HealthResultState())
    fun getActivities(
        userId: Int,
        date: String
    ) {
        viewModelScope.launch {
            state = state.copy(loadingState = true)

            when (val response = healthServiceRepository.getActivities(userId, date)) {
                is Result.Success -> {
                    state = state.copy(
                        successState = true,
                        activityList = response.data
                    )
                }
                is Result.Error -> {
                    state = state.copy(
                        errorState = true,
                        errorMessage = "Get Activities failed: ${response.exception.localizedMessage}"
                    )
                }
            }

            state = state.copy(loadingState = false)
        }
    }
}

class GetAllActivitiesViewModel(
    private val healthServiceRepository: HealthServiceRepository
): ViewModel() {
    var state by mutableStateOf(HealthResultState())
    fun getAllActivities(
        userId: Int,
        date: String
    ) {
        viewModelScope.launch {
            state = state.copy(loadingState = true)

            val result = healthServiceRepository.getAllActivities(userId, date)
            if(result != null){
                state = state.copy(successState = true)
                state.cachedActivityList = result
            } else {
                state = state.copy(errorState = true, errorMessage = "Get Activities Failed")
            }
            state = state.copy(loadingState = false)

        }
    }
}