package com.allerria.moneytracker

import android.app.Activity
import android.app.Application
import com.allerria.moneytracker.di.AppComponent
import com.allerria.moneytracker.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject
import timber.log.Timber


class MoneyTrackerApp : Application(), HasActivityInjector {

    companion object {
        lateinit var INSTANCE: MoneyTrackerApp
        lateinit var component: AppComponent
    }

    @Inject
    lateinit var mActivityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        component = createComponent()
        component.inject(this)
        initLogger()
    }

    private fun initLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun createComponent(): AppComponent {
        return DaggerAppComponent.builder().application(this).build()
    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return mActivityDispatchingAndroidInjector
    }

}