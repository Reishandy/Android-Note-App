package com.reishandy.noteapp.ui.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.reishandy.noteapp.ui.theme.NoteAppTheme

@Composable
fun NoteList(
    modifier: Modifier = Modifier,
) {
    // TODO: Implement NoteList composable
}

@Preview
@Composable
fun PreviewNoteList() {
    NoteAppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            NoteList()
        }
    }
}