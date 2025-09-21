package com.yukinoa.presentation.theme

import androidx.compose.ui.graphics.Color

// Light Theme Colors
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

// Dark Theme Colors
val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// Custom Colors for Light Theme
val LightNoteColors = listOf(
    Color(0xFFf5f5f5), // 更美观的默认浅灰色背景，替代之前的白色
    Color(0xFFF8BBD9), // Pink
    Color(0xFFFFE0B2), // Orange
    Color(0xFFFFF59D), // Yellow
    Color(0xFFC8E6C9), // Green
    Color(0xFFB3E5FC), // Blue
    Color(0xFFD1C4E9), // Purple
    Color(0xFFFFCCBC)  // Red
)

// Custom Colors for Dark Theme
val DarkNoteColors = listOf(
    Color(0xFF121212), // 深色主题默认背景
    Color(0xFF5A1A30), // Pink
    Color(0xFF4A3A10), // Orange
    Color(0xFF4A4200), // Yellow
    Color(0xFF1E3E1F), // Green
    Color(0xFF00334A), // Blue
    Color(0xFF301E4A), // Purple
    Color(0xFF4A1E00)  // Red
)

val NoteColors = LightNoteColors