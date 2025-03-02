package tech.oklocation.jar.assignment.data.repository

import tech.oklocation.jar.assignment.data.remote.RemoteDataSource
import tech.oklocation.jar.assignment.data.remote.model.OnboardingDataContainer
import tech.oklocation.jar.assignment.domain.Repository
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : Repository {

    override suspend fun getOnboardingData(): OnboardingDataContainer {
        val getOnboardingDataFromRemote = remoteDataSource.fetchOnboardingData()
        if (getOnboardingDataFromRemote.success) {
            return getOnboardingDataFromRemote.data
        } else {
            throw IllegalStateException("Api failure")
        }
    }
}