package com.example.fullhealthcareapplication

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraExecutor
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
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
import com.example.fullhealthcareapplication.ui.components.CameraView
import com.example.fullhealthcareapplication.ui.graphs.RootNavigationGraph
import com.example.fullhealthcareapplication.ui.theme.FullHealthcareApplicationTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : ComponentActivity() , SensorEventListener{

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    
    private var shouldShowCamera: MutableState<Boolean> = mutableStateOf(false)

    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){ isGranted ->
        if(isGranted){
            Log.i("User", "Permission Granted")
            shouldShowCamera.value = true
        } else {
            Log.i("User", "Permission Denied")
        }
    }

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
//                if (shouldShowCamera.value){
//                    CameraView(
//                        outputDirectory = outputDirectory,
//                        executor = cameraExecutor,
//                        onImageCaptured = ::handleImageCapture,
//                        onError = { Log.e("User", "View Error:", it) }
//                    )
//                }
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
        requestCameraPermission()
        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()

    }

//    override fun onDestroy() {
//        super.onDestroy()
//        sensorManager.unregisterListener(this)
//    }

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

    private fun requestCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                Log.i("User", "Permission previously granted")
                shouldShowCamera.value = true
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                android.Manifest.permission.CAMERA
            ) -> Log.i ("User", "Show camera permissions dialog")

            else -> requestPermissionsLauncher.launch(android.Manifest.permission.CAMERA)
        }
    }

    private fun handleImageCapture(uri: Uri){
        Log.i("User", "Image captured: $uri")
        shouldShowCamera.value = false
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull().let {
            File(it, resources.getString(R.string.app_name)).apply{ mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else filesDir
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

}

