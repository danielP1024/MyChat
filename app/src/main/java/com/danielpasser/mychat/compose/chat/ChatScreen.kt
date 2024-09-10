package com.danielpasser.mychat.compose.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.danielpasser.mychat.R
import com.danielpasser.mychat.compose.BackButton
import com.danielpasser.mychat.compose.HorizontalSpacer
import com.danielpasser.mychat.compose.UserMenu
import com.danielpasser.mychat.compose.VerticalSpacer
import com.danielpasser.mychat.models.ChatMessage
import com.danielpasser.mychat.utils.Utils.Companion.fullDate
import com.danielpasser.mychat.viewmodels.ChatScreenViewModel

@Composable
fun ChatScreen(
    viewModel: ChatScreenViewModel = hiltViewModel(),
    onProfileClicked: () -> Unit,
    onEditProfileClicked: () -> Unit,
    onLogOffClicked: () -> Unit,
    userName: String,
    onBackClicked: () -> Unit,
) {
    Scaffold(
        topBar = {
            ChatTopBar(
                onProfileClicked = onProfileClicked,
                userName = userName,
                onEditProfileClicked = onEditProfileClicked,
                onLogOffClicked = onLogOffClicked,
                onBackClicked = onBackClicked
            )
        },
        bottomBar = { ChatBottomBar() }) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    start = dimensionResource(id = R.dimen.padding_small),
                    end = dimensionResource(id = R.dimen.padding_small),
                    bottom = dimensionResource(id = R.dimen.padding_small)
                )
                .fillMaxWidth()
        ) {
            items(items = viewModel.messageList) { message ->
                val isCurrentUser = message.userId == viewModel.myUserId
                val padding =
                    if (isCurrentUser) PaddingValues(start = dimensionResource(id = R.dimen.padding_extra_large)) else PaddingValues(
                        end = dimensionResource(id = R.dimen.padding_extra_large)
                    )
                val align = if (isCurrentUser) Alignment.End else Alignment.Start
                Column(modifier = Modifier.fillMaxWidth()) {
                    ChatItem(
                        message = message,
                        isCurrentUser = isCurrentUser,
                        modifier = Modifier
                            .align(align)
                            .padding(padding)
                    )
                    VerticalSpacer()
                }
            }
        }
    }
}

@Composable
private fun ChatItem(message: ChatMessage, modifier: Modifier, isCurrentUser: Boolean) {
    val colors = if (isCurrentUser) CardDefaults.elevatedCardColors(
        containerColor = Color.Green
    ) else CardDefaults.elevatedCardColors()

    ElevatedCard(modifier = modifier, colors = colors) {
        Column(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small))) {
            Text(
                text = message.text,
            )
            message.date.fullDate()?.let {
                Text(
                    text = it,
                    fontSize = 8.sp,
                    modifier = Modifier.align(Alignment.End),
                    style = TextStyle(lineHeight = 1.em),
                    textAlign = TextAlign.End
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatTopBar(
    onProfileClicked: () -> Unit,
    onEditProfileClicked: () -> Unit,
    onLogOffClicked: () -> Unit,
    onBackClicked: () -> Unit,
    userName: String,
) {
    TopAppBar(
        navigationIcon = {
            BackButton(onBackClicked)
        },
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.baseline_account_circle_24),
                    contentDescription = ""
                )
                HorizontalSpacer()
                Text(userName)
            }
        }, modifier = Modifier.statusBarsPadding(),
        actions = {
            UserMenu(
                onProfileClicked = onProfileClicked,
                onEditProfileClicked = onEditProfileClicked,
                onLogOffClicked = onLogOffClicked
            )
        }
    )
}


@Composable
private fun ChatBottomBar(
) {
    BottomAppBar(modifier = Modifier.statusBarsPadding())
    {
        OutlinedTextField(modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),

            value = "", onValueChange = {}, trailingIcon = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = ""
                    )
                }
            })
    }
}