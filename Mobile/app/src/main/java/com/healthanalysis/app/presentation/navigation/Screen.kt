package com.healthanalysis.app.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.SwapVert
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String) {
    data object Auth : Screen("auth")
    data object Home : Screen("home")
    data object Food : Screen("food")
    data object Analysis : Screen("analysis")
    data object Tips : Screen("tips")
    data object Profile : Screen("profile")
}

data class BottomNavItem(
    val screen: Screen,
    val label: String,
    val icon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(Screen.Home, "Главная", Icons.Outlined.Home),
    BottomNavItem(Screen.Food, "Питание", Icons.Outlined.FavoriteBorder),
    BottomNavItem(Screen.Analysis, "Анализы", Icons.Outlined.WaterDrop),
    BottomNavItem(Screen.Tips, "Советы", Icons.Outlined.SwapVert),
    BottomNavItem(Screen.Profile, "Профиль", Icons.Outlined.Person)
)
