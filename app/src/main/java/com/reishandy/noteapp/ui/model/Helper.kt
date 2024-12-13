package com.reishandy.noteapp.ui.model

import android.content.Context
import android.widget.Toast
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun showToast(context: Context, message: String) {
    Toast.makeText(
        context,
        message,
        Toast.LENGTH_SHORT
    ).show()
}

fun hashPassword(password: String): String {
    val bytes = password.toByteArray()
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(bytes)
    return digest.fold("") { str, it -> str + "%02x".format(it) }
}

fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("HH:mm \ndd/MM/yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}