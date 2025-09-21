package com.yukinoa.domain.repository

import com.yukinoa.core.Result
import com.yukinoa.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getAllNotes(): Flow<Result<List<Note>>>
    fun getNoteById(id: Long): Flow<Result<Note>>
    suspend fun insertNote(note: Note): Result<Long>
    suspend fun updateNote(note: Note): Result<Int>
    suspend fun deleteNote(note: Note): Result<Int>
    fun searchNotes(query: String): Flow<Result<List<Note>>>
    fun getPinnedNotes(): Flow<Result<List<Note>>>
}