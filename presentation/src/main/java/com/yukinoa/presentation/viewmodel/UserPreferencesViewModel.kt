package com.yukinoa.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yukinoa.domain.model.UserPreferences
import com.yukinoa.domain.usecase.GetUserPreferencesUseCase
import com.yukinoa.domain.usecase.UpdateUserPreferencesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserPreferencesViewModel @Inject constructor(
    private val getUserPreferencesUseCase: GetUserPreferencesUseCase,
    private val updateUserPreferencesUseCase: UpdateUserPreferencesUseCase
) : ViewModel() {

    private val _userPreferences = MutableStateFlow(UserPreferences.DEFAULT)
    val userPreferences = _userPreferences.asStateFlow()

    init {
        loadUserPreferences()
    }

    private fun loadUserPreferences() {
        viewModelScope.launch {
            getUserPreferencesUseCase().collectLatest { preferences ->
                _userPreferences.value = preferences
            }
        }
    }

    fun updateTitleFontSize(fontSize: Float) {
        viewModelScope.launch {
            val updatedPreferences = _userPreferences.value.copy(titleFontSize = fontSize)
            updateUserPreferencesUseCase(updatedPreferences)
        }
    }

    fun updateContentFontSize(fontSize: Float) {
        viewModelScope.launch {
            val updatedPreferences = _userPreferences.value.copy(contentFontSize = fontSize)
            updateUserPreferencesUseCase(updatedPreferences)
        }
    }

    fun updateTitleColor(colorIndex: Int) {
        viewModelScope.launch {
            val updatedPreferences = _userPreferences.value.copy(titleColor = colorIndex)
            updateUserPreferencesUseCase(updatedPreferences)
        }
    }

    fun updateContentColor(colorIndex: Int) {
        viewModelScope.launch {
            val updatedPreferences = _userPreferences.value.copy(contentColor = colorIndex)
            updateUserPreferencesUseCase(updatedPreferences)
        }
    }
}