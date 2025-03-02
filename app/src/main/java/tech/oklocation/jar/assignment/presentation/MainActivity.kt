package tech.oklocation.jar.assignment.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import tech.oklocation.jar.assignment.App
import tech.oklocation.jar.assignment.presentation.onboarding.OnboardingScreen
import tech.oklocation.jar.assignment.presentation.theme.JarAssignmentTheme
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory

    private val viewModel: MainViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as App).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JarAssignmentTheme {
                MainContent(viewModel)
            }
        }
    }
}

@Composable
fun MainContent(viewModel: MainViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        when (uiState) {
            is UiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            is UiState.Success -> {
                val data = (uiState as UiState.Success).data
                OnboardingScreen(paddingValues = innerPadding, onboardingData = data)
            }

            is UiState.Error -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = stringResource((uiState as UiState.Error).message),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }
        }
    }
}



