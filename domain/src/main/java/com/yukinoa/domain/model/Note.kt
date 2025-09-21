package com.yukinoa.domain.model

import java.time.LocalDateTime

data class Note(
    val id: Long = 0,
    val title: String,
    val content: String,
    val color: Int = 0,
    val isPinned: Boolean = false,
    val tagIds: List<Long> = emptyList(),
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)