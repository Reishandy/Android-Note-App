package com.reishandy.noteapp.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
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
import com.reishandy.noteapp.ui.component.NoteForm
import com.reishandy.noteapp.ui.component.NoteList
import com.reishandy.noteapp.ui.model.AuthFormState
import com.reishandy.noteapp.ui.model.AuthViewModel
import com.reishandy.noteapp.ui.model.AuthViewModelFactory
import com.reishandy.noteapp.ui.model.NoteViewModel
import com.reishandy.noteapp.ui.model.NoteViewModelFactory

enum class NoteAppNav() {
    Login,
    Register,
    Username,
    Password,
    MainMenu,
    Note,
    AddNote,
    EditNote
}

@Composable
fun NoteApp() {
    val context = LocalContext.current
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(context))
    val noteViewModel: NoteViewModel = viewModel(factory = NoteViewModelFactory(context))
    val navController: NavHostController = rememberNavController()

    val authUiState by authViewModel.uiState.collectAsState()
    val noteUiState by noteViewModel.uiState.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        NavHost(
            navController = navController,
            startDestination = NoteAppNav.Login.name,
            modifier = Modifier.statusBarsPadding()
        ) {
            composable(route = NoteAppNav.Login.name) {
                AuthForm(
                    uiState = authUiState,
                    usernameValue = authViewModel.username,
                    usernameOnValueChanged = { authViewModel.updateUsername(it) },
                    passwordValue = authViewModel.password,
                    passwordOnValueChanged = { authViewModel.updatePassword(it) },
                    onSubmit = {
                        authViewModel.login(
                            context = context,
                            onSuccess = {
                                navController.navigate(NoteAppNav.MainMenu.name)
                            }
                        )
                    },
                    onChange = {
                        authViewModel.changeAuthFormState(AuthFormState.Register)
                        navController.navigate(NoteAppNav.Register.name)
                    }
                )
            }

            composable(route = NoteAppNav.Register.name) {
                AuthForm(
                    uiState = authUiState,
                    usernameValue = authViewModel.username,
                    usernameOnValueChanged = { authViewModel.updateUsername(it) },
                    passwordValue = authViewModel.password,
                    passwordOnValueChanged = { authViewModel.updatePassword(it) },
                    rePasswordValue = authViewModel.rePassword,
                    rePasswordOnValueChanged = { authViewModel.updateRePassword(it) },
                    onSubmit = {
                        authViewModel.register(
                            context = context,
                            onSuccess = {
                                navController.navigate(NoteAppNav.Login.name)
                            }
                        )
                    },
                    onChange = {
                        authViewModel.changeAuthFormState(AuthFormState.Login)
                        navController.navigate(NoteAppNav.Login.name)
                    }
                )
            }

            composable(route = NoteAppNav.Username.name) {
                AuthForm(
                    uiState = authUiState,
                    usernameValue = authViewModel.username,
                    usernameOnValueChanged = { authViewModel.updateUsername(it) },
                    onSubmit = {
                        authViewModel.changeUsername(
                            currentUsername = authUiState.user,
                            context = context,
                            onSuccess = {
                                noteViewModel.changeUsername(
                                    oldUsername = authUiState.user,
                                    newUsername = authViewModel.username
                                )
                                navController.navigate(NoteAppNav.MainMenu.name)
                            }
                        )
                    },
                    onCancel = {
                        navController.navigate(NoteAppNav.MainMenu.name)
                    }
                )
            }

            composable(route = NoteAppNav.Password.name) {
                AuthForm(
                    uiState = authUiState,
                    passwordValue = authViewModel.password,
                    passwordOnValueChanged = { authViewModel.updatePassword(it) },
                    rePasswordValue = authViewModel.rePassword,
                    rePasswordOnValueChanged = { authViewModel.updateRePassword(it) },
                    onSubmit = {
                        authViewModel.changePassword(
                            currentUsername = authUiState.user,
                            context = context,
                            onSuccess = {
                                navController.navigate(NoteAppNav.MainMenu.name)
                            }
                        )
                    },
                    onCancel = {
                        navController.navigate(NoteAppNav.MainMenu.name)
                    }
                )
            }

            composable(route = NoteAppNav.MainMenu.name) {
                val context = LocalContext.current

                MainMenu(
                    uiState = authUiState,
                    onClick = {
                        noteViewModel.initNotes(authUiState.user)
                        navController.navigate(NoteAppNav.Note.name)
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
                            content = context.getString(R.string.delete_confirmation),
                            onConfirm = {
                                authViewModel.deleteAccount(
                                    currentUsername = authUiState.user,
                                    context = context,
                                    onSuccess = {
                                        authViewModel.changeAuthFormState(AuthFormState.Login)
                                        navController.navigate(NoteAppNav.Login.name)
                                    },
                                    onFailed = {
                                        authViewModel.hideDialog()
                                    }
                                )
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

            composable(route = NoteAppNav.Note.name) {
                NoteList(
                    uiState = noteUiState,
                    notes = noteUiState.notes,
                    onSelected = { selectedNote ->
                        if (noteUiState.selectedNote == selectedNote) {
                            noteViewModel.resetSelectedNote()
                        } else {
                            noteViewModel.updateSelectedNote(selectedNote)
                        }
                    },
                    onAdd = {
                        navController.navigate(NoteAppNav.AddNote.name)
                    },
                    onEdit = {
                        noteViewModel.updateAll(
                            title = noteUiState.selectedNote?.title ?: "",
                            subtitle = noteUiState.selectedNote?.subtitle ?: "",
                            content = noteUiState.selectedNote?.content ?: ""
                        )
                        navController.navigate(NoteAppNav.EditNote.name)
                    },
                    onDelete = {
                        noteViewModel.showDialog()
                    },
                    onDeleteDialog = {
                        noteViewModel.deleteNote(
                            onSuccess = {
                                noteViewModel.hideDialog()
                            },
                            context = context
                        )
                    },
                    onDismissDialog = {
                        noteViewModel.hideDialog()
                    },
                    onBack = {
                        navController.navigate(NoteAppNav.MainMenu.name)
                    }
                )
            }

            composable(route = NoteAppNav.AddNote.name) {
                NoteForm(
                    header = R.string.note_add,
                    titleValue = noteViewModel.title,
                    titleOnValueChanged = { noteViewModel.updateTitle(it) },
                    subtitleValue = noteViewModel.subtitle,
                    subtitleOnValueChanged = { noteViewModel.updateSubtitle(it) },
                    contentValue = noteViewModel.content,
                    contentOnValueChanged = { noteViewModel.updateContent(it) },
                    onSubmit = {
                        noteViewModel.addNote(
                            username = authUiState.user,
                            onSuccess = {
                                noteViewModel.resetInput()
                                navController.navigate(NoteAppNav.Note.name)
                            },
                            context = context
                        )
                    },
                    onCancel = {
                        noteViewModel.resetInput()
                        navController.navigate(NoteAppNav.Note.name)
                    }
                )
            }

            composable(route = NoteAppNav.EditNote.name) {
                NoteForm(
                    header = R.string.note_edit,
                    titleValue = noteViewModel.title,
                    titleOnValueChanged = { noteViewModel.updateTitle(it) },
                    subtitleValue = noteViewModel.subtitle,
                    subtitleOnValueChanged = { noteViewModel.updateSubtitle(it) },
                    contentValue = noteViewModel.content,
                    contentOnValueChanged = { noteViewModel.updateContent(it) },
                    onSubmit = {
                        noteViewModel.updateNote(
                            onSuccess = {
                                noteViewModel.resetInput()
                                navController.navigate(NoteAppNav.Note.name)
                            },
                            context = context
                        )
                    },
                    onCancel = {
                        noteViewModel.resetInput()
                        navController.navigate(NoteAppNav.Note.name)
                    }
                )
            }
        }
    }
}