package com.yukinoa.domain.usecase

import com.yukinoa.core.Result
import com.yukinoa.domain.model.Note
import com.yukinoa.domain.repository.NoteRepository
import javax.inject.Inject

class InsertNoteUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    suspend operator fun invoke(note: Note): Result<Long> {
        return noteRepository.insertNote(note)
    }
}