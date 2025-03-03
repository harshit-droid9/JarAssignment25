package tech.oklocation.jar.assignment.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import tech.oklocation.jar.assignment.App
import tech.oklocation.jar.assignment.presentation.landing.LandingScreen
import tech.oklocation.jar.assignment.presentation.onboarding.OnboardingScreen
import tech.oklocation.jar.assignment.presentation.theme.JarAssignmentTheme
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    companion object {
        const val NAV_ONBOARDING_SCREEN = "/onboarding"
        const val NAV_LANDING_SCREEN = "/landing"
    }

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

    @Composable
    fun MainContent(viewModel: MainViewModel) {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = NAV_ONBOARDING_SCREEN,
            modifier = Modifier
        ) {
            composable(NAV_ONBOARDING_SCREEN) {
                OnboardingScreen(
                    viewModel = viewModel,
                    onNavigateToLandingPage = { navController.navigate(NAV_LANDING_SCREEN) },
                )
            }

            composable(NAV_LANDING_SCREEN) {
                LandingScreen()
            }
        }
    }

}


