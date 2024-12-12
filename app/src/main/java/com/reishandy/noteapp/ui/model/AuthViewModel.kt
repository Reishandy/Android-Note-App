package com.reishandy.noteapp.ui.model

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reishandy.noteapp.R
import com.reishandy.noteapp.data.NoteAppDatabase
import com.reishandy.noteapp.data.user.User
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.compose
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.security.MessageDigest

enum class AuthFormState(@StringRes val title: Int) {
    Login(title = R.string.LOGIN),
    Register(title = R.string.REGISTER),
    Username(title = R.string.NEW_USERNAME),
    Password(title = R.string.NEW_PASSWORD)
}

data class AuthUiState(
    val authFormState: AuthFormState = AuthFormState.Login,
    val usernameError: String = "",
    val passwordError: String = "",
    val rePasswordError: String = "",
    val user: String = "",
    val showDialog: Boolean = false,
    val dialogContent: String = "",
    val dialogOnConfirm: () -> Unit = {},
    val dialogOnDismiss: () -> Unit = {}
)

class AuthViewModel(database: NoteAppDatabase) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val userDao = database.userDao()

    var username by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var rePassword by mutableStateOf("")
        private set

    // UPDATE
    fun updateUsername(newUsername: String) {
        username = newUsername
    }

    fun updatePassword(newPassword: String) {
        password = newPassword
    }

    fun updateRePassword(newRePassword: String) {
        rePassword = newRePassword
    }

    private fun resetInput() {
        username = ""
        password = ""
        rePassword = ""
    }

    fun changeAuthFormState(authFormState: AuthFormState) {
        resetInput()
        _uiState.update { state ->
            state.copy(
                authFormState = authFormState,
                usernameError = "",
                passwordError = "",
                rePasswordError = ""
            )
        }
    }

    fun showDialog(
        content: String,
        onConfirm: () -> Unit = {},
    ) {
        _uiState.update { state ->
            state.copy(
                showDialog = true,
                dialogContent = content,
                dialogOnConfirm = onConfirm,
                dialogOnDismiss = { hideDialog() }
            )
        }
    }

    fun hideDialog() {
        _uiState.update { state ->
            state.copy(
                showDialog = false,
                dialogContent = "",
                dialogOnConfirm = {},
                dialogOnDismiss = {}
            )
        }
    }

    // MAIN LOGIC
    fun login(
        context: Context,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val user = userDao.getUser(username)

            if (user == null) {
                _uiState.update { state ->
                    state.copy(
                        usernameError = "Username not found",
                        passwordError = ""
                    )
                }
                return@launch
            }

            if (user.password != hashPassword(password)) {
                _uiState.update { state ->
                    state.copy(
                        usernameError = "",
                        passwordError = "Incorrect password"
                    )
                }
                return@launch
            }

            _uiState.value = AuthUiState(user = username)
            showToast(context, "Welcome back $username")
            onSuccess()
        }
    }

    fun register(
        context: Context,
        onSuccess: () -> Unit
    ) {
        if (!validatePassword()) {
            return
        }

        viewModelScope.launch {
            try {
                userDao.insert(
                    User(
                        username = username,
                        password = hashPassword(password)
                    )
                )

                changeAuthFormState(AuthFormState.Login)
                showToast(context, "Account created successfully")
                onSuccess()
            } catch (e: SQLiteConstraintException) {
                _uiState.update { state ->
                    state.copy(
                        usernameError = "Username already exists",
                        passwordError = "",
                        rePasswordError = ""
                    )
                }
            }
        }

    }

    fun changeUsername(
        currentUsername: String,
        context: Context,
        onSuccess: () -> Unit
    ) {
        // TODO: Implement change username logic database
        viewModelScope.launch {
            val user = userDao.getUser(currentUsername)

            if (user == null) {
                showToast(context, "Something went wrong")
                onSuccess()
                return@launch
            }

            userDao.update(
                oldUsername = user.username,
                username = username,
                password = user.password
            )

            _uiState.value = AuthUiState(user = username)
            showToast(context, "Username changed successfully to $username")
            onSuccess()
        }
    }

    fun changePassword(
        currentUsername: String,
        context: Context,
        onSuccess: () -> Unit
    ) {
        if (!validatePassword()) {
            return
        }

        // TODO: Implement change password logic database
        viewModelScope.launch {
            val user = userDao.getUser(currentUsername)

            if (user == null) {
                showToast(context, "Something went wrong")
                onSuccess()
                return@launch
            }

            userDao.update(
                oldUsername = user.username,
                username = user.username,
                password = hashPassword(password)
            )

            _uiState.value = AuthUiState(user = currentUsername)
            showToast(context, "Password changed successfully")
            onSuccess()
        }
    }

    fun deleteAccount(
        currentUsername: String,
        context: Context,
        onSuccess: () -> Unit,
        onFailed: () -> Unit
    ) {
        viewModelScope.launch {
            val user = userDao.getUser(currentUsername)

            if (user == null) {
                showToast(context, "Something went wrong")
                onFailed()
                return@launch
            }

            userDao.delete(user)
            _uiState.value = AuthUiState()
            showToast(context, "Account deleted successfully")
            onSuccess()
        }
    }

    fun logout(context: Context) {
        _uiState.value = AuthUiState()
        showToast(context, "Logged out successfully")
    }

    // HELPER
    private fun showToast(context: Context, message: String) {
        Toast.makeText(
            context,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun validatePassword(): Boolean {
        if (password.length < 8) {
            _uiState.update { state ->
                state.copy(
                    passwordError = "Password must be at least 8 characters",
                    rePasswordError = ""
                )
            }
            return false
        }

        if (password != rePassword) {
            _uiState.update { state ->
                state.copy(
                    passwordError = "",
                    rePasswordError = "Password does not match"
                )
            }
            return false
        }

        return true
    }

    fun hashPassword(password: String): String {
        val bytes = password.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }
}