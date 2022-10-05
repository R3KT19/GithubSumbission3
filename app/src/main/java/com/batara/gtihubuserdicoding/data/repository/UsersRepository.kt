package com.batara.gtihubuserdicoding.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.batara.gtihubuserdicoding.data.local.entity.UsersEntity
import com.batara.gtihubuserdicoding.data.local.room.UsersDao
import com.batara.gtihubuserdicoding.data.remote.retrofit.ApiService

class UsersRepository private constructor(
    private val apiService : ApiService,
    private val usersDao : UsersDao,
) {
    fun getUsers() : LiveData<Result<List<UsersEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.retrieveUser()
            val listUsers = response.map { users ->
                val isBookmarked = usersDao.isUsersBookmarked(users.login)
                UsersEntity(
                    users.login,
                    users.id,
                    users.avatarUrl,
                    isBookmarked
                )
            }
            usersDao.deleteAll()
            usersDao.insertUsers(listUsers)
            Log.d(TAG, "getUsers: test")
        }catch (e : Exception) {
            Log.d("UsersRepository", "getUsers: ${e.message.toString()}")
        }
        val localData: LiveData<Result<List<UsersEntity>>> = usersDao.getUsers().map {
            Result.Success(
                it
            )
        }
        emitSource(localData)
    }


    fun getBookmarkedUsers() : LiveData<List<UsersEntity>> {
        return usersDao.getBookmarkedUsers()
    }

    fun getSearchUser(username: String) : LiveData<Result<List<UsersEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.searchUser(username)
            val dataSearchUsers = response.items
            val listSearchUsers = dataSearchUsers.map { search ->
                val isBookmarked = usersDao.isUsersBookmarked(search.login)
                UsersEntity(
                    search.login,
                    search.id,
                    search.avatarUrl,
                    isBookmarked
                )
            }
            usersDao.deleteAll()
            usersDao.insertUsers(listSearchUsers)
        }catch (e : Exception) {
            Log.d(TAG, "getUsers: ${e.message.toString()}")
        }
        val localData: LiveData<Result<List<UsersEntity>>> = usersDao.searchUser(username).map {
            Result.Success(
                it
            )
        }
        emitSource(localData)
    }


    suspend fun setUsersBookmark(users: UsersEntity, bookmarkState: Boolean) {
        users.isBookmarked = bookmarkState
        usersDao.updateUsers(users)
    }

    suspend fun getDetailUsers(username: String) : UsersEntity {
        return usersDao.getDetailUser(username)
    }

    companion object{
        @Volatile
        private var instance: UsersRepository? = null
        fun getInstance(
            apiService: ApiService,
            usersDao: UsersDao,
        ) : UsersRepository =
            instance ?: synchronized(this) {
                instance ?: UsersRepository(apiService, usersDao)
            }.also { instance = it }

        private const val TAG = "UsersRepository"
    }
}