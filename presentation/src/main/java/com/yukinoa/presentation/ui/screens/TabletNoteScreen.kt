package com.yukinoa.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.yukinoa.domain.model.Note
import com.yukinoa.presentation.ui.components.NoteCard
import com.yukinoa.presentation.viewmodel.NoteListViewModel

@Composable
fun TabletAdaptiveScreen(
    navController: NavController,
    noteListViewModel: NoteListViewModel = hiltViewModel()
) {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp

    // 判断是否为大屏幕设备（如平板）
    val isTablet = screenWidthDp >= 600

    if (isTablet) {
        // 平板布局 - 双栏视图
        TwoPaneLayout(navController, noteListViewModel)
    } else {
        // 手机布局 - 单栏视图
        NoteListScreen(navController, noteListViewModel)
    }
}

@Composable
fun TwoPaneLayout(
    navController: NavController,
    viewModel: NoteListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var selectedNote by remember { mutableStateOf<Note?>(null) }

    Row(modifier = Modifier.fillMaxSize()) {
        // 左侧笔记列表 (占屏幕的40%)
        NoteListPane(
            notes = state.notes,
            onNoteSelected = { note: Note ->
                selectedNote = note
            },
            onAddNote = {
                navController.navigate("note_detail")
            },
            modifier = Modifier
                .weight(0.4f)
                .fillMaxHeight()
        )

        // 分割线
        Divider(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.01f),
            color = MaterialTheme.colorScheme.outline
        )

        // 右侧笔记详情 (占屏幕的60%)
        NoteDetailPane(
            note = selectedNote,
            navController = navController,
            modifier = Modifier
                .weight(0.59f)
                .fillMaxHeight()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteListPane(
    notes: List<Note>,
    onNoteSelected: (Note) -> Unit,
    onAddNote: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedNote by remember { mutableStateOf<Note?>(null) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Yuuki Notes") },
                actions = {
                    IconButton(onClick = { /* 搜索功能 */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                    IconButton(onClick = { /* 设置功能 */ }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddNote
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Note")
            }
        },
        modifier = modifier
    ) { paddingValues ->
        if (notes.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "No notes yet. Tap the + button to create one!",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalItemSpacing = 8.dp,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(notes) { note ->
                    NoteCard(
                        note = note,
                        onClick = {
                            selectedNote = note
                            onNoteSelected(note)
                        },
                        onLongClick = { /* 删除功能 */ }
                    )
                }
            }
        }
    }
}

@Composable
fun NoteDetailPane(
    note: Note?,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    // 显示选中的笔记详情或提示信息
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (note != null) {
            Text(
                text = "Note detail view would appear here for note: ${note.title}",
                style = MaterialTheme.typography.bodyLarge
            )
        } else {
            Text(
                text = "Select a note to view details",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}