package com.reishandy.noteapp.ui.model

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import com.reishandy.noteapp.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.compose
import kotlinx.coroutines.flow.update

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

class AuthViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

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
    fun login(context: Context): Boolean {
        if (username.isEmpty() || password.isEmpty()) {
            _uiState.update { state ->
                state.copy(
                    usernameError = if (username.isEmpty()) "Username is empty" else "",
                    passwordError = if (password.isEmpty()) "Password is empty" else ""
                )
            }
            return false
        }

        // TODO: Implement login logic database

        _uiState.value = AuthUiState(user = username)
        showToast(context, "Welcome back $username")
        return true
    }

    fun register(context: Context): Boolean {
        if (!validateUsername()) {
            return false
        }

        if (!validatePassword()) {
            return false
        }

        // TODO: Implement register logic database

        changeAuthFormState(AuthFormState.Login)
        showToast(context, "Account created successfully")
        return true
    }

    fun changeUsername(context: Context): Boolean {
        if (!validateUsername()) {
            return false
        }

        // TODO: Implement change username logic database

        _uiState.value = AuthUiState(user = username)
        showToast(context, "Username changed successfully to $username")
        return true
    }

    fun changePassword(context: Context): Boolean {
        if (!validatePassword()) {
            return false
        }

        // TODO: Implement change password logic database

        _uiState.update { state ->
            state.copy(
                passwordError = "",
                rePasswordError = ""
            )
        }
        showToast(context, "Password changed successfully")
        return true
    }

    fun deleteAccount(context: Context): Boolean {
        // TODO: Implement delete account logic database

        if (false) { // if error TODO
            showToast(context, "Failed to delete account")
            return false
        }

        showToast(context, "Account deleted successfully")
        return true
    }

    fun logout(context: Context) {
        changeAuthFormState(AuthFormState.Login)
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

    private fun validateUsername(): Boolean {
        if (username.isEmpty()) {
            _uiState.update { state ->
                state.copy(usernameError = "Username is empty")
            }
            return false
        }

        return true
    }

    private fun validatePassword(): Boolean {
        if (password.isEmpty() || rePassword.isEmpty()) {
            _uiState.update { state ->
                state.copy(
                    usernameError = "",
                    passwordError = if (password.isEmpty()) "Password is empty" else "",
                    rePasswordError = if (rePassword.isEmpty()) "Re-enter password is empty" else ""
                )
            }
            return false
        }

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
}