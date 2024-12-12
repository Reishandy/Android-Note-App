package com.reishandy.noteapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.reishandy.noteapp.data.user.User
import com.reishandy.noteapp.data.user.UserDao

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class NoteAppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var Instance: NoteAppDatabase? = null

        fun getDatabase(context: Context): NoteAppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, NoteAppDatabase::class.java, "note_app_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}