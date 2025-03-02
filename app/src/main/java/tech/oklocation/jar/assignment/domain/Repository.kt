package tech.oklocation.jar.assignment.domain

import tech.oklocation.jar.assignment.data.remote.model.OnboardingDataContainer

interface Repository {

    suspend fun getOnboardingData(): OnboardingDataContainer

}