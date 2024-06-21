package com.example.bmitracker.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bmitracker.screens.onboard.OnBoardScreen
import com.example.bmitracker.screens.profile.BasicProfileScreen
import com.example.bmitracker.screens.home.HomeScreen


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(
    viewModel: NavigationViewModel = hiltViewModel()
) {
    val controller = rememberNavController()
    val isLoggedIn = viewModel.isLoggedIn
    NavHost(navController = controller, startDestination = if (isLoggedIn) Screen.Home else Screen.OnBoard) {
        composable<Screen.BasicProfile> {
            BasicProfileScreen(
                goToHome = {
                    controller.navigate(Screen.Home) {
                        popUpTo(0){
                            inclusive=true
                        }
                        launchSingleTop = true
                    }
                },
                onBack = {
                    controller.navigateUp()
                }
            )
        }

        composable<Screen.OnBoard> {
            OnBoardScreen(
                navigateToProfile = {
                    controller.navigate(Screen.BasicProfile)
                }
            )
        }

        composable<Screen.Home> {
            HomeScreen(
                goToProfile = {
                    controller.navigate(Screen.BasicProfile)
                }
            )
        }
    }
}



