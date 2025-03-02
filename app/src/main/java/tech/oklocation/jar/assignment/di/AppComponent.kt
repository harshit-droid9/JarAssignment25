package tech.oklocation.jar.assignment.di

import dagger.Component
import tech.oklocation.jar.assignment.data.remote.ApiService
import tech.oklocation.jar.assignment.domain.repository.Repository
import tech.oklocation.jar.assignment.presentation.MainActivity
import tech.oklocation.jar.assignment.presentation.MainViewModelFactory
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class])
interface AppComponent {
    fun repository(): Repository
    fun apiService(): ApiService

    fun inject(factory: MainViewModelFactory)

    fun inject(activity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(): AppComponent
    }
}