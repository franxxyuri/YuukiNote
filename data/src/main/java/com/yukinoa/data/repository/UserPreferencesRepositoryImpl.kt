package com.yukinoa.data.repository

import android.content.SharedPreferences
import com.yukinoa.domain.model.UserPreferences
import com.yukinoa.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : UserPreferencesRepository {

    private val _userPreferences = MutableStateFlow(loadUserPreferences())
    val userPreferencesFlow: Flow<UserPreferences> = _userPreferences.asStateFlow()

    override fun getUserPreferences(): Flow<UserPreferences> = userPreferencesFlow

    override suspend fun updateUserPreferences(preferences: UserPreferences) {
        saveUserPreferences(preferences)
        _userPreferences.value = preferences
    }

    private fun loadUserPreferences(): UserPreferences {
        return UserPreferences(
            titleFontSize = sharedPreferences.getFloat(PREF_TITLE_FONT_SIZE, UserPreferences.DEFAULT.titleFontSize),
            contentFontSize = sharedPreferences.getFloat(PREF_CONTENT_FONT_SIZE, UserPreferences.DEFAULT.contentFontSize),
            titleColor = sharedPreferences.getInt(PREF_TITLE_COLOR, UserPreferences.DEFAULT.titleColor),
            contentColor = sharedPreferences.getInt(PREF_CONTENT_COLOR, UserPreferences.DEFAULT.contentColor)
        )
    }

    private fun saveUserPreferences(preferences: UserPreferences) {
        with(sharedPreferences.edit()) {
            putFloat(PREF_TITLE_FONT_SIZE, preferences.titleFontSize)
            putFloat(PREF_CONTENT_FONT_SIZE, preferences.contentFontSize)
            putInt(PREF_TITLE_COLOR, preferences.titleColor)
            putInt(PREF_CONTENT_COLOR, preferences.contentColor)
            apply()
        }
    }

    companion object {
        private const val PREF_TITLE_FONT_SIZE = "title_font_size"
        private const val PREF_CONTENT_FONT_SIZE = "content_font_size"
        private const val PREF_TITLE_COLOR = "title_color"
        private const val PREF_CONTENT_COLOR = "content_color"
    }
}