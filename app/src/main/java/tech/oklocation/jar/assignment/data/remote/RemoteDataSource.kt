package tech.oklocation.jar.assignment.data.remote

import tech.oklocation.jar.assignment.data.remote.model.OnboardingResponse
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun fetchOnboardingData(): OnboardingResponse {
        return apiService.getOnboardingData()
    }
}