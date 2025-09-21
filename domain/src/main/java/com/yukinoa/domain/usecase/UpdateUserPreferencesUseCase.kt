package com.yukinoa.domain.usecase

import com.yukinoa.domain.model.UserPreferences
import com.yukinoa.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class UpdateUserPreferencesUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(preferences: UserPreferences) {
        userPreferencesRepository.updateUserPreferences(preferences)
    }
}