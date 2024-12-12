package com.reishandy.noteapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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
    val viewModel: AuthViewModel = viewModel()
    val navController: NavHostController = rememberNavController()

    val uiState by viewModel.uiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = NoteAppNav.Login.name
    ) {
        composable(route = NoteAppNav.Login.name) {
            AuthForm(
                uiState = uiState,
                usernameValue = viewModel.username,
                usernameOnValueChanged = { viewModel.updateUsername(it) },
                passwordValue = viewModel.password,
                passwordOnValueChanged = { viewModel.updatePassword(it) },
                onSubmit = {
                    val success = viewModel.login(context)
                    if (success) {
                        navController.navigate(NoteAppNav.MainMenu.name)
                    }
                },
                onChange = {
                    viewModel.changeAuthFormState(AuthFormState.Register)
                    navController.navigate(NoteAppNav.Register.name)
                }
            )
        }

        composable(route = NoteAppNav.Register.name) {
            AuthForm(
                uiState = uiState,
                usernameValue = viewModel.username,
                usernameOnValueChanged = { viewModel.updateUsername(it) },
                passwordValue = viewModel.password,
                passwordOnValueChanged = { viewModel.updatePassword(it) },
                rePasswordValue = viewModel.rePassword,
                rePasswordOnValueChanged = { viewModel.updateRePassword(it) },
                onSubmit = {
                    val success = viewModel.register(context)
                    if (success) {
                        navController.navigate(NoteAppNav.Login.name)
                    }
                },
                onChange = {
                    viewModel.changeAuthFormState(AuthFormState.Login)
                    navController.navigate(NoteAppNav.Login.name)
                }
            )
        }

        composable(route = NoteAppNav.Username.name) {
            AuthForm(
                uiState = uiState,
                usernameValue = viewModel.username,
                usernameOnValueChanged = { viewModel.updateUsername(it) },
                onSubmit = {
                    val success = viewModel.changeUsername(context)
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
                passwordValue = viewModel.password,
                passwordOnValueChanged = { viewModel.updatePassword(it) },
                rePasswordValue = viewModel.rePassword,
                rePasswordOnValueChanged = { viewModel.updateRePassword(it) },
                onSubmit = {
                    val success = viewModel.changePassword(context)
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
                    viewModel.changeAuthFormState(AuthFormState.Username)
                    navController.navigate(NoteAppNav.Username.name)
                },
                changePasswordOnClick = {
                    viewModel.changeAuthFormState(AuthFormState.Password)
                    navController.navigate(NoteAppNav.Password.name)
                },
                deleteAccountOnClick = {
                    viewModel.showDialog(
                        content = context.getString(R.string.delete_confitmation),
                        onConfirm = {
                            val success = viewModel.deleteAccount(context)
                            if (success) {
                                viewModel.changeAuthFormState(AuthFormState.Login)
                                navController.navigate(NoteAppNav.Login.name)
                            } else {
                                viewModel.hideDialog()
                            }
                        }
                    )
                },
                logoutOnClick = {
                    viewModel.showDialog(
                        content = context.getString(R.string.logout_confirmation),
                        onConfirm = {
                            viewModel.logout(context)
                            navController.navigate(NoteAppNav.Login.name)
                        }
                    )
                }
            )
        }
    }
}