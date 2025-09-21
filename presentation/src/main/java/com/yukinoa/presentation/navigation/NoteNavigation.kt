package com.yukinoa.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yukinoa.presentation.ui.screens.NoteDetailScreen
import com.yukinoa.presentation.ui.screens.NoteListScreen

@Composable
fun NoteAppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "note_list"
    ) {
        composable("note_list") {
            NoteListScreen(navController)
        }

        composable("note_detail") {
            NoteDetailScreen(navController)
        }

        composable("note_detail/{noteId}") { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId")
            NoteDetailScreen(navController)
        }
    }
}