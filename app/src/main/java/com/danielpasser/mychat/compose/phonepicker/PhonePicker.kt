package com.danielpasser.mychat.compose.phonepicker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.KeyboardType
import com.danielpasser.mychat.R
import com.danielpasser.mychat.models.Country
import com.danielpasser.mychat.utils.Utils

@Composable
fun PhonePicker(
    phoneNumber: String,
    onValueChanged: (String) -> Unit = {},
    onCountrySelected: (Country) -> Unit = {},
    country: Country,
    countriesList: List<Country> = listOf(),
    enabled: Boolean = true
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(),

        value = phoneNumber,
        onValueChange = onValueChanged,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        singleLine = true,
        placeholder = {
            Text(
                text = "Enter Your Phone Number", style = MaterialTheme.typography.bodyMedium
            )
        },
        label = {
            Text(
                text = "Phone Number", style = MaterialTheme.typography.bodyMedium
            )
        },
        leadingIcon = {
            CountryCodePicker(
                country = country,
                countriesList = countriesList,
                onCountrySelected = onCountrySelected,
                enabled = enabled
            )
        },
        enabled = enabled
    )
}


@Composable
private fun CountryCodePicker(
    country: Country,
    countriesList: List<Country>,
    onCountrySelected: (Country) -> Unit,
    enabled: Boolean
) {
    var isPickerOpen by remember { mutableStateOf(false) }

    Box {
        CountryView(
            country,
            modifier = Modifier.clickable(enabled = enabled) { isPickerOpen = true })
        if (isPickerOpen) {
            CountryPickerDialog(
                countriesList,
                onCountryClicked = {
                    onCountrySelected(it)
                    isPickerOpen = false
                },
                onDismissRequest = { isPickerOpen = false })
        }
    }
}

@Composable
private fun CountryView(country: Country, modifier: Modifier = Modifier) {
    Row(modifier = modifier.padding(dimensionResource(id = R.dimen.padding_small))) {
        Text(text = Utils.getCountryInfoShort(country))
        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = null,
        )
    }
}