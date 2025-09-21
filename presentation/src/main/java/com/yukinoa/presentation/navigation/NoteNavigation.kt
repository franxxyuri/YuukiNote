package com.yukinoa.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yukinoa.presentation.ui.screens.NoteDetailScreen
import com.yukinoa.presentation.ui.screens.TabletAdaptiveScreen
import com.yukinoa.presentation.ui.components.SettingsScreen

@Composable
fun NoteAppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "notes"
    ) {
        composable("notes") {
            TabletAdaptiveScreen(navController)
        }
        composable(
            "note_detail/{noteId}",
            arguments = listOf(navArgument("noteId") { defaultValue = "0" })
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId") ?: "0"
            NoteDetailScreen(navController)
        }
        composable("note_detail") {
            NoteDetailScreen(navController)
        }
        composable("settings") {
            SettingsScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}