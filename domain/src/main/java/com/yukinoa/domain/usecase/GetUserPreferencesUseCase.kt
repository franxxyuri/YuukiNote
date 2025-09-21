package com.yukinoa.domain.usecase

import com.yukinoa.domain.model.UserPreferences
import com.yukinoa.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserPreferencesUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    operator fun invoke(): Flow<UserPreferences> {
        return userPreferencesRepository.getUserPreferences()
    }
}