package tech.oklocation.jar.assignment.presentation.landing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import tech.oklocation.jar.assignment.R
import tech.oklocation.jar.assignment.presentation.composables.HeaderSection

@Composable
fun LandingScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(it)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.TopStart
        ) {
            HeaderSection(R.string.landing)
        }
    }
}