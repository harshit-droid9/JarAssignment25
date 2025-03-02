package tech.oklocation.jar.assignment.data.repository

import tech.oklocation.jar.assignment.data.remote.RemoteDataSource
import tech.oklocation.jar.assignment.data.remote.model.OnboardingData
import tech.oklocation.jar.assignment.domain.repository.Repository
import javax.inject.Inject


class RepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : Repository {

    override suspend fun getOnboardingData(): OnboardingData {
        val getOnboardingDataFromRemote = remoteDataSource.fetchOnboardingData()
        if (getOnboardingDataFromRemote.success) {
            return getOnboardingDataFromRemote.data.onboardingData
        } else {
            throw IllegalStateException("Api failure")
        }
    }
}