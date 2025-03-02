package tech.oklocation.jar.assignment

import android.app.Application
import tech.oklocation.jar.assignment.di.AppComponent
import tech.oklocation.jar.assignment.di.DaggerAppComponent

class App : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.factory().create()
    }
}