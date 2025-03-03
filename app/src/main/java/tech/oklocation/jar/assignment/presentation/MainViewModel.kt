package tech.oklocation.jar.assignment.presentation


import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import tech.oklocation.jar.assignment.R
import tech.oklocation.jar.assignment.data.remote.model.OnboardingData
import tech.oklocation.jar.assignment.domain.repository.Repository

sealed interface UiState {
    data object Loading : UiState
    data class Success(
        val onboardingData: OnboardingData? = null
    ) : UiState

    data class Error(@StringRes val message: Int) : UiState
}

class MainViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun loadOnboardingData() = viewModelScope.launch(Dispatchers.IO) {
        try {
            val response = repository.getOnboardingData()
            _uiState.value = UiState.Success(response)
        } catch (e: Exception) {
            _uiState.value = UiState.Error(R.string.api_error)
        }
    }
}
