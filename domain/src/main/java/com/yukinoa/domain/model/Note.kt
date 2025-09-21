package com.yukinoa.domain.model

import java.time.LocalDateTime

data class Note(
    val id: Long = 0,
    val title: String = "",
    val content: String = "",
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val isPinned: Boolean = false,
    val color: Int = 0
) {
    companion object {
        val EMPTY = Note()
    }
}