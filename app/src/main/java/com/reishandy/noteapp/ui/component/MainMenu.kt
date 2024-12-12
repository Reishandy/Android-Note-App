package com.reishandy.noteapp.ui.component

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Web
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.reishandy.noteapp.R
import com.reishandy.noteapp.ui.model.AuthUiState
import com.reishandy.noteapp.ui.theme.NoteAppTheme

@Composable
fun MainMenu(
    uiState: AuthUiState,
    onClick: () -> Unit,
    changeUsernameOnClick: () -> Unit,
    changePasswordOnClick: () -> Unit,
    deleteAccountOnClick: () -> Unit,
    logoutOnClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_large)),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.displaySmall
            )

            MainContent(
                username = uiState.user,
                onClick = onClick,
                changeUsernameOnClick = changeUsernameOnClick,
                changePasswordOnClick = changePasswordOnClick,
                deleteAccountOnClick = deleteAccountOnClick,
                logoutOnClick = logoutOnClick,
            )

            Text(
                text = stringResource(R.string.author),
                style = MaterialTheme.typography.bodyMedium
            )

            ConfirmDialog(
                showDialog = uiState.showDialog,
                content = uiState.dialogContent,
                onConfirm = uiState.dialogOnConfirm,
                onDismiss = uiState.dialogOnDismiss
            )
        }
    }
}

@Composable
fun ConfirmDialog(
    showDialog: Boolean,
    content: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = stringResource(R.string.confirm),
                    style = MaterialTheme.typography.titleMedium
                )
            },
            text = {
                Text(
                    text = content,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onConfirm()
                        onDismiss()
                    },
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(
                        text = stringResource(R.string.confirm),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            },
            dismissButton = {
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
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
fun MainContent(
    username: String,
    onClick: () -> Unit,
    changeUsernameOnClick: () -> Unit,
    changePasswordOnClick: () -> Unit,
    deleteAccountOnClick: () -> Unit,
    logoutOnClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        WelcomeCard(
            username = username
        )

        Button(
            onClick = onClick,
            modifier = Modifier
                .padding(vertical = dimensionResource(R.dimen.padding_extra))
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = stringResource(R.string.note_button),
                modifier = Modifier.padding(vertical = dimensionResource(R.dimen.padding_medium)),
                style = MaterialTheme.typography.titleLarge
            )
        }

        IconGrid(
            changeUsernameOnClick = changeUsernameOnClick,
            changePasswordOnClick = changePasswordOnClick,
            deleteAccountOnClick = deleteAccountOnClick,
            logoutOnClick = logoutOnClick
        )
    }
}

@Composable
fun WelcomeCard(
    username: String,

    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.padding_small))
    ) {
        Column(
            modifier = modifier
                .padding(dimensionResource(R.dimen.padding_large))
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.welcome),
                style = MaterialTheme.typography.displayMedium
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))

            Text(
                text = username,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
fun IconGrid(
    changeUsernameOnClick: () -> Unit,
    changePasswordOnClick: () -> Unit,
    deleteAccountOnClick: () -> Unit,
    logoutOnClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconElement(
                onClick = changeUsernameOnClick,
                icon = Icons.Filled.AccountCircle,
                label = stringResource(R.string.change_username),
            )
            IconElement(
                onClick = changePasswordOnClick,
                icon = Icons.Filled.Lock,
                label = stringResource(R.string.change_password),
            )
            IconElement(
                onClick = deleteAccountOnClick,
                icon = Icons.Filled.Delete,
                label = stringResource(R.string.delete_account),
            )
            IconElement(
                onClick = logoutOnClick,
                icon = Icons.AutoMirrored.Filled.ExitToApp,
                label = stringResource(R.string.logout),
            )
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconElement(
                icon = Icons.Filled.Web,
                label = stringResource(R.string.my_website),
                url = "https://reishandy.my.id"
            )
            IconElement(
                icon = Icons.Outlined.Info,
                label = stringResource(R.string.my_github),
                url = "https://github.com/Reishandy"
            )
            IconElement(
                icon = Icons.Outlined.Info,
                label = stringResource(R.string.my_linkedin),
                url = "https://www.linkedin.com/in/reishandy/"
            )
            IconElement(
                icon = Icons.Outlined.Info,
                label = stringResource(R.string.my_instagram),
                url = "https://www.instagram.com/ruxury_nyaa/"
            )
        }
    }
}

@Composable
fun IconElement(
    onClick: () -> Unit = {},
    icon: ImageVector,
    label: String,
    url: String? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .padding(dimensionResource(R.dimen.padding_small))
            .clickable {
                url?.let {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                    context.startActivity(intent)
                } ?: onClick()
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(dimensionResource(R.dimen.padding_extra)),
            tint = MaterialTheme.colorScheme.secondary
        )

        Text(
            text = label,
            modifier = Modifier.width(dimensionResource(R.dimen.padding_plus)),
            style = MaterialTheme.typography.labelLarge,
            maxLines = 2,
            textAlign = TextAlign.Center
        )
    }

}

@Preview
@Composable
internal fun PreviewMainMenu() {
    NoteAppTheme(darkTheme = false) {
        MainMenu(
            uiState = AuthUiState(user = "username"),
            onClick = { },
            changeUsernameOnClick = { },
            changePasswordOnClick = { },
            deleteAccountOnClick = { },
            logoutOnClick = { }
        )
    }
}