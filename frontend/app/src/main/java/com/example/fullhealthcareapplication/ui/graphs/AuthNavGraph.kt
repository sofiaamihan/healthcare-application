package com.example.fullhealthcareapplication.ui.graphs

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
        startDestination = AuthScreen.Welcome.route // To test the onboarding
    ){
        composable(
            route = AuthScreen.Login.route,
            enterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.End, tween(700)
                )
            }, exitTransition = {
                return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(700)
                )
            }, popEnterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.End, tween(700)
                )
            }
        ){
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
        composable(
            route = AuthScreen.SignUp.route,
            enterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.End, tween(700)
                )
            }, exitTransition = {
                return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(700)
                )
            }, popEnterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.End, tween(700)
                )
            }
            ){
            SignUpScreen(
                viewModelFactory = userInfoViewModelFactory,
                toWelcome = {
                    navController.navigate(AuthScreen.Welcome.route)
                },
                toLogin = {
                    navController.navigate(AuthScreen.Login.route)
                },
                healthServiceViewModelFactory = healthServiceViewModelFactory,
            ){ nric, role ->
                navController.navigate("${AuthScreen.Onboarding.route}/$nric/$role")
            }
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
        composable(route = "${AuthScreen.Onboarding.route}/{nric}/{role}"){ backStackEntry ->
            val nric: String = backStackEntry.arguments?.getString("nric") ?: ""
            val role: String = backStackEntry.arguments?.getString("role") ?: ""
            OnboardingScreen(
                healthServiceViewModelFactory = healthServiceViewModelFactory,
                toLogin = {
                    navController.navigate(AuthScreen.Login.route)
                },
                nric = nric,
                role = role
            )
        }
    }
}

sealed class AuthScreen(val route: String){
    object Welcome: AuthScreen(route = "WELCOME")
    object Login: AuthScreen(route = "LOGIN")
    object SignUp: AuthScreen(route = "SIGN_UP")
    object Onboarding: AuthScreen(route = "ONBOARDING")
}