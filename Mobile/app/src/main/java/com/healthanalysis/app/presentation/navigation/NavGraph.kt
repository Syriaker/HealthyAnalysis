package com.healthanalysis.app.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.healthanalysis.app.presentation.screens.analysis.AnalysisScreen
import com.healthanalysis.app.presentation.screens.food.FoodScreen
import com.healthanalysis.app.presentation.screens.home.HomeScreen
import com.healthanalysis.app.presentation.screens.profile.ProfileScreen
import com.healthanalysis.app.presentation.screens.tips.TipsScreen

@Composable
fun MainNavGraph(
    navController: NavHostController,
    onLogout: () -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentRoute = currentRoute,
                onItemSelected = { screen ->
                    navController.navigate(screen.route) {
                        popUpTo(Screen.Home.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Home.route) {
                HomeScreen()
            }
            composable(Screen.Food.route) {
                FoodScreen()
            }
            composable(Screen.Analysis.route) {
                AnalysisScreen()
            }
            composable(Screen.Tips.route) {
                TipsScreen()
            }
            composable(Screen.Profile.route) {
                ProfileScreen(onLogout = onLogout)
            }
        }
    }
}
