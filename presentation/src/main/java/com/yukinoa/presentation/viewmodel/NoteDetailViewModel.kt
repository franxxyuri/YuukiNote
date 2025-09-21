package com.yukinoa.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.yukinoa.core.BaseViewModel
import com.yukinoa.core.Result
import com.yukinoa.domain.model.Note
import com.yukinoa.domain.usecase.GetNoteByIdUseCase
import com.yukinoa.domain.usecase.InsertNoteUseCase
import com.yukinoa.domain.usecase.UpdateNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    private val getNoteByIdUseCase: GetNoteByIdUseCase,
    private val insertNoteUseCase: InsertNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<NoteDetailViewModel.State, NoteDetailViewModel.Event>(State()) {

    data class State(
        val note: Note = Note.EMPTY,
        val isLoading: Boolean = false,
        val error: String? = null,
        val isSaved: Boolean = false
    )

    sealed class Event {
        data object LoadNote : Event()
        data class UpdateTitle(val title: String) : Event()
        data class UpdateContent(val content: String) : Event()
        data class UpdateColor(val color: Int) : Event()
        data class TogglePin(val isPinned: Boolean) : Event()
        data object SaveNote : Event()
    }

    private val noteId: Long? = savedStateHandle.get<String>(NOTE_ID_KEY)?.toLongOrNull()

    init {
        if (noteId != null) {
            handleEvent(Event.LoadNote)
        }
    }

    override fun handleEvent(event: Event) {
        when (event) {
            is Event.LoadNote -> loadNote()
            is Event.UpdateTitle -> updateTitle(event.title)
            is Event.UpdateContent -> updateContent(event.content)
            is Event.UpdateColor -> updateColor(event.color)
            is Event.TogglePin -> togglePin(event.isPinned)
            is Event.SaveNote -> saveNote()
        }
    }

    private fun loadNote() {
        noteId?.let { id ->
            viewModelScope.launch {
                setState { copy(isLoading = true, error = null) }
                getNoteByIdUseCase(id).collectLatest { result ->
                    when (result) {
                        is Result.Success -> setState {
                            copy(
                                note = result.data,
                                isLoading = false,
                                error = null
                            )
                        }
                        is Result.Error -> setState {
                            copy(
                                isLoading = false,
                                error = result.exception.message
                            )
                        }
                        is Result.Loading -> setState { copy(isLoading = true) }
                    }
                }
            }
        }
    }

    private fun updateTitle(title: String) {
        setState { copy(note = note.copy(title = title), isSaved = false) }
    }

    private fun updateContent(content: String) {
        setState { copy(note = note.copy(content = content), isSaved = false) }
    }

    private fun updateColor(color: Int) {
        setState { copy(note = note.copy(color = color), isSaved = false) }
    }

    private fun togglePin(isPinned: Boolean) {
        setState { copy(note = note.copy(isPinned = isPinned), isSaved = false) }
    }

    private fun saveNote() {
        viewModelScope.launch {
            val result = if (noteId == null) {
                insertNoteUseCase(state.value.note)
            } else {
                updateNoteUseCase(state.value.note)
            }

            when (result) {
                is Result.Success -> {
                    setState { copy(isSaved = true, error = null) }
                }
                is Result.Error -> {
                    setState { copy(error = result.exception.message, isSaved = false) }
                }
                else -> {}
            }
        }
    }

    companion object {
        const val NOTE_ID_KEY = "noteId"
    }
}