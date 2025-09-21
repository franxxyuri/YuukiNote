package com.yukinoa.domain.usecase

import com.yukinoa.core.Result
import com.yukinoa.domain.model.Note
import com.yukinoa.domain.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.OutputStream

class BackupAndRestoreUseCase(private val noteRepository: NoteRepository) {
    
    // 备份所有笔记
    suspend fun backupNotes(outputStream: OutputStream): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val notesResult = noteRepository.getAllNotes().first()
            val notes = when (notesResult) {
                is Result.Success -> notesResult.data
                is Result.Error -> throw notesResult.exception
                is Result.Loading -> emptyList()
            }
            
            // 简单的文本格式备份
            val backupData = notes.joinToString("\n---\n") { note ->
                """
                Title: ${note.title}
                Content: ${note.content}
                Color: ${note.color}
                IsPinned: ${note.isPinned}
                CreatedAt: ${note.createdAt}
                UpdatedAt: ${note.updatedAt}
                """.trimIndent()
            }
            
            outputStream.use { os ->
                os.write(backupData.toByteArray())
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    
    // 从备份恢复笔记
    suspend fun restoreNotes(inputStream: InputStream): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val backupData = inputStream.use { it.readBytes() }.toString(Charsets.UTF_8)
            val noteEntries = backupData.split("\n---\n")
            
            // 解析并恢复笔记
            noteEntries.forEach { entry ->
                if (entry.isNotBlank()) {
                    val lines = entry.lines()
                    val title = lines.find { it.startsWith("Title: ") }?.substringAfter("Title: ") ?: ""
                    val content = lines.find { it.startsWith("Content: ") }?.substringAfter("Content: ") ?: ""
                    val color = lines.find { it.startsWith("Color: ") }?.substringAfter("Color: ")?.toIntOrNull() ?: 0
                    val isPinned = lines.find { it.startsWith("IsPinned: ") }?.substringAfter("IsPinned: ")?.toBoolean() ?: false
                    
                    val note = Note(
                        title = title,
                        content = content,
                        color = color,
                        isPinned = isPinned
                    )
                    
                    noteRepository.insertNote(note)
                }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}