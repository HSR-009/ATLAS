package com.atlas.app.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.atlas.app.ui.screens.*
import com.atlas.app.viewmodel.AtlasViewModel

object Routes {
    const val SPLASH         = "splash"
    const val ROLE_SELECTION = "role_selection/{mode}"
    const val AUTH           = "auth/{role}/{mode}"
    const val HOME           = "home"
    const val EVENT_DETAIL   = "event_detail/{eventId}"
    const val ACTIVITY_LOG   = "activity_log"
    const val PROFILE        = "profile"

    fun roleSelection(mode: String) = "role_selection/$mode"
    fun auth(role: String, mode: String) = "auth/$role/$mode"
    fun eventDetail(eventId: String) = "event_detail/$eventId"
}

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val vm: AtlasViewModel = viewModel()

    NavHost(navController = navController, startDestination = Routes.SPLASH) {

        composable(Routes.SPLASH) {
            SplashScreen(
                onLoginClick        = { navController.navigate(Routes.roleSelection("login")) },
                onCreateAccountClick = { navController.navigate(Routes.roleSelection("signup")) }
            )
        }

        composable(
            route = Routes.ROLE_SELECTION,
            arguments = listOf(navArgument("mode") { type = NavType.StringType })
        ) { back ->
            val mode = back.arguments?.getString("mode") ?: "login"
            RoleSelectionScreen(
                mode     = mode,
                onBack   = { navController.popBackStack() },
                onContinue = { role ->
                    vm.userRole = role
                    navController.navigate(Routes.auth(role.name.lowercase(), mode))
                }
            )
        }

        composable(
            route = Routes.AUTH,
            arguments = listOf(
                navArgument("role") { type = NavType.StringType },
                navArgument("mode") { type = NavType.StringType }
            )
        ) { back ->
            val role = back.arguments?.getString("role") ?: "organizer"
            val mode = back.arguments?.getString("mode") ?: "login"
            AuthScreen(
                role   = role,
                mode   = mode,
                onBack = { navController.popBackStack() },
                onSignUp = { name, email, phone ->
                    vm.saveUserProfile(name, email, phone)
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                },
                onLogin = { email ->
                    vm.loginUser(email)
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                vm           = vm,
                onEventClick = { navController.navigate(Routes.eventDetail(it)) },
                onLogsClick  = { navController.navigate(Routes.ACTIVITY_LOG) },
                onProfileClick = { navController.navigate(Routes.PROFILE) }
            )
        }

        composable(
            route = Routes.EVENT_DETAIL,
            arguments = listOf(navArgument("eventId") { type = NavType.StringType })
        ) { back ->
            val eventId = back.arguments?.getString("eventId") ?: "e1"
            EventDetailScreen(
                eventId = eventId,
                vm      = vm,
                onBack  = { navController.popBackStack() }
            )
        }

        composable(Routes.ACTIVITY_LOG) {
            ActivityLogScreen(
                vm             = vm,
                onEventsClick  = { navController.navigate(Routes.HOME) { launchSingleTop = true } },
                onProfileClick = { navController.navigate(Routes.PROFILE) }
            )
        }

        composable(Routes.PROFILE) {
            ProfileScreen(
                vm            = vm,
                onEventsClick = { navController.navigate(Routes.HOME) { launchSingleTop = true } },
                onLogsClick   = { navController.navigate(Routes.ACTIVITY_LOG) },
                onLogOut      = {
                    vm.clearAllData()
                    navController.navigate(Routes.SPLASH) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
