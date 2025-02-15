package com.example.fullhealthcareapplication

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO - intialise the token data store one time and reference it

        val userInfoRepository = UserInfoRepository(TokenDataStore(this))
        val userInfoViewModelFactory = UserInfoViewModelFactory(userInfoRepository)

        val discoverServiceRepository = DiscoverServiceRepository(TokenDataStore(this))
        val discoverServiceViewModelFactory = DiscoverServiceViewModelFactory(discoverServiceRepository)

        val healthServiceRepository = HealthServiceRepository(TokenDataStore(this), this)
        val healthServiceViewModelFactory = HealthServiceViewModelFactory(healthServiceRepository)

        enableEdgeToEdge()
        setContent {
            FullHealthcareApplicationTheme() {
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
}

