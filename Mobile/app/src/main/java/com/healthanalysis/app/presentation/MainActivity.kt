package com.healthanalysis.app.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.healthanalysis.app.data.local.TokenManager
import com.healthanalysis.app.presentation.navigation.AppNavGraph
import com.healthanalysis.app.presentation.navigation.Screen
import com.healthanalysis.app.presentation.theme.HealthAnalysisTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HealthAnalysisTheme {
                val isLoggedIn by tokenManager.isLoggedIn.collectAsState(initial = null)
                if (isLoggedIn != null) {
                    MainApp(isLoggedIn = isLoggedIn!!)
                }
            }
        }
    }
}

@Composable
private fun MainApp(isLoggedIn: Boolean) {
    val navController = rememberNavController()

    LaunchedEffect(isLoggedIn) {
        if (!isLoggedIn) {
            navController.navigate(Screen.Login.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    AppNavGraph(
        navController = navController,
        startLoggedIn = isLoggedIn
    )
}
