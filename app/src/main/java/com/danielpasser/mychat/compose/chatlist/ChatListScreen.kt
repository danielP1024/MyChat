package com.danielpasser.mychat.compose.chatlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.danielpasser.mychat.R
import com.danielpasser.mychat.compose.UserMenu
import com.danielpasser.mychat.compose.VerticalSpacer
import com.danielpasser.mychat.models.ChatMessagePreview
import com.danielpasser.mychat.viewmodels.ChatListScreenViewModel

@Composable
fun ChatListScreen(
    viewModel: ChatListScreenViewModel = hiltViewModel(),
    onProfileClicked: () -> Unit,
    onEditProfileClicked: () -> Unit,
    onLogOffClicked: () -> Unit,
    onMessageClicked: (String) -> Unit
) {
    Scaffold(
        topBar = {
            ChatListTopBar(
                onProfileClicked = onProfileClicked,
                onEditProfileClicked = onEditProfileClicked,
                onLogOffClicked = onLogOffClicked
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    start = dimensionResource(id = R.dimen.padding_small),
                    end = dimensionResource(id = R.dimen.padding_small),
                    bottom = dimensionResource(id = R.dimen.padding_small)
                )
                .fillMaxWidth()
        ) {


            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(items = viewModel.messageList) { message ->
                    ChatListItem(
                        chatMessagePreview = message,
                        onClick = { onMessageClicked(message.userName) }
                    )
                    VerticalSpacer()
                }

            }
        }
    }
}

@Composable
private fun ChatListItem(chatMessagePreview: ChatMessagePreview, onClick: () -> Unit) {
    OutlinedCard(onClick = onClick) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small))
        ) {
            Icon(
                painter = painterResource(R.drawable.baseline_account_circle_24),
                contentDescription = ""
            )
            VerticalSpacer()
            Column {
                Text(text = chatMessagePreview.userName)
                Text(text = chatMessagePreview.message)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListTopBar(
    modifier: Modifier = Modifier, onProfileClicked: () -> Unit, onEditProfileClicked: () -> Unit,
    onLogOffClicked: () -> Unit,
) {
    TopAppBar(

        title = {

            Text(stringResource(id = R.string.chat))

        }, modifier = modifier.statusBarsPadding(),
        actions = {
            UserMenu(
                onProfileClicked = onProfileClicked,
                onEditProfileClicked = onEditProfileClicked,
                onLogOffClicked = onLogOffClicked
            )
//            var expanded by rememberSaveable {
//                mutableStateOf(false)
//            }
//            IconButton(onClick = { expanded = !expanded }) {
//                Icon(
//                    imageVector = Icons.Default.MoreVert,
//                    contentDescription = stringResource(id = R.string.edit_profile)
//                )
//                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
//                    DropdownMenuItem(
//                        text = { Text(text = stringResource(id = R.string.profile)) },
//                        onClick = onProfileClicked
//                    )
//                }
//            }
        }
    )
}