package com.batara.gtihubuserdicoding

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.batara.gtihubuserdicoding.di.Injection
import com.batara.gtihubuserdicoding.data.repository.UsersRepository

class ViewModelFactory private constructor(private val usersRepository: UsersRepository) : ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UsersViewModel::class.java)) {
            return UsersViewModel(usersRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class " +modelClass.name)
    }

    companion object{
        @Volatile
        private var instance : ViewModelFactory? = null
        fun getInstance(context: Context) : ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}