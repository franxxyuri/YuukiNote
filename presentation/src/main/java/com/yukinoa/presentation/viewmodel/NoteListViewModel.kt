package com.yukinoa.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.yukinoa.core.BaseViewModel
import com.yukinoa.core.Result
import com.yukinoa.domain.model.Note
import com.yukinoa.domain.usecase.DeleteNoteUseCase
import com.yukinoa.domain.usecase.GetAllNotesUseCase
import com.yukinoa.domain.usecase.SearchNotesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val getAllNotesUseCase: GetAllNotesUseCase,
    private val searchNotesUseCase: SearchNotesUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase
) : BaseViewModel<NoteListViewModel.State, NoteListViewModel.Event>(State()) {

    data class State(
        val notes: List<Note> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null,
        val searchQuery: String = "",
        val isSearchActive: Boolean = false
    )

    sealed class Event {
        data object LoadNotes : Event()
        data class SearchNotes(val query: String) : Event()
        data class DeleteNote(val note: Note) : Event()
        data object ClearSearch : Event()
        data object ActivateSearch : Event() // 添加激活搜索事件
    }

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        handleEvent(Event.LoadNotes)
    }

    override fun handleEvent(event: Event) {
        when (event) {
            is Event.LoadNotes -> loadNotes()
            is Event.SearchNotes -> searchNotes(event.query)
            is Event.DeleteNote -> deleteNote(event.note)
            is Event.ClearSearch -> clearSearch()
            is Event.ActivateSearch -> activateSearch() // 处理激活搜索事件
        }
    }

    private fun loadNotes() {
        viewModelScope.launch {
            setState { copy(isLoading = true, error = null) }
            getAllNotesUseCase().collectLatest { result ->
                when (result) {
                    is Result.Success -> setState {
                        copy(
                            notes = result.data,
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

    private fun searchNotes(query: String) {
        _searchQuery.value = query
        if (query.isBlank()) {
            loadNotes()
            return
        }

        viewModelScope.launch {
            setState { copy(isSearchActive = true, searchQuery = query) }
            searchNotesUseCase(query).collectLatest { result ->
                when (result) {
                    is Result.Success -> setState {
                        copy(
                            notes = result.data,
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

    // 添加激活搜索功能
    private fun activateSearch() {
        setState { copy(isSearchActive = true) }
    }

    private fun deleteNote(note: Note) {
        viewModelScope.launch {
            when (val result = deleteNoteUseCase(note)) {
                is Result.Success -> {
                    // Notes will be reloaded automatically via flow
                }
                is Result.Error -> {
                    setState { copy(error = result.exception.message) }
                }
                else -> {}
            }
        }
    }

    private fun clearSearch() {
        _searchQuery.value = ""
        setState { copy(isSearchActive = false, searchQuery = "") }
        loadNotes()
    }
}