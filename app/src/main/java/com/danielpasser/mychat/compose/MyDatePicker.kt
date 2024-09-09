package com.danielpasser.mychat.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.danielpasser.mychat.utils.Utils.Companion.convertMillisToDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDatePicker(
    label:String,
    timeInMillis: Long?,
    onDateSelected: (Long?) -> Unit,
) {
    val datePickerState = rememberDatePickerState(timeInMillis)
    var showDatePicker by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = timeInMillis.convertMillisToDate(),
        onValueChange = { },
        label = { Text(label) },
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = { showDatePicker = !showDatePicker }) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Select date"
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
    )

    if (showDatePicker)
        DatePickerDialog(
            onDismissRequest = {showDatePicker=false},
            confirmButton = {
                TextButton(onClick = {
                    onDateSelected(datePickerState.selectedDateMillis)
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
}