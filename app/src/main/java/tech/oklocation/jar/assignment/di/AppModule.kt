package tech.oklocation.jar.assignment.di

import dagger.Module
import dagger.Provides
import tech.oklocation.jar.assignment.data.remote.RemoteDataSource
import tech.oklocation.jar.assignment.data.repository.RepositoryImpl
import tech.oklocation.jar.assignment.domain.repository.Repository
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideRepository(remoteDataSource: RemoteDataSource): Repository {
        return RepositoryImpl(remoteDataSource)
    }
}