package com.prography.pilit

import android.app.Application
import com.prography.pilit.data.datasource.local.SharedPreferences
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PilitApplication : Application() {

    companion object {
        lateinit var preferences : SharedPreferences
    }

    override fun onCreate() {
        super.onCreate()
        preferences = SharedPreferences(applicationContext)
    }
}