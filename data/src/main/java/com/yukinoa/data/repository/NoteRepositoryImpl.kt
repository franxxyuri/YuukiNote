package com.yukinoa.data.repository

import com.yukinoa.core.Result
import com.yukinoa.data.database.NoteDao
import com.yukinoa.data.model.toDomainModel
import com.yukinoa.data.model.toEntity
import com.yukinoa.domain.model.Note
import com.yukinoa.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao
) : NoteRepository {

    override fun getAllNotes(): Flow<Result<List<Note>>> =
        noteDao.getAllNotes()
            .map { entities -> Result.Success(entities.map { it.toDomainModel() }) }
            .map { result -> result as Result<List<Note>> }

    override fun getNoteById(id: Long): Flow<Result<Note>> =
        noteDao.getNoteById(id)
            .map { entity ->
                if (entity != null) {
                    Result.Success(entity.toDomainModel())
                } else {
                    Result.Error(IllegalArgumentException("Note not found"))
                }
            }

    override suspend fun insertNote(note: Note): Result<Long> {
        return try {
            val noteWithTimestamp = note.copy(
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
            val id = noteDao.insertNote(noteWithTimestamp.toEntity())
            Result.Success(id)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateNote(note: Note): Result<Int> {
        return try {
            val noteWithTimestamp = note.copy(updatedAt = LocalDateTime.now())
            val rows = noteDao.updateNote(noteWithTimestamp.toEntity())
            Result.Success(rows)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun deleteNote(note: Note): Result<Int> {
        return try {
            val rows = noteDao.deleteNote(note.toEntity())
            Result.Success(rows)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override fun searchNotes(query: String): Flow<Result<List<Note>>> =
        noteDao.searchNotes(query)
            .map { entities -> Result.Success(entities.map { it.toDomainModel() }) }
            .map { result -> result as Result<List<Note>> }

    override fun getPinnedNotes(): Flow<Result<List<Note>>> =
        noteDao.getPinnedNotes()
            .map { entities -> Result.Success(entities.map { it.toDomainModel() }) }
            .map { result -> result as Result<List<Note>> }
}