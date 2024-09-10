package com.danielpasser.mychat.compose.loginscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.danielpasser.mychat.R
import com.danielpasser.mychat.compose.ShowToastCompose
import com.danielpasser.mychat.models.networkmodels.response.AuthInfoResponse
import com.danielpasser.mychat.utils.ApiResponse

@Composable
fun EnterCodeDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: (String) -> Unit,
    checkAuthCode: ApiResponse<AuthInfoResponse>?
) {
    var text by remember { mutableStateOf("133337") }
    var isError by remember { mutableStateOf(false) }



    Box(Modifier.wrapContentSize()) {
        AlertDialog(
            title = {
                Text(text = stringResource(id = R.string.enter_sms))
            },
            text = {
                OutlinedTextField(
                    value = text, onValueChange = {
                        text = it
                        isError = false
                    },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.enter_sms),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(id = R.string.sms),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    supportingText = {
                        if (isError) {
                            Text(
                                text = stringResource(id = R.string.code_incorrect),
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )
            },
            onDismissRequest = {
                onDismissRequest()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirmation(text)
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
                    Text(text = stringResource(id = R.string.dismiss))
                }
            }
        )
        when (checkAuthCode) {
            is ApiResponse.Failure -> {
                if (checkAuthCode.code == 404) isError = true
                else ShowToastCompose(response = checkAuthCode)
            }

            is ApiResponse.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }

            else -> {}
        }
    }

}
