package com.yukinoa.domain.usecase

import com.yukinoa.core.Result
import com.yukinoa.domain.model.Note
import com.yukinoa.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchNotesUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    operator fun invoke(query: String): Flow<Result<List<Note>>> {
        // 目前只实现普通文本搜索
        // 后续可以扩展支持标签搜索等功能
        return noteRepository.searchNotes(query)
    }
}