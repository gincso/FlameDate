package com.flamedate.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.flamedate.app.ui.chat.ChatDetailScreen
import com.flamedate.app.ui.discover.DiscoverScreen
import com.flamedate.app.ui.matches.MatchesScreen
import com.flamedate.app.ui.navigation.BottomNavItem
import com.flamedate.app.ui.navigation.Screen
import com.flamedate.app.ui.profile.ProfileScreen
import com.flamedate.app.ui.theme.FlameDateTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlameDateTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FlameDateMain()
                }
            }
        }
    }
}

@Composable
fun FlameDateMain() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomNavItems = listOf(
        BottomNavItem.Discover,
        BottomNavItem.Matches,
        BottomNavItem.Profile
    )

    Scaffold(
        bottomBar = {
            val showBottomBar = currentDestination?.route in bottomNavItems.map { it.route }
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.label
                                )
                            },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Discover.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Discover.route) {
                DiscoverScreen()
            }

            composable(Screen.Matches.route) {
                MatchesScreen(
                    onMatchClick = { matchId, matchName, matchPhoto ->
                        navController.navigate(Screen.ChatDetail.createRoute(matchId, matchName, matchPhoto))
                    }
                )
            }

            composable(Screen.Profile.route) {
                ProfileScreen()
            }

            composable(
                route = Screen.ChatDetail.route,
                arguments = listOf(
                    navArgument("matchId") { type = NavType.StringType },
                    navArgument("matchName") { type = NavType.StringType },
                    navArgument("matchPhoto") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val matchId = backStackEntry.arguments?.getString("matchId") ?: return@composable
                val matchName = backStackEntry.arguments?.getString("matchName") ?: ""
                val matchPhoto = backStackEntry.arguments?.getString("matchPhoto") ?: ""
                ChatDetailScreen(
                    matchId = matchId,
                    matchName = matchName,
                    matchPhoto = matchPhoto,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
