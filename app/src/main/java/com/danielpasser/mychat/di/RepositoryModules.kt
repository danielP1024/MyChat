package com.danielpasser.mychat.di

import com.danielpasser.mychat.network.AuthApiService
import com.danielpasser.mychat.network.UserApiService
import com.danielpasser.mychat.repositories.AuthRepository
import com.danielpasser.mychat.repositories.UserRepository
import com.danielpasser.mychat.room.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class RepositoryModules {
    @Provides
    fun provideAuthRepository(authApiService: AuthApiService) = AuthRepository(authApiService)

    @Provides
    fun provideUserRepository(userApiService: UserApiService, userDao: UserDao) =
        UserRepository(userApiService, userDao)
}