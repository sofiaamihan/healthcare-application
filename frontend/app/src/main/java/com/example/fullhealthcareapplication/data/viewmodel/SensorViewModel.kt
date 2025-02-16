package com.example.fullhealthcareapplication.data.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SensorViewModel : ViewModel(){
    private val _accelerometerData = MutableStateFlow(listOf(0f, 0f, 0f))
    val accelerometerData = _accelerometerData.asStateFlow()

    fun updateAccelerometerData(x: Float, y: Float, z: Float) {
        _accelerometerData.value = listOf(x, y, z)
    }
}