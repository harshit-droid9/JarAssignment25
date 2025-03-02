package tech.oklocation.jar.assignment.data.remote

import retrofit2.http.GET
import tech.oklocation.jar.assignment.data.remote.model.OnboardingResponse

interface ApiService {

    @GET("0a095cf2-a081-44af-965a-953b0fa6499b")
    suspend fun getOnboardingData(): OnboardingResponse
}