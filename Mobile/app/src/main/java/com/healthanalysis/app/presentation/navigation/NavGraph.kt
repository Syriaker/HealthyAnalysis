package com.healthanalysis.app.presentation.navigation

import android.net.Uri
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.healthanalysis.app.presentation.screens.analysis.AnalysisScreen
import com.healthanalysis.app.presentation.screens.auth.LoginScreen
import com.healthanalysis.app.presentation.screens.auth.RegisterScreen
import com.healthanalysis.app.presentation.screens.auth.VerifyScreen
import com.healthanalysis.app.presentation.screens.food.FoodScreen
import com.healthanalysis.app.presentation.screens.home.HomeScreen
import com.healthanalysis.app.presentation.screens.profile.ProfileScreen
import com.healthanalysis.app.presentation.screens.tips.TipsScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    startLoggedIn: Boolean
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val mainRoutes = remember {
        setOf(
            Screen.Home.route,
            Screen.Food.route,
            Screen.Analysis.route,
            Screen.Tips.route,
            Screen.Profile.route
        )
    }

    Scaffold(
        bottomBar = {
            if (currentRoute in mainRoutes) {
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
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = if (startLoggedIn) Screen.Home.route else Screen.Login.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Login.route) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onNavigateToRegister = { navController.navigate(Screen.Register.route) }
                )
            }
            composable(Screen.Register.route) {
                RegisterScreen(
                    onRegisterSuccess = { email ->
                        navController.navigate("${Screen.Verify.route}/${Uri.encode(email)}")
                    },
                    onNavigateToLogin = { navController.popBackStack() }
                )
            }
            composable(
                route = "${Screen.Verify.route}/{email}",
                arguments = listOf(navArgument("email") { type = NavType.StringType })
            ) { backStack ->
                val email = backStack.arguments?.getString("email") ?: ""
                VerifyScreen(
                    email = email,
                    onVerifySuccess = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Register.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.Home.route) { HomeScreen() }
            composable(Screen.Food.route) { FoodScreen() }
            composable(Screen.Analysis.route) { AnalysisScreen() }
            composable(Screen.Tips.route) { TipsScreen() }
            composable(Screen.Profile.route) {
                ProfileScreen(onLogout = {})
            }
        }
    }
}
