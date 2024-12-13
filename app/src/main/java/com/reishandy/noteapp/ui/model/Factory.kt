package com.reishandy.noteapp.ui.model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.reishandy.noteapp.data.NoteAppDatabase

class AuthViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val database = NoteAppDatabase.getDatabase(context)
        @Suppress("UNCHECKED_CAST")
        return AuthViewModel(database) as T
    }
}

class NoteViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val database = NoteAppDatabase.getDatabase(context)
        @Suppress("UNCHECKED_CAST")
        return NoteViewModel(database) as T
    }
}