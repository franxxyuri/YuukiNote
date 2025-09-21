package com.yukinoa.domain.usecase

import com.yukinoa.core.Result
import com.yukinoa.domain.model.Note
import com.yukinoa.domain.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.io.OutputStream

class ExportNotesUseCase(private val noteRepository: NoteRepository) {
    suspend operator fun invoke(outputStream: OutputStream): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val notesResult: Result<List<Note>> = noteRepository.getAllNotes().first()
            val notes: List<Note> = when (notesResult) {
                is Result.Success -> notesResult.data
                is Result.Error -> throw notesResult.exception
                is Result.Loading -> emptyList() // 在加载状态下返回空列表
            }
            
            // 这里可以使用JSON或其他格式导出数据
            val jsonString = notes.joinToString(
                separator = ",\n",
                prefix = "[\n",
                postfix = "\n]"
            ) { note: Note ->
                """
                    {
                        "id": ${note.id},
                        "title": "${note.title.replace("\"", "\\\"")}",
                        "content": "${note.content.replace("\"", "\\\"")}",
                        "color": ${note.color},
                        "isPinned": ${note.isPinned},
                        "createdAt": "${note.createdAt}",
                        "updatedAt": "${note.updatedAt}"
                    }
                """.trimIndent()
            }
            
            outputStream.use { os ->
                os.write(jsonString.toByteArray())
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}