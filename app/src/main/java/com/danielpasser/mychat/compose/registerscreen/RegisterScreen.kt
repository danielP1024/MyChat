package com.danielpasser.mychat.compose.registerscreen

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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.danielpasser.mychat.R
import com.danielpasser.mychat.compose.Registration
import com.danielpasser.mychat.compose.ShowToastCompose
import com.danielpasser.mychat.compose.VerticalSpacer
import com.danielpasser.mychat.compose.phonepicker.PhonePicker
import com.danielpasser.mychat.utils.ApiResponse
import com.danielpasser.mychat.viewmodels.RegisterViewModel

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel(),
    reg: Registration,
) {
    LaunchedEffect(Unit) {
        viewModel.initData(reg)
    }

    Scaffold(topBar = { RegisterTopBar() }) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    start = dimensionResource(id = R.dimen.padding_small),
                    end = dimensionResource(id = R.dimen.padding_small),
                    bottom = dimensionResource(id = R.dimen.padding_small)
                )
                .fillMaxWidth()
        ) {
            Column()
            {
                PhonePicker(
                    phoneNumber = viewModel.phoneNumber.collectAsState().value,
                    country = viewModel.selectedCountry.collectAsState().value,
                    enabled = false
                )
                VerticalSpacer()
                OutlinedTextField(
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.enter_name),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    label = {
                        Text(
                            text = stringResource(id = R.string.name),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    value = viewModel.name.collectAsState().value,
                    onValueChange = { viewModel.onNameChanged(it) })
                VerticalSpacer()
                OutlinedTextField(
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.enter_user_name),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(id = R.string.user_name),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    value = viewModel.userName.collectAsState().value,
                    onValueChange = { viewModel.onUsernameChanged(it) })
                VerticalSpacer()
                Button(modifier = Modifier.align(Alignment.End), onClick = {
                    viewModel.register()
                }) {
                    Text(text = stringResource(id = R.string.register))
                }
            }

            when (val sendAuthCode = viewModel.register.collectAsState().value) {
                is ApiResponse.Failure -> {
                    ShowToastCompose(response = sendAuthCode)
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RegisterTopBar(
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = {
            Text(stringResource(id = R.string.login_title))
        }, modifier = modifier.statusBarsPadding()
    )
}