package com.yukinoa.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.yukinoa.presentation.ui.components.NoteCard
import com.yukinoa.presentation.viewmodel.NoteListViewModel
import androidx.compose.foundation.background
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteListScreen(
    navController: NavController,
    viewModel: NoteListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }
    var noteToDelete by remember { mutableStateOf<com.yukinoa.domain.model.Note?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Notes") },
                actions = {
                    IconButton(onClick = { 
                        // 点击搜索按钮时激活搜索栏
                        viewModel.handleEvent(NoteListViewModel.Event.ActivateSearch)
                    }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("note_detail")
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Note")
            }
        }
    ) { paddingValues ->
        // 删除确认对话框
        if (showDeleteDialog && noteToDelete != null) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Delete Note") },
                text = { Text("Are you sure you want to delete this note?") },
                confirmButton = {
                    Button(
                        onClick = {
                            noteToDelete?.let { note ->
                                viewModel.handleEvent(NoteListViewModel.Event.DeleteNote(note))
                            }
                            showDeleteDialog = false
                            noteToDelete = null
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Delete", color = Color.White)
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            showDeleteDialog = false
                            noteToDelete = null
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }

        // 使用SearchBar组件，它会处理自己的激活状态
        SearchBar(
            query = searchQuery,
            onQueryChange = { query ->
                viewModel.handleEvent(NoteListViewModel.Event.SearchNotes(query))
            },
            onSearch = { query ->
                viewModel.handleEvent(NoteListViewModel.Event.SearchNotes(query))
            },
            active = state.isSearchActive,
            onActiveChange = { isActive ->
                if (!isActive) {
                    viewModel.handleEvent(NoteListViewModel.Event.ClearSearch)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search notes") },
            leadingIcon = if (state.isSearchActive) {
                {
                    IconButton(onClick = {
                        viewModel.handleEvent(NoteListViewModel.Event.ClearSearch)
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            } else {
                null
            },
            trailingIcon = if (state.isSearchActive && searchQuery.isNotEmpty()) {
                {
                    IconButton(onClick = {
                        viewModel.handleEvent(NoteListViewModel.Event.SearchNotes(""))
                    }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                }
            } else {
                null
            },
            colors = SearchBarDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            // 搜索激活时显示的内容
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                if (state.notes.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (state.isSearchActive) {
                                "No notes found for \"${searchQuery}\""
                            } else {
                                "No notes yet. Tap the + button to create one!"
                            },
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalItemSpacing = 8.dp,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(state.notes) { note ->
                            NoteCard(
                                note = note,
                                onClick = {
                                    navController.navigate("note_detail/${note.id}")
                                },
                                // 添加长按删除功能
                                onLongClick = {
                                    noteToDelete = note
                                    showDeleteDialog = true
                                }
                            )
                        }
                    }
                }
            }
        }
        
        // 非搜索状态时显示的内容
        if (!state.isSearchActive) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                if (state.notes.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "No notes yet. Tap the + button to create one!",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalItemSpacing = 8.dp,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(state.notes) { note ->
                            NoteCard(
                                note = note,
                                onClick = {
                                    navController.navigate("note_detail/${note.id}")
                                },
                                // 添加长按删除功能
                                onLongClick = {
                                    noteToDelete = note
                                    showDeleteDialog = true
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}