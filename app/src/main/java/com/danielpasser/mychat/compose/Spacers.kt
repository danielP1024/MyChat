package com.danielpasser.mychat.compose

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.danielpasser.mychat.R

@Composable
fun VerticalSpacer()
{
    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_small)))
}

@Composable
fun HorizontalSpacer()
{
    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding_small)))
}