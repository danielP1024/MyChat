package com.danielpasser.mychat.compose.userprofile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.danielpasser.mychat.R
import com.danielpasser.mychat.compose.BackButton
import com.danielpasser.mychat.compose.HorizontalSpacer
import com.danielpasser.mychat.compose.ShowToastCompose
import com.danielpasser.mychat.compose.VerticalSpacer
import com.danielpasser.mychat.models.networkmodels.response.ProfileData
import com.danielpasser.mychat.models.networkmodels.response.UserResponse
import com.danielpasser.mychat.utils.ApiResponse
import com.danielpasser.mychat.utils.BASE_URL_MEDIA
import com.danielpasser.mychat.utils.Utils.Companion.dateToZodiac
import com.danielpasser.mychat.viewmodels.UserProfileViewModel

@Composable
fun UserProfileScreen(
    viewModel: UserProfileViewModel = hiltViewModel(),
    onEditProfileClicked: () -> Unit,
    onBackClicked: () -> Unit
) {


    var userResponse by remember {
        mutableStateOf(UserResponse())
    }
    when (val _userResponse = viewModel.userResponse.collectAsState().value) {
        is ApiResponse.Failure -> {
            ShowToastCompose(_userResponse)
        }

        is ApiResponse.Loading -> {}
        is ApiResponse.Success -> {
            userResponse = _userResponse.data
        }

        null -> {}
    }


    Scaffold(
        topBar = {
            UserProfileTopBar(
                onEditProfileClicked = onEditProfileClicked,
                onBackClicked = onBackClicked
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(
                top = paddingValues.calculateTopPadding(),
                start = dimensionResource(id = R.dimen.padding_small),
                end = dimensionResource(id = R.dimen.padding_small),
                bottom = dimensionResource(id = R.dimen.padding_small)
            )
        ) {
            UserProfile(viewModel.user.collectAsState().value)
            /*   Button(onClick = { viewModel.checkRefreshToken() }) {
                   Text(text = "checkRefreshToken")
               }
               Button(onClick = { viewModel.getUser() }) {
                   Text(text = "GetUser")
               }
             */
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun UserProfile(userResponse: ProfileData?) {
    Column {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small))) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    GlideImage(
                        model = BASE_URL_MEDIA + userResponse?.avatars?.miniAvatar,
                        contentDescription = "",
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                    )
                    {
                        it.placeholder(R.drawable.baseline_account_circle_24)
                    }


                    HorizontalSpacer()
                    Column {
                        Text(
                            text = userResponse?.name ?: "",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        VerticalSpacer()
                        Text(
                            text = userResponse?.phone ?: "",
                        )
                    }
                }
                VerticalSpacer()
                Text(
                    text = "${stringResource(id = R.string.birthday)}: ${userResponse?.birthday ?: ""}",
                )
                VerticalSpacer()
                Text(
                    text = "${stringResource(id = R.string.birthday)}: ${userResponse?.birthday?.dateToZodiac() ?: ""}",
                )
                VerticalSpacer()
                Text(
                    text = "${stringResource(id = R.string.city)} ${userResponse?.city ?: ""}",
                )
                VerticalSpacer()
            }
        }

        VerticalSpacer()
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small))) {
                Text(
                    text = stringResource(id = R.string.status),
                    style = MaterialTheme.typography.bodySmall
                )
                VerticalSpacer()
                Text(text = userResponse?.status ?: "STATUS STATUS")
            }
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserProfileTopBar(
    modifier: Modifier = Modifier,
    onEditProfileClicked: () -> Unit,
    onBackClicked: () -> Unit
) {
    TopAppBar(
        navigationIcon = {
            BackButton(onBackClicked)
        },
        title = {
            Text(stringResource(id = R.string.profile))
        }, modifier = modifier.statusBarsPadding(),
        actions = {
            var expanded by rememberSaveable {
                mutableStateOf(false)
            }
            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = stringResource(id = R.string.edit_profile)
                )
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    DropdownMenuItem(
                        text = { Text(text = stringResource(id = R.string.edit_profile)) },
                        onClick = {
                            expanded = false
                            onEditProfileClicked()
                        }
                    )
                }
            }
        }
    )
}