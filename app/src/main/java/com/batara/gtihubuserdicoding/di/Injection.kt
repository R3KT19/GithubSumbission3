package com.batara.gtihubuserdicoding.di

import android.content.Context
import com.batara.gtihubuserdicoding.data.remote.retrofit.ApiConfig
import com.batara.gtihubuserdicoding.data.repository.UsersRepository
import com.batara.gtihubuserdicoding.data.local.room.UsersDatabase

object Injection {
    fun provideRepository(context: Context) : UsersRepository {
        val apiService = ApiConfig.getApiService()
        val database = UsersDatabase.getInstance(context)
        val dao = database.usersDao()
        return UsersRepository.getInstance(apiService, dao)
    }
}