package com.reishandy.noteapp.ui.model

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reishandy.noteapp.data.NoteAppDatabase
import com.reishandy.noteapp.data.note.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NoteUiState(
    val notes: List<Note> = emptyList(),
    val selectedNote: Note? = null,
    val showDialog: Boolean = false,
)

class NoteViewModel(database: NoteAppDatabase) : ViewModel() {
    private val _uiState = MutableStateFlow(NoteUiState())
    val uiState: StateFlow<NoteUiState> = _uiState.asStateFlow()

    val noteDao = database.noteDao()

    var title by mutableStateOf("")
        private set
    var subtitle by mutableStateOf("")
        private set
    var content by mutableStateOf("")
        private set

    fun updateTitle(newTitle: String) {
        title = newTitle
    }

    fun updateSubtitle(newSubtitle: String) {
        subtitle = newSubtitle
    }

    fun updateContent(newContent: String) {
        content = newContent
    }

    fun updateAll(title: String, subtitle: String, content: String) {
        this.title = title
        this.subtitle = subtitle
        this.content = content
    }

    fun resetInput() {
        title = ""
        subtitle = ""
        content = ""
    }

    fun updateSelectedNote(note: Note) {
        _uiState.update { it.copy(selectedNote = note) }
    }

    fun resetSelectedNote() {
        _uiState.update { it.copy(selectedNote = null) }
    }

    fun showDialog() {
        _uiState.update { it.copy(showDialog = true) }
    }

    fun hideDialog() {
        _uiState.update { it.copy(showDialog = false) }
    }

    // MAIN FUNCTION
    fun initNotes(username: String) {
        viewModelScope.launch {
            noteDao.getAllNotesStream(username).collect { notes ->
                _uiState.update { it.copy(notes = notes) }
            }
        }
    }

    fun addNote(
        username: String,
        onSuccess: () -> Unit,
        context: Context
    ) {
        viewModelScope.launch {
            noteDao.insert(
                Note(
                    title = title,
                    subtitle = subtitle,
                    content = content,
                    userId = username
                )
            )
            showToast(context, "Note added")
            onSuccess()
        }
    }

    fun updateNote(
        onSuccess: () -> Unit,
        context: Context
    ) {
        viewModelScope.launch {
            val selectedNote = _uiState.value.selectedNote ?: return@launch
            noteDao.update(
                id = selectedNote.id,
                title = title,
                subtitle = subtitle,
                content = content,
                timestamp = System.currentTimeMillis()
            )
            showToast(context, "Note $title updated")
            onSuccess()
        }
    }

    fun deleteNote(
        onSuccess: () -> Unit,
        context: Context
    ) {
        viewModelScope.launch {
            val selectedNote = _uiState.value.selectedNote ?: return@launch
            noteDao.delete(selectedNote)
            showToast(context, "Note ${selectedNote.title} deleted")
            onSuccess()
        }
    }

    // AUTH HELPER
    fun changeUsername(oldUsername: String, newUsername: String) {
        viewModelScope.launch {
            noteDao.changeUsername(oldUsername, newUsername)
        }
    }
}