package com.example.fullhealthcareapplication.ui.graphs

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.fullhealthcareapplication.data.factory.HealthServiceViewModelFactory
import com.example.fullhealthcareapplication.data.factory.UserInfoViewModelFactory
import com.example.fullhealthcareapplication.ui.graphs.Graph
import com.example.fullhealthcareapplication.ui.screens.LogInScreen
import com.example.fullhealthcareapplication.ui.screens.OnboardingScreen
import com.example.fullhealthcareapplication.ui.screens.SignUpScreen
import com.example.fullhealthcareapplication.ui.screens.WelcomeScreen
import com.google.accompanist.pager.ExperimentalPagerApi

@OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    userInfoViewModelFactory: UserInfoViewModelFactory,
    healthServiceViewModelFactory: HealthServiceViewModelFactory
){
    navigation(
        route = Graph.AUTHENTICATION,
        startDestination = AuthScreen.Onboarding.route // To test the onboarding
    ){
        composable(route = AuthScreen.Login.route){
            LogInScreen(
                userInfoViewModelFactory = userInfoViewModelFactory,
                healthServiceViewModelFactory = healthServiceViewModelFactory,
                toWelcome = {
                    navController.navigate(AuthScreen.Welcome.route)
                },
                toHome = {
                    navController.navigate(Graph.HOME) {
                        popUpTo(Graph.AUTHENTICATION) { inclusive = true }
                    }
                }
            )
        }
        composable(route = AuthScreen.SignUp.route){
            SignUpScreen(
                viewModelFactory = userInfoViewModelFactory,
                toWelcome = {
                    navController.navigate(AuthScreen.Welcome.route)
                },
                toLogin = {
                    navController.navigate(AuthScreen.Login.route)
                }
            )
        }
        composable(route = AuthScreen.Welcome.route){
            WelcomeScreen(
                toLogin = {
                    navController.navigate(AuthScreen.Login.route)
                },
                toSignUp = {
                    navController.navigate(AuthScreen.SignUp.route)
                }
            )
        }
        composable(route = AuthScreen.Onboarding.route){
            OnboardingScreen()
        }
    }
}

sealed class AuthScreen(val route: String){
    object Welcome: AuthScreen(route = "WELCOME")
    object Login: AuthScreen(route = "LOGIN")
    object SignUp: AuthScreen(route = "SIGN_UP")
    object Onboarding: AuthScreen(route = "ONBOARDING")
}