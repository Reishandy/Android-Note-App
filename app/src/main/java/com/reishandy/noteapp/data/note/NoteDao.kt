package com.reishandy.noteapp.data.note

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert
    suspend fun insert(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("UPDATE notes SET title = :title, subtitle = :subtitle, content = :content, timestamp = :timestamp WHERE id = :id")
    suspend fun update(id: Long, title: String, subtitle: String, content: String, timestamp: Long)

    @Query("SELECT * from notes WHERE user_id = :username ORDER BY timestamp DESC")
    fun getAllNotesStream(username: String): Flow<List<Note>>

    @Query("UPDATE notes SET user_id = :newUsername WHERE user_id = :oldUsername")
    suspend fun changeUsername(oldUsername: String, newUsername: String)
}