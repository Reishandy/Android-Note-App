package com.reishandy.noteapp.ui.component

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Password
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.reishandy.noteapp.R
import com.reishandy.noteapp.ui.model.AuthFormState
import com.reishandy.noteapp.ui.model.AuthUiState
import com.reishandy.noteapp.ui.theme.NoteAppTheme

@Composable
fun AuthForm(
    uiState: AuthUiState,
    usernameValue: String = "",
    usernameOnValueChanged: (String) -> Unit = {},
    passwordValue: String = "",
    passwordOnValueChanged: (String) -> Unit = {},
    rePasswordValue: String = "",
    rePasswordOnValueChanged: (String) -> Unit = {},
    onSubmit: () -> Unit,
    onCancel: () -> Unit = {},
    onChange: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(dimensionResource(R.dimen.extra_large))
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(uiState.authFormState.title),
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.displayMedium,
            textAlign = TextAlign.Center
        )

        if (uiState.authFormState != AuthFormState.Password) {
            AuthTextField(
                value = usernameValue,
                onValueChange = usernameOnValueChanged,
                label = if (uiState.usernameError.isEmpty())
                    stringResource(R.string.username) else uiState.usernameError,
                isError = !uiState.usernameError.isEmpty(),
                isPassword = false,
                isLast = uiState.authFormState == AuthFormState.Username
            )
        }

        if (uiState.authFormState != AuthFormState.Username) {
            AuthTextField(
                value = passwordValue,
                onValueChange = passwordOnValueChanged,
                label = if (uiState.passwordError.isEmpty())
                    stringResource(R.string.password) else uiState.passwordError,
                isError = !uiState.passwordError.isEmpty(),
                isPassword = true,
                isLast = uiState.authFormState == AuthFormState.Login
            )
        }

        if (uiState.authFormState == AuthFormState.Password ||
            uiState.authFormState == AuthFormState.Register
        ) {
            AuthTextField(
                value = rePasswordValue,
                onValueChange = rePasswordOnValueChanged,
                label = if (uiState.rePasswordError.isEmpty())
                    stringResource(R.string.re_enter_password) else uiState.rePasswordError,
                isError = !uiState.rePasswordError.isEmpty(),
                isPassword = true,
                isLast = true
            )
        }

        Row(
            modifier = Modifier
                .padding(top = dimensionResource(R.dimen.large))
                .fillMaxWidth()
        ) {
            if (uiState.authFormState == AuthFormState.Username ||
                uiState.authFormState == AuthFormState.Password
            ) {
                AuthButtons(
                    label = stringResource(R.string.cancel),
                    onClick = onCancel,
                    modifier = Modifier.weight(1f),
                    enabled = true
                )

                Spacer(modifier = Modifier.weight(0.2f))
            }

            AuthButtons(
                label = stringResource(R.string.submit),
                onClick = onSubmit,
                enabled = when (uiState.authFormState) {
                    AuthFormState.Login -> usernameValue.isNotEmpty() && passwordValue.isNotEmpty()
                    AuthFormState.Register -> usernameValue.isNotEmpty() && passwordValue.isNotEmpty()
                            && rePasswordValue.isNotEmpty()

                    AuthFormState.Username -> usernameValue.isNotEmpty()
                    AuthFormState.Password -> passwordValue.isNotEmpty() && rePasswordValue.isNotEmpty()
                },
                modifier = Modifier.weight(1f)
            )
        }

        if (uiState.authFormState == AuthFormState.Login) {
            AuthTextButtons(
                label = stringResource(R.string.register_instead),
                onClick = onChange
            )
        } else if (uiState.authFormState == AuthFormState.Register) {
            AuthTextButtons(
                label = stringResource(R.string.login_instead),
                onClick = onChange
            )
        }
    }

}

@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean,
    isPassword: Boolean,
    isLast: Boolean,
    modifier: Modifier = Modifier
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    if (!isPassword) passwordVisible = true

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .padding(top = dimensionResource(R.dimen.medium))
            .fillMaxWidth(),
        label = { Text(label) },
        leadingIcon = {
            Icon(
                imageVector = if (isPassword) Icons.Outlined.Password
                else Icons.Outlined.AccountCircle,
                contentDescription = null
            )
        },
        trailingIcon = {
            if (isPassword) {
                IconButton(
                    onClick = { passwordVisible = !passwordVisible }
                ) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Outlined.Visibility
                        else Icons.Outlined.VisibilityOff,
                        contentDescription = null
                    )
                }
            }
        },
        isError = isError,
        visualTransformation = if (passwordVisible) VisualTransformation.None
        else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            imeAction = if (isLast) ImeAction.Done else ImeAction.Next,
            keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Text
        ),
        singleLine = true
    )
}

@Composable
fun AuthButtons(
    label: String,
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled
    ) {
        Text(label)
    }
}

@Composable
fun AuthTextButtons(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(label)
    }
}

@Preview
@Composable
fun AuthFormPreview() {
    NoteAppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            AuthForm(
                uiState = AuthUiState(
                    authFormState = AuthFormState.Register
                ),
                onSubmit = {}
            )
        }
    }
}