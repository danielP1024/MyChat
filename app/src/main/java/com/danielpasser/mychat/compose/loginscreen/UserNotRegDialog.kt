package com.danielpasser.mychat.compose.loginscreen

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.danielpasser.mychat.R

@Composable
fun UserNotRegDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
) {
    AlertDialog(

        title = {
            Text(text = stringResource(id = R.string.user_not_found))
        },
        text = {
            Text(text = stringResource(id = R.string.want_reg))
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text(text = stringResource(id = R.string.yes))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(text = stringResource(id = R.string.no))
            }
        }
    )
}
