package com.reishandy.noteapp.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.reishandy.noteapp.R
import com.reishandy.noteapp.ui.component.AuthForm
import com.reishandy.noteapp.ui.component.MainMenu
import com.reishandy.noteapp.ui.model.AuthFormState
import com.reishandy.noteapp.ui.model.AuthViewModel

enum class NoteAppNav() {
    Login,
    Register,
    Username,
    Password,
    MainMenu
}

@Composable
fun NoteApp() {
    val context = LocalContext.current
    val authViewModel: AuthViewModel = viewModel()
    val navController: NavHostController = rememberNavController()

    val uiState by authViewModel.uiState.collectAsState()

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        NavHost(
            navController = navController,
            startDestination = NoteAppNav.Login.name
        ) {
            composable(route = NoteAppNav.Login.name) {
                AuthForm(
                    uiState = uiState,
                    usernameValue = authViewModel.username,
                    usernameOnValueChanged = { authViewModel.updateUsername(it) },
                    passwordValue = authViewModel.password,
                    passwordOnValueChanged = { authViewModel.updatePassword(it) },
                    onSubmit = {
                        val success = authViewModel.login(context)
                        if (success) {
                            navController.navigate(NoteAppNav.MainMenu.name)
                        }
                    },
                    onChange = {
                        authViewModel.changeAuthFormState(AuthFormState.Register)
                        navController.navigate(NoteAppNav.Register.name)
                    }
                )
            }

            composable(route = NoteAppNav.Register.name) {
                AuthForm(
                    uiState = uiState,
                    usernameValue = authViewModel.username,
                    usernameOnValueChanged = { authViewModel.updateUsername(it) },
                    passwordValue = authViewModel.password,
                    passwordOnValueChanged = { authViewModel.updatePassword(it) },
                    rePasswordValue = authViewModel.rePassword,
                    rePasswordOnValueChanged = { authViewModel.updateRePassword(it) },
                    onSubmit = {
                        val success = authViewModel.register(context)
                        if (success) {
                            navController.navigate(NoteAppNav.Login.name)
                        }
                    },
                    onChange = {
                        authViewModel.changeAuthFormState(AuthFormState.Login)
                        navController.navigate(NoteAppNav.Login.name)
                    }
                )
            }

            composable(route = NoteAppNav.Username.name) {
                AuthForm(
                    uiState = uiState,
                    usernameValue = authViewModel.username,
                    usernameOnValueChanged = { authViewModel.updateUsername(it) },
                    onSubmit = {
                        val success = authViewModel.changeUsername(context)
                        if (success) {
                            navController.navigate(NoteAppNav.MainMenu.name)
                        }
                    },
                    onCancel = {
                        navController.navigate(NoteAppNav.MainMenu.name)
                    }
                )
            }

            composable(route = NoteAppNav.Password.name) {
                AuthForm(
                    uiState = uiState,
                    passwordValue = authViewModel.password,
                    passwordOnValueChanged = { authViewModel.updatePassword(it) },
                    rePasswordValue = authViewModel.rePassword,
                    rePasswordOnValueChanged = { authViewModel.updateRePassword(it) },
                    onSubmit = {
                        val success = authViewModel.changePassword(context)
                        if (success) {
                            navController.navigate(NoteAppNav.MainMenu.name)
                        }
                    },
                    onCancel = {
                        navController.navigate(NoteAppNav.MainMenu.name)
                    }
                )
            }

            composable(route = NoteAppNav.MainMenu.name) {
                val context = LocalContext.current

                MainMenu(
                    uiState = uiState,
                    onClick = {
                        // TODO
                    },
                    changeUsernameOnClick = {
                        authViewModel.changeAuthFormState(AuthFormState.Username)
                        navController.navigate(NoteAppNav.Username.name)
                    },
                    changePasswordOnClick = {
                        authViewModel.changeAuthFormState(AuthFormState.Password)
                        navController.navigate(NoteAppNav.Password.name)
                    },
                    deleteAccountOnClick = {
                        authViewModel.showDialog(
                            content = context.getString(R.string.delete_confitmation),
                            onConfirm = {
                                val success = authViewModel.deleteAccount(context)
                                if (success) {
                                    authViewModel.changeAuthFormState(AuthFormState.Login)
                                    navController.navigate(NoteAppNav.Login.name)
                                } else {
                                    authViewModel.hideDialog()
                                }
                            }
                        )
                    },
                    logoutOnClick = {
                        authViewModel.showDialog(
                            content = context.getString(R.string.logout_confirmation),
                            onConfirm = {
                                authViewModel.logout(context)
                                navController.navigate(NoteAppNav.Login.name)
                            }
                        )
                    }
                )
            }
        }
    }
}