package com.reishandy.noteapp.ui.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.reishandy.noteapp.R
import com.reishandy.noteapp.ui.theme.NoteAppTheme

@Composable
fun NoteForm(
    @StringRes header: Int,
    titleValue: String,
    titleOnValueChanged: (String) -> Unit,
    subtitleValue: String,
    subtitleOnValueChanged: (String) -> Unit,
    contentValue: String = "",
    contentOnValueChanged: (String) -> Unit = {},
    onCancel: () -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(dimensionResource(R.dimen.large))
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(header),
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.displayMedium,
            textAlign = TextAlign.Center
        )

        OutlinedTextField(
            value = titleValue,
            onValueChange = titleOnValueChanged,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dimensionResource(R.dimen.medium)),
            label = { Text(text = stringResource(R.string.title)) },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            singleLine = true
        )

        OutlinedTextField(
            value = subtitleValue,
            onValueChange = subtitleOnValueChanged,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dimensionResource(R.dimen.medium)),
            label = { Text(text = stringResource(R.string.subtitle)) },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            singleLine = true
        )

        OutlinedTextField(
            value = contentValue,
            onValueChange = contentOnValueChanged,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dimensionResource(R.dimen.medium)),
            label = { Text(text = stringResource(R.string.content)) },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            minLines = 5,
        )

        Row(
            modifier = Modifier
                .padding(top = dimensionResource(R.dimen.large))
                .fillMaxWidth(),
        ) {
            Button(
                onClick = onCancel,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = stringResource(R.string.cancel))
            }

            Spacer(modifier = Modifier.weight(0.1f))

            Button(
                onClick = onSubmit,
                modifier = Modifier.weight(1f),
                enabled = titleValue.isNotEmpty() && subtitleValue.isNotEmpty()
            ) {
                Text(text = stringResource(header))
            }
        }
    }
}

@Preview
@Composable
fun NoteFormPreview() {
    NoteAppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            NoteForm(
                header = R.string.note_add,
                titleValue = "",
                titleOnValueChanged = {},
                subtitleValue = "",
                subtitleOnValueChanged = {},
                contentValue = "",
                contentOnValueChanged = {},
                onCancel = {},
                onSubmit = {}
            )
        }
    }
}