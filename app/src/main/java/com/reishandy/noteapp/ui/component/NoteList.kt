package com.reishandy.noteapp.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.reishandy.noteapp.R
import com.reishandy.noteapp.data.note.Note
import com.reishandy.noteapp.ui.model.NoteUiState
import com.reishandy.noteapp.ui.model.formatTimestamp
import com.reishandy.noteapp.ui.theme.NoteAppTheme

@Composable
fun NoteList(
    uiState: NoteUiState,
    notes: List<Note> = emptyList(),
    onSelected: (Note) -> Unit = {},
    onAdd: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onDeleteDialog: () -> Unit,
    onDismissDialog: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Row(
                    modifier = Modifier
                        .padding(horizontal = dimensionResource(R.dimen.large))
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.weight(0.1f)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            modifier = Modifier.size(dimensionResource(R.dimen.extra_large))
                        )
                    }

                    Text(
                        text = stringResource(R.string.app_name),
                        modifier = modifier
                            .weight(1f)
                            .padding(vertical = dimensionResource(R.dimen.large)),
                        style = MaterialTheme.typography.displaySmall,
                        textAlign = TextAlign.Center
                    )
                }
            }

            if (notes.isEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.note_empty),
                        modifier = Modifier.padding(top = dimensionResource(R.dimen.extra_large)),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                items(notes) { note ->
                    NoteCard(
                        note = note,
                        onSelected = onSelected,
                        onEdit = onEdit,
                        onDelete = onDelete,
                        isSelected = note == uiState.selectedNote
                    )
                }
            }
        }

        ConfirmDialog(
            showDialog = uiState.showDialog,
            onDeleteDialog = onDeleteDialog,
            onDismissDialog = onDismissDialog
        )

        FloatingActionButton(
            onClick = onAdd,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(dimensionResource(R.dimen.large))
        ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = null)
        }
    }
}

@Composable
fun ConfirmDialog(
    showDialog: Boolean,
    onDeleteDialog: () -> Unit,
    onDismissDialog: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismissDialog,
            title = {
                Text(
                    text = stringResource(R.string.note_delete),
                    style = MaterialTheme.typography.titleMedium
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.note_delete_confirmation),
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteDialog()
                        onDismissDialog()
                    },
                    modifier = Modifier.padding(dimensionResource(R.dimen.small)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(
                        text = stringResource(R.string.note_delete),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            },
            dismissButton = {
                Button(
                    onClick = onDismissDialog,
                    modifier = Modifier.padding(dimensionResource(R.dimen.small))
                ) {
                    Text(
                        text = stringResource(R.string.cancel),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            },
            modifier = modifier
        )
    }
}

@Composable
fun NoteCard(
    note: Note,
    isSelected: Boolean = false,
    onSelected: (Note) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = { onSelected(note) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(R.dimen.large),
                vertical = dimensionResource(R.dimen.small)
            ),
        elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.extra_small)),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.inverseSurface
            else MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = dimensionResource(R.dimen.large),
                vertical = dimensionResource(R.dimen.medium)
            )
        ) {
            Row {
                Text(
                    text = note.title,
                    modifier = Modifier.weight(2f),
                    style = MaterialTheme.typography.headlineSmall,
                )

                Text(
                    text = formatTimestamp(note.timestamp),
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.labelLarge,
                    textAlign = TextAlign.End
                )
            }

            Text(
                text = note.subtitle,
                modifier = Modifier.padding(top = dimensionResource(R.dimen.medium)),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Justify
            )

            AnimatedVisibility(visible = isSelected) {
                Column(
                    modifier.animateContentSize(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioNoBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    ),
                ) {
                    HorizontalDivider(
                        modifier = Modifier
                            .padding(vertical = dimensionResource(R.dimen.medium))
                            .fillMaxWidth(),
                        thickness = dimensionResource(R.dimen.extra_small)
                    )

                    Text(
                        text = note.content,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Justify
                    )

                    CardButton(
                        onEdit = onEdit,
                        onDelete = onDelete
                    )
                }
            }
        }
    }
}

@Composable
fun CardButton(
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .padding(top = dimensionResource(R.dimen.large))
            .fillMaxWidth(),
    ) {
        Button(
            onClick = onEdit,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Text(text = stringResource(R.string.note_edit))
        }

        Spacer(modifier = Modifier.weight(0.1f))

        Button(
            onClick = onDelete,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text(text = stringResource(R.string.note_delete))
        }
    }
}

@Preview
@Composable
fun PreviewNoteList() {
    NoteAppTheme(darkTheme = false) {
        Surface(modifier = Modifier.fillMaxSize()) {
            NoteList(
                uiState = NoteUiState(
                    selectedNote = Note(
                        title = "Title 1",
                        subtitle = "Subtitle 1",
                        content = "Content 1",
                        timestamp = System.currentTimeMillis(),
                        userId = "username"
                    )
                ),
                notes = listOf(
                    Note(
                        title = "Title 1",
                        subtitle = "Subtitle 1",
                        content = "Content 1",
                        timestamp = System.currentTimeMillis(),
                        userId = "username"
                    ),
                    Note(
                        title = "Title 2",
                        subtitle = "Subtitle 2",
                        content = "Content 2",
                        timestamp = System.currentTimeMillis(),
                        userId = "username"
                    ),
                    Note(
                        title = "Title 3",
                        subtitle = "Subtitle 3",
                        content = "Content 3",
                        timestamp = System.currentTimeMillis(),
                        userId = "username"
                    ),
                ),
                onAdd = { },
                onEdit = { },
                onDelete = { },
                onDeleteDialog = { },
                onDismissDialog = { },
                onBack = { }
            )
        }
    }
}