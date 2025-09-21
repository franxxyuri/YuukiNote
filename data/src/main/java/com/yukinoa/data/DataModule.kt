package com.yukinoa.data

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.yukinoa.data.database.NoteDatabase
import com.yukinoa.data.repository.NoteRepositoryImpl
import com.yukinoa.data.repository.UserPreferencesRepositoryImpl
import com.yukinoa.domain.repository.NoteRepository
import com.yukinoa.domain.repository.UserPreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(@ApplicationContext context: Context): NoteDatabase {
        return Room.databaseBuilder(
            context,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(noteDatabase: NoteDatabase): NoteRepository {
        return NoteRepositoryImpl(noteDatabase.noteDao())
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("user_preferences", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideUserPreferencesRepository(sharedPreferences: SharedPreferences): UserPreferencesRepository {
        return UserPreferencesRepositoryImpl(sharedPreferences)
    }
}