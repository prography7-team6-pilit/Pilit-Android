package com.prography.pilit.data.datasource.remote
import android.content.Context
import com.prography.pilit.AuthInterceptorClient
import com.prography.pilit.OtherInterceptorClient
import com.prography.pilit.data.datasource.local.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    @OtherInterceptorClient
    fun provideHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @AuthInterceptorClient
    fun providesAuthHttpClient(
        @ApplicationContext context: Context
    ): OkHttpClient {
        val loggingInterceptorClient = HttpLoggingInterceptor()
        loggingInterceptorClient.level = HttpLoggingInterceptor.Level.BODY
        val authInterceptor = AuthInterceptor(SharedPreferences(context))
        return OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptorClient)
            .addInterceptor(authInterceptor)
            .build()
    }


    @Provides
    @Singleton
    @OtherInterceptorClient
    fun provideRetrofit(
        @OtherInterceptorClient okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://team6-api.prography.org")
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    @AuthInterceptorClient
    fun provideAuthRetrofit(
        @AuthInterceptorClient okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://team6-api.prography.org")
            .client(okHttpClient)
            .build()
}