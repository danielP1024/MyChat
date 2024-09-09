package com.danielpasser.mychat.compose.loginscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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
import com.danielpasser.mychat.viewmodels.LoginViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onRegisterNewUserClicked: (Registration) -> Unit,
//    onLogin: () -> Unit
) {

    val sendAuthCode = viewModel.sendAuthCode.collectAsState().value

    ShowToastCompose(sendAuthCode)

//    LaunchedEffect(Unit) {
//        viewModel.isLogin.collect { if (it) onLogin() }
//    }
    Scaffold(topBar = { LoginTopBar() }) { paddingValues ->
        Box(Modifier.fillMaxSize()) {
            if (sendAuthCode is ApiResponse.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
            Column(
                modifier = Modifier.padding(
                    top = paddingValues.calculateTopPadding(),
                    start = dimensionResource(id = R.dimen.padding_small),
                    end = dimensionResource(id = R.dimen.padding_small),
                    bottom = dimensionResource(id = R.dimen.padding_small)
                )
            ) {
                //val sendAuthCode = viewModel.sendAuthCode.collectAsState().value

                if (viewModel.showUserNotRegDialog.collectAsState().value == true) {
                    UserNotRegDialog(
                        onDismissRequest = { viewModel.dismissUserNotRegDialog() },
                        onConfirmation = {
                            viewModel.dismissUserNotRegDialog()
                            onRegisterNewUserClicked(viewModel.registerNewUserInfo())
                        })
                }
                if (sendAuthCode is ApiResponse.Success) {
                    EnterCodeDialog(
                        onDismissRequest = { viewModel.clearAlertDialog() },
                        onConfirmation = { viewModel.onSmsCodeReceived(it) },
                        checkAuthCode = viewModel.checkAuthCode.collectAsState().value
                    )
                }
                PhonePicker(
                    phoneNumber = viewModel.phoneNumber.collectAsState().value,
                    onValueChanged = { viewModel.onPhoneNumberChanged(it) },
                    onCountrySelected = { viewModel.onCountrySelected(it) },
                    country = viewModel.selectedCountry.collectAsState().value,
                    countriesList = viewModel.countries.collectAsState().value
                )
                VerticalSpacer()
                Button(modifier = Modifier.align(Alignment.End), onClick = {
                    viewModel.sendAuthCode()
                }) {
                    Text(text = stringResource(id = R.string.login))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoginTopBar(
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = {
            Text(stringResource(id = R.string.login_title))
        }, modifier = modifier.statusBarsPadding()
    )
}