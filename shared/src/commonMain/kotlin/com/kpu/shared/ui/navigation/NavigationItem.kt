package com.kpu.shared.presentation.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationItem(
    val route: String,
    val title: String,
    val icon: ImageVector?
) {

    object Home : NavigationItem("/home", "Домашняя", Icons.Rounded.Home)
    object Favorites : NavigationItem("/favorites", "Избранное", Icons.Rounded.Favorite)
    object Settings : NavigationItem("/settings", "Настройки", Icons.Rounded.Settings)
    object Details : NavigationItem("/details/{id:[0-9]+}", "Подробнее", null)
}
