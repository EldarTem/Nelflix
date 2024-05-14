package com.kpu.shared.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import com.kpu.shared.presentation.ui.navigation.NavigationItem
import com.kpu.shared.ui.screens.details.DetailsScreen
import com.kpu.shared.ui.screens.favorites.FavoritesScreen
import com.kpu.shared.ui.screens.home.HomeScreen
import com.kpu.shared.ui.screens.settings.SettingsScreen
import com.kpu.shared.utils.WindowSize
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.path

@Composable
fun Navigation(
    navigator: Navigator,
    windowSize: WindowSize,
    paddingValues: PaddingValues = PaddingValues()
) {
    NavHost(navigator = navigator, initialRoute = NavigationItem.Home.route) {
        scene(NavigationItem.Home.route) {
            HomeScreen(
                navigator = navigator,
                windowSize = windowSize,
                paddingValues = paddingValues
            )
        }

        scene(NavigationItem.Favorites.route) {
            FavoritesScreen(navigator = navigator)
        }

        scene(NavigationItem.Settings.route) {
            SettingsScreen()
        }

        scene(NavigationItem.Details.route) { backStackEntry ->
            backStackEntry.path<Int>("id")?.let { movieId ->
                DetailsScreen(navigator = navigator, windowSize = windowSize, movieId = movieId)
            }
        }
    }
}
