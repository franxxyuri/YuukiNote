package com.yukinoa.domain.usecase

import com.yukinoa.core.Result
import com.yukinoa.domain.model.Note
import com.yukinoa.domain.model.Statistics
import com.yukinoa.domain.repository.NoteRepository
import kotlinx.coroutines.flow.first
import java.time.LocalDateTime

class GetStatisticsUseCase(private val noteRepository: NoteRepository) {
    suspend operator fun invoke(): Statistics {
        val notesResult = noteRepository.getAllNotes().first()
        val notes = when (notesResult) {
            is Result.Success -> notesResult.data
            is Result.Error -> emptyList()
            is Result.Loading -> emptyList()
        }
        
        if (notes.isEmpty()) {
            return Statistics()
        }
        
        val totalNotes = notes.size
        val pinnedNotes = notes.count { it.isPinned }
        
        val totalLength = notes.sumOf { it.content.length + it.title.length }
        val averageNoteLength = if (totalNotes > 0) totalLength / totalNotes else 0
        
        val colorCounts = notes.groupingBy { it.color }.eachCount()
        val mostUsedColor = colorCounts.maxByOrNull { it.value }?.key ?: 0
        
        val oneWeekAgo = LocalDateTime.now().minusWeeks(1)
        val createdLastWeek = notes.count { it.createdAt.isAfter(oneWeekAgo) }
        
        return Statistics(
            totalNotes = totalNotes,
            pinnedNotes = pinnedNotes,
            averageNoteLength = averageNoteLength,
            mostUsedColor = mostUsedColor,
            createdLastWeek = createdLastWeek
        )
    }
}