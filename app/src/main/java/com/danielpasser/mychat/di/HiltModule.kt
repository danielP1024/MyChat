package com.danielpasser.mychat.di

import android.content.Context
import com.danielpasser.mychat.network.AuthApiService
import com.danielpasser.mychat.network.AuthAuthenticator1
import com.danielpasser.mychat.network.AuthInterceptor
import com.danielpasser.mychat.network.UserApiService
import com.danielpasser.mychat.repositories.TokenRepository
import com.danielpasser.mychat.room.AppDatabase
import com.danielpasser.mychat.room.UserDao
import com.danielpasser.mychat.utils.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class HiltModule {

    @Singleton
    @Provides
    fun provideTokenManager(@ApplicationContext context: Context): TokenRepository =
        TokenRepository(context)

    @Singleton
    @Provides
    fun provideRetrofitBuilder(): Retrofit.Builder =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())

    @Singleton
    @Provides
    fun provideAuthAPIService(retrofit: Retrofit.Builder): AuthApiService {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        return retrofit.client(okHttpClient)
            .build()
            .create(AuthApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        authAuthenticator: AuthAuthenticator1,
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .authenticator(authAuthenticator)
            .build()
    }

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun providePlantDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }



    @Singleton
    @Provides
    fun provideUserApiService(
        okHttpClient: OkHttpClient,
        retrofit: Retrofit.Builder
    ): UserApiService =
        retrofit
            .client(okHttpClient)
            .build()
            .create(UserApiService::class.java)
}