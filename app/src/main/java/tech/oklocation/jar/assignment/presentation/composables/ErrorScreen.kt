package tech.oklocation.jar.assignment.presentation.composables

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@Composable
fun ErrorScreen(paddingValues: PaddingValues, @StringRes errorMessage: Int) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = stringResource(errorMessage),
            style = MaterialTheme.typography.headlineSmall
        )
    }
}