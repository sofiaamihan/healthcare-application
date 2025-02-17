package com.example.fullhealthcareapplication.data.viewmodel

import android.app.Application
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SensorViewModel(application: Application) : AndroidViewModel(application), SensorEventListener {
    private val sensorManager: SensorManager =
        application.getSystemService(SensorManager::class.java)

    private val accelerometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val gyroscope: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

    private val _accelerometerData = MutableStateFlow(listOf(0f, 0f, 0f))
    val accelerometerData: StateFlow<List<Float>> = _accelerometerData

    private val _gyroscopeData = MutableStateFlow(listOf(0f, 0f, 0f))
    val gyroscopeData: StateFlow<List<Float>> = _gyroscopeData

    fun startListening() {
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_FASTEST)
        }
        gyroscope?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_FASTEST)
        }
    }

    fun stopListening() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        viewModelScope.launch {
            when (event?.sensor?.type) {
                Sensor.TYPE_ACCELEROMETER -> {
                    _accelerometerData.value = listOf(event.values[0], event.values[1], event.values[2])
                }
                Sensor.TYPE_GYROSCOPE -> {
                    _gyroscopeData.value = listOf(event.values[0], event.values[1], event.values[2])
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}