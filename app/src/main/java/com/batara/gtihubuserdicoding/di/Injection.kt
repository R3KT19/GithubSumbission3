package com.batara.gtihubuserdicoding.di

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.batara.gtihubuserdicoding.data.local.entity.UsersEntity
import com.batara.gtihubuserdicoding.data.remote.retrofit.ApiConfig
import com.batara.gtihubuserdicoding.data.repository.UsersRepository
import com.batara.gtihubuserdicoding.data.local.room.UsersDatabase
import com.batara.gtihubuserdicoding.utlis.AppExecutors

object Injection {
    fun provideRepository(context: Context) : UsersRepository {
        val apiService = ApiConfig.getApiService()
        val database = UsersDatabase.getInstance(context)
        val dao = database.usersDao()
        val appExecutors = AppExecutors()
        return UsersRepository.getInstance(apiService, dao, appExecutors)
    }
}