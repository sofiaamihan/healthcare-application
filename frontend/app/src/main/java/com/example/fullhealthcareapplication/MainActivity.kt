package com.example.fullhealthcareapplication

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.example.fullhealthcareapplication.ui.theme.FullHealthcareApplicationTheme
import com.example.fullhealthcareapplication.data.preferences.TokenDataStore
import com.example.fullhealthcareapplication.data.repository.DiscoverServiceRepository
import com.example.fullhealthcareapplication.data.repository.UserInfoRepository
import com.example.fullhealthcareapplication.data.factory.DiscoverServiceViewModelFactory
import com.example.fullhealthcareapplication.data.factory.HealthServiceViewModelFactory
import com.example.fullhealthcareapplication.data.factory.UserInfoViewModelFactory
import com.example.fullhealthcareapplication.data.repository.HealthServiceRepository
import com.example.fullhealthcareapplication.ui.graphs.RootNavigationGraph
import com.example.fullhealthcareapplication.ui.theme.FullHealthcareApplicationTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class MainActivity : ComponentActivity() , SensorEventListener{

    private val sensorManager: SensorManager by lazy {
        getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }
//
//    private val sensor: Sensor? = null
    private val counter = MutableStateFlow<Int>(0)


    @RequiresApi(Build.VERSION_CODES.Q)
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO - initialise the token data store one time and reference it

        val userInfoRepository = UserInfoRepository(TokenDataStore(this))
        val userInfoViewModelFactory = UserInfoViewModelFactory(userInfoRepository)

        val discoverServiceRepository = DiscoverServiceRepository(TokenDataStore(this))
        val discoverServiceViewModelFactory = DiscoverServiceViewModelFactory(discoverServiceRepository)

        val healthServiceRepository = HealthServiceRepository(TokenDataStore(this), this)
        val healthServiceViewModelFactory = HealthServiceViewModelFactory(healthServiceRepository)

        enableEdgeToEdge()

//        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
//        sensorManager.registerListener(this, sensor!!, SensorManager.SENSOR_DELAY_FASTEST)

        setContent {
            FullHealthcareApplicationTheme() {
//                val counter = counter.collectAsState()
//                val permission = rememberPermissionState(permission = android.Manifest.permission.ACTIVITY_RECOGNITION)
//
//                LaunchedEffect(Unit) {
//                    permission.launchPermissionRequest()
//                }
//
//                when{
//                    permission.status.isGranted->{
//                        Text(text = counter.value.toString())
//                    }
//                    permission.status.shouldShowRationale->{
//                        Button(
//                            onClick = {
//                                permission.launchPermissionRequest()
//                            }
//                        ){
//                            Text(text = "Give Permission")
//
//                        }
//                    }
//                }

                RootNavigationGraph(
                    navController = rememberNavController(),
                    userInfoViewModelFactory = userInfoViewModelFactory,
                    discoverServiceViewModelFactory = discoverServiceViewModelFactory,
                    healthServiceViewModelFactory = healthServiceViewModelFactory,
                    tokenDataStore = TokenDataStore(this)
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(sensorEvent: SensorEvent?) {
        sensorEvent?.let { event ->
            if(event.sensor.type== Sensor.TYPE_STEP_DETECTOR){
                counter.update { it.plus(1) }
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int){
        TODO("Implement in the future")
    }
}

