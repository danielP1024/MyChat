package com.danielpasser.mychat.compose

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.danielpasser.mychat.R

@Composable
fun UserMenu(
    onProfileClicked: () -> Unit,
    onEditProfileClicked: () -> Unit,
    onLogOffClicked: () -> Unit,
) {
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
                text = { Text(text = stringResource(id = R.string.profile)) },
                onClick = {
                    expanded = false
                    onProfileClicked()
                }
            )
            DropdownMenuItem(
                text = { Text(text = stringResource(id = R.string.edit_profile)) },
                onClick = {
                    expanded = false
                    onEditProfileClicked()
                }
            )
            DropdownMenuItem(
                text = { Text(text = stringResource(id = R.string.log_off)) },
                onClick = {
                    expanded = false
                    onLogOffClicked()
                }
            )
        }
    }
}