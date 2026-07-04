package com.flamedate.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String) {
    data object Discover : Screen("discover")
    data object Matches : Screen("matches")
    data object Profile : Screen("profile")
    data object ChatDetail : Screen("chat/{matchId}/{matchName}/{matchPhoto}") {
        fun createRoute(matchId: String, matchName: String, matchPhoto: String): String {
            return "chat/$matchId/$matchName/$matchPhoto"
        }
    }
}

enum class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    Discover(
        route = Screen.Discover.route,
        label = "Discover",
        icon = Icons.Default.FavoriteBorder
    ),
    Matches(
        route = Screen.Matches.route,
        label = "Matches",
        icon = Icons.AutoMirrored.Filled.Chat
    ),
    Profile(
        route = Screen.Profile.route,
        label = "Profile",
        icon = Icons.Default.Person
    )
}
