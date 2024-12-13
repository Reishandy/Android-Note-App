package com.reishandy.noteapp.data.user

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Query("UPDATE users SET username = :username, password = :password WHERE username = :oldUsername")
    suspend fun update(oldUsername: String, username: String, password: String)

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT * from users WHERE username = :username")
    suspend fun getUser(username: String): User

    @Query("SELECT EXISTS(SELECT * FROM users WHERE username = :username)")
    suspend fun checkIfUserExists(username: String): Boolean
}