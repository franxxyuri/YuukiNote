package com.yukinoa.domain.model

data class Statistics(
    val totalNotes: Int = 0,
    val pinnedNotes: Int = 0,
    val averageNoteLength: Int = 0,
    val mostUsedColor: Int = 0,
    val createdLastWeek: Int = 0
)