package tech.oklocation.jar.assignment.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tech.oklocation.jar.assignment.data.repository.RepositoryImpl
import tech.oklocation.jar.assignment.domain.Repository

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    abstract fun bindRepository(repositoryImpl: RepositoryImpl): Repository
}