package tech.oklocation.jar.assignment.presentation.composables

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import tech.oklocation.jar.assignment.R

@Composable
fun HeaderSection(@StringRes heading: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            modifier = Modifier,
            painter = painterResource(R.drawable.ic_back_button),
            contentDescription = "back button",
            tint = MaterialTheme.colorScheme.onPrimary
        )

        Text(
            text = stringResource(heading),
            style = MaterialTheme.typography.headlineMedium.copy(
                color = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}