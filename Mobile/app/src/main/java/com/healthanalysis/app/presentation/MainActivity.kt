package com.healthanalysis.app.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.healthanalysis.app.presentation.navigation.MainNavGraph
import com.healthanalysis.app.presentation.theme.HealthAnalysisTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HealthAnalysisTheme {
                MainApp()
            }
        }
    }
}

@Composable
private fun MainApp() {
    val navController = rememberNavController()
    MainNavGraph(
        navController = navController,
        onLogout = { }
    )
}
