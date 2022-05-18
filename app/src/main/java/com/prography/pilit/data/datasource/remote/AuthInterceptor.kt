package com.prography.pilit.data.datasource.remote

import com.prography.pilit.data.datasource.local.SharedPreferences
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val preferences: SharedPreferences) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer " + preferences.accessToken).build()

        return chain.proceed(request)
    }
}