package tech.oklocation.jar.assignment.domain.repository

import tech.oklocation.jar.assignment.data.remote.model.OnboardingData

interface Repository {

    suspend fun getOnboardingData(): OnboardingData

}