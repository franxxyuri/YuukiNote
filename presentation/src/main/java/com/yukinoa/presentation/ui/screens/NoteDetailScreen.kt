package com.yukinoa.presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.background
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.yukinoa.presentation.ui.components.NoteColorPicker
import com.yukinoa.presentation.theme.LocalNoteColors
import com.yukinoa.presentation.viewmodel.NoteDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(
    navController: NavController,
    viewModel: NoteDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val noteColors = LocalNoteColors.current

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) {
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Note") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.handleEvent(NoteDetailViewModel.Event.TogglePin(!state.note.isPinned))
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.PushPin,
                            contentDescription = if (state.note.isPinned) "Unpin" else "Pin",
                            tint = if (state.note.isPinned) MaterialTheme.colorScheme.primary else Color.Gray
                        )
                    }
                    IconButton(
                        onClick = {
                            viewModel.handleEvent(NoteDetailViewModel.Event.SaveNote)
                        }
                    ) {
                        Icon(Icons.Default.Check, contentDescription = "Save")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    if (state.note.color >= 0 && state.note.color < noteColors.size) {
                        noteColors[state.note.color]
                    } else {
                        MaterialTheme.colorScheme.background
                    }
                )
        ) {
            BasicTextField(
                value = state.note.title,
                onValueChange = { title ->
                    viewModel.handleEvent(NoteDetailViewModel.Event.UpdateTitle(title))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textStyle = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                decorationBox = { innerTextField ->
                    if (state.note.title.isEmpty()) {
                        Text(
                            "Title",
                            style = TextStyle(
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        )
                    }
                    innerTextField()
                }
            )

            NoteColorPicker(
                selectedColor = state.note.color,
                onColorSelected = { colorIndex ->
                    viewModel.handleEvent(NoteDetailViewModel.Event.UpdateColor(colorIndex))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            BasicTextField(
                value = state.note.content,
                onValueChange = { content ->
                    viewModel.handleEvent(NoteDetailViewModel.Event.UpdateContent(content))
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                decorationBox = { innerTextField ->
                    if (state.note.content.isEmpty()) {
                        Text(
                            "Start writing...",
                            style = TextStyle(
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        )
                    }
                    innerTextField()
                }
            )
        }
    }
}