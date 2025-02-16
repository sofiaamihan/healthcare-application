package com.example.fullhealthcareapplication

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.fullhealthcareapplication.ui.theme.FullHealthcareApplicationTheme
import com.example.fullhealthcareapplication.data.preferences.TokenDataStore
import com.example.fullhealthcareapplication.data.repository.DiscoverServiceRepository
import com.example.fullhealthcareapplication.data.repository.UserInfoRepository
import com.example.fullhealthcareapplication.data.factory.DiscoverServiceViewModelFactory
import com.example.fullhealthcareapplication.data.factory.HealthServiceViewModelFactory
import com.example.fullhealthcareapplication.data.factory.UserInfoViewModelFactory
import com.example.fullhealthcareapplication.data.repository.HealthServiceRepository
import com.example.fullhealthcareapplication.data.viewmodel.SensorViewModel
import com.example.fullhealthcareapplication.ui.components.BlackText
import com.example.fullhealthcareapplication.ui.components.Graph
import com.example.fullhealthcareapplication.ui.components.GraphAppearance
import com.example.fullhealthcareapplication.ui.graphs.RootNavigationGraph
import com.google.accompanist.permissions.ExperimentalPermissionsApi


class MainActivity : ComponentActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var gyroscope: Sensor? = null

    val xValue = mutableStateOf("")
    val yValue = mutableStateOf("")
    val zValue = mutableStateOf("")

    val xVal = mutableStateOf("")
    val yVal = mutableStateOf("")
    val zVal = mutableStateOf("")

    val accelerometerValues = mutableStateOf(listOf(0f, 0f, 0f))
    private lateinit var sensorViewModel: SensorViewModel



    @RequiresApi(Build.VERSION_CODES.Q)
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO - initialise the token data store one time and reference it
        sensorViewModel = ViewModelProvider(this)[SensorViewModel::class.java]


        val userInfoRepository = UserInfoRepository(TokenDataStore(this))
        val userInfoViewModelFactory = UserInfoViewModelFactory(userInfoRepository)
        val discoverServiceRepository = DiscoverServiceRepository(TokenDataStore(this))
        val discoverServiceViewModelFactory =
            DiscoverServiceViewModelFactory(discoverServiceRepository)
        val healthServiceRepository = HealthServiceRepository(TokenDataStore(this), this)
        val healthServiceViewModelFactory = HealthServiceViewModelFactory(healthServiceRepository)


        enableEdgeToEdge()
        setContent {
            val accValues by remember { accelerometerValues }
            FullHealthcareApplicationTheme() {
//                ----- Testing Sensors -----
//                val viewModel = viewModel<MainViewModel>()
//                val xAxis = viewModel.xAxis
//                val yAxis = viewModel.yAxis
//                val zAxis = viewModel.zAxis
//                Column (
//                    modifier = Modifier.fillMaxSize(),
//                    verticalArrangement = Arrangement.Center,
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ){
//                    Text("Accelerometer")
//                    Text(xValue.value)
//                    Text(yValue.value)
//                    Text(zValue.value)
//                    Text("Gyroscope")
//                    Text(xVal.value)
//                    Text(yVal.value)
//                    Text(zVal.value)
//                    Graph(
//                        modifier = Modifier.fillMaxSize(),
//                        xValues = listOf(0, 1, 2), // X, Y, Z labels
//                        yValues = (0..10).toList(), // Dummy Y axis labels
//                        points = accValues, // Dynamic values from accelerometer
//                        paddingSpace = 16.dp,
//                        verticalStep = 1,
//                        graphAppearance = GraphAppearance(
//                            graphColor = MaterialTheme.colorScheme.secondary,
//                            graphAxisColor = Color.Black,
//                            graphThickness = 4f,
//                            iscolorAreaUnderChart = true,
//                            colorAreaUnderChart = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
//                            isCircleVisible = true,
//                            circleColor = MaterialTheme.colorScheme.primary,
//                            backgroundColor = MaterialTheme.colorScheme.background
//                        ))
//                }


//                ----- Camera Testing -----
//                Column (
//                    modifier = Modifier.fillMaxSize(),
//                    verticalArrangement = Arrangement.Center,
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ){
//                    MainScreen()
//                }

//                ----- Main Screen -----
                RootNavigationGraph(
                    navController = rememberNavController(),
                    userInfoViewModelFactory = userInfoViewModelFactory,
                    discoverServiceViewModelFactory = discoverServiceViewModelFactory,
                    healthServiceViewModelFactory = healthServiceViewModelFactory,
                    tokenDataStore = TokenDataStore(this),
                    sensorViewModel = sensorViewModel
                )
//            }
            }

            sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        }
    }

        override fun onResume() {
            super.onResume()
            accelerometer?.also { acc ->
                sensorManager.registerListener(this, acc, SensorManager.SENSOR_DELAY_FASTEST)
            }
            gyroscope?.also { acc ->
                sensorManager.registerListener(this, acc, SensorManager.SENSOR_DELAY_FASTEST)
            }
        }

        override fun onPause() {
            super.onPause()
            sensorManager.unregisterListener(this)
        }


    override fun onSensorChanged(event: SensorEvent?) {
        when (event?.sensor?.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]
                xValue.value = "X: $x"
                yValue.value = "Y: $y"
                zValue.value = "Z: $z"
                accelerometerValues.value =
                    listOf(event.values[0], event.values[1], event.values[2])
                sensorViewModel.updateAccelerometerData(x, y, z)
            }

            Sensor.TYPE_GYROSCOPE -> {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]
                xVal.value = "X: $x"
                yVal.value = "Y: $y"
                zVal.value = "Z: $z"
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, p1: Int) {

    }

}






