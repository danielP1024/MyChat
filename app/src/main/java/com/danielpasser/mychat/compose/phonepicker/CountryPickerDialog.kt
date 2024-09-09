package com.danielpasser.mychat.compose.phonepicker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import com.danielpasser.mychat.R
import com.danielpasser.mychat.models.Country
import com.danielpasser.mychat.utils.Utils

@Composable
fun CountryPickerDialog(
    countryList: List<Country>,
    onCountryClicked: (Country) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val context = LocalContext.current

    var value by remember { mutableStateOf("") }


    val filteredCountries by remember(context, value) {
        derivedStateOf {
            if (value.isEmpty()) {
                countryList
            } else {
                Utils.searchCountry(value, countryList)
            }
        }
    }

    Dialog(onDismissRequest = onDismissRequest) {
        Scaffold(topBar = {
            CountryPickerDialogTopBar(
                value = value, onValueChange = { value = it }, onDismissRequest = onDismissRequest
            )
        }) { paddingValues ->
            LazyColumn(modifier = Modifier.padding(paddingValues)) {
                items(items = filteredCountries) { country ->
                    HorizontalDivider(color = Color.Gray)
                    CountryItem(country, onCountryClicked = { onCountryClicked(country) })
                }

            }
        }
    }
}

@Composable
private fun CountryItem(country: Country, onCountryClicked: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(dimensionResource(id = R.dimen.padding_small))

            .clickable(onClick = { onCountryClicked() })
    ) {
        Text(
            text = Utils.getCountryInfoShort(country),
            modifier = Modifier.padding(end = dimensionResource(id = R.dimen.padding_small))
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = country.name ?: "", textAlign = TextAlign.Right
        )
    }
}


@Composable
private fun CountrySearch(
    value: String,
    onValueChange: (String) -> Unit,
) {
    TextField(modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        placeholder = {
            Text(text = "Search Country")
        },
        leadingIcon = {
            Icon(Icons.Outlined.Search, contentDescription = "Search")
        },
        trailingIcon = {
            IconButton(onClick = { onValueChange("") }) {
                Icon(Icons.Outlined.Clear, contentDescription = "Clear")
            }
        })
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CountryPickerDialogTopBar(
    value: String, onValueChange: (String) -> Unit, onDismissRequest: () -> Unit
) {
    Column {
        TopAppBar(title = {
            Text(stringResource(id = R.string.select_country))
        }, modifier = Modifier.statusBarsPadding(), actions = {
            IconButton(onClick = onDismissRequest) {
                Icon(
                    imageVector = Icons.Filled.Clear, contentDescription = "Close"
                )
            }
        })
        CountrySearch(value = value, onValueChange = onValueChange)
    }
}