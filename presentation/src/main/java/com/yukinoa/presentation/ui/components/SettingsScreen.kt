package com.yukinoa.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yukinoa.presentation.viewmodel.UserPreferencesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: UserPreferencesViewModel = hiltViewModel()
) {
    val userPreferences by viewModel.userPreferences.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // 显示设置部分标题
            Text(
                text = "Display Settings",
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            // 标题字体大小设置
            Text(
                text = "Title Font Size: ${userPreferences.titleFontSize.toInt()}sp",
                fontSize = 18.sp
            )
            Slider(
                value = userPreferences.titleFontSize,
                onValueChange = { viewModel.updateTitleFontSize(it) },
                valueRange = 12f..48f,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 内容字体大小设置
            Text(
                text = "Content Font Size: ${userPreferences.contentFontSize.toInt()}sp",
                fontSize = 18.sp
            )
            Slider(
                value = userPreferences.contentFontSize,
                onValueChange = { viewModel.updateContentFontSize(it) },
                valueRange = 8f..32f,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 标题颜色设置
            Text(
                text = "Title Color",
                fontSize = 18.sp
            )
            ColorPicker(
                selectedColor = userPreferences.titleColor,
                onColorSelected = { colorIndex -> viewModel.updateTitleColor(colorIndex) }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 内容颜色设置
            Text(
                text = "Content Color",
                fontSize = 18.sp
            )
            ColorPicker(
                selectedColor = userPreferences.contentColor,
                onColorSelected = { colorIndex -> viewModel.updateContentColor(colorIndex) }
            )
            Spacer(modifier = Modifier.height(32.dp))
            
            // 云同步设置部分（预留）
            Text(
                text = "Cloud Settings",
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Text(
                text = "Cloud sync options will be available in a future version",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onNavigateBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back")
            }
        }
    }
}