package com.danielpasser.mychat.compose.edituser

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.danielpasser.mychat.compose.MyDatePicker
import com.danielpasser.mychat.R
import com.danielpasser.mychat.compose.BackButton
import com.danielpasser.mychat.compose.HorizontalSpacer
import com.danielpasser.mychat.compose.ImageLoader
import com.danielpasser.mychat.models.networkmodels.response.errors
import com.danielpasser.mychat.utils.ApiResponse
import com.danielpasser.mychat.viewmodels.UserEditViewModel

@Composable
fun EditUserProfileScreen(
    viewModel: UserEditViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val context = LocalContext.current
    Scaffold(topBar = { EditUserProfileTopBar(onBackClicked = navigateBack) }) { paddingValues ->
        Box(
            modifier = Modifier.padding(
                top = paddingValues.calculateTopPadding(),
                start = dimensionResource(id = R.dimen.padding_small),
                end = dimensionResource(id = R.dimen.padding_small),
                bottom = dimensionResource(id = R.dimen.padding_small)
            )
        ) {
            UserEditor(viewModel)
            when (val saveUserResponse = viewModel.saveUserResponse.collectAsState().value) {
                is ApiResponse.Failure -> {
                    LaunchedEffect(Unit) {
                        Toast.makeText(
                            context,
                            "${saveUserResponse.code} ${saveUserResponse.error.errors()}",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }

                ApiResponse.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                }

                is ApiResponse.Success -> {
                    LaunchedEffect(Unit) {
                        Toast.makeText(
                            context,
                            context.getText(R.string.user_saved).toString(),
                            Toast.LENGTH_SHORT,
                        ).show()
                        navigateBack()
                    }
                }
                null -> {}
            }
            when (val userResponse = viewModel.userResponse.collectAsState().value) {
                is ApiResponse.Failure -> {
                    LaunchedEffect(Unit) {
                        Toast.makeText(
                            context,
                            "${userResponse.code} ${userResponse.error.errors()}",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }

                ApiResponse.Loading -> {
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
}

@Composable
fun UserEditor(viewModel: UserEditViewModel) {
    Column(modifier = Modifier.fillMaxWidth()) {
        ImageLoader(
            onImagedSelected = { viewModel.onImagedSelected(it) },
            avatar = viewModel.avatar.collectAsState().value,
            url = viewModel.avatarUrl.collectAsState().value

        )

        HorizontalSpacer()
        MyTextField(
            value = viewModel.name.collectAsState().value,
            onValueChanged = { viewModel.onNameChanged(it) },
            placeholderText = stringResource(id = R.string.enter_name),
            label = stringResource(id = R.string.name)
        )

        HorizontalSpacer()
        MyTextField(
            value = viewModel.city.collectAsState().value,
            onValueChanged = { viewModel.onCityChanged(it) },
            placeholderText = stringResource(id = R.string.enter_city),
            label = stringResource(id = R.string.city)
        )

        HorizontalSpacer()
        MyTextField(
            value = viewModel.status.collectAsState().value,
            onValueChanged = { viewModel.onStatusChanged(it) },
            placeholderText = stringResource(id = R.string.enter_status),
            label = stringResource(id = R.string.status)
        )
        HorizontalSpacer()
        MyDatePicker(
            timeInMillis = viewModel.birthday.collectAsState().value,
            onDateSelected = { viewModel.onBirthDayChanged(it) },
            label = stringResource(id = R.string.birthday)
        )
        HorizontalSpacer()
        MyTextField(
            value = viewModel.vk.collectAsState().value,
            onValueChanged = { viewModel.onVkChanged(it) },
            placeholderText = stringResource(id = R.string.enter_vk),
            label = stringResource(id = R.string.vk)
        )
        HorizontalSpacer()
        MyTextField(
            value = viewModel.instagram.collectAsState().value,
            onValueChanged = { viewModel.onInstagramChanged(it) },
            placeholderText = stringResource(id = R.string.enter_instagram),
            label = stringResource(id = R.string.instagram)
        )
        Button(onClick = { viewModel.saveUser() }, modifier = Modifier.align(Alignment.End)) {
            Text(text = "Save")
        }

    }

}

@Composable
private fun MyTextField(
    value: String,
    onValueChanged: (String) -> Unit,
    placeholderText: String,
    label: String
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(),

        value = value,
        onValueChange = onValueChanged,
        singleLine = true,
        placeholder = {
            Text(
                text = placeholderText, style = MaterialTheme.typography.bodyMedium
            )
        },
        label = {
            Text(
                text = label, style = MaterialTheme.typography.bodyMedium
            )
        },
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditUserProfileTopBar(
    onBackClicked: () -> Unit
) {
    TopAppBar(
        navigationIcon = { BackButton(onBackClicked = onBackClicked) },
        title = {
            Text(stringResource(id = R.string.edit_profile))
        }, modifier = Modifier.statusBarsPadding()
    )
}