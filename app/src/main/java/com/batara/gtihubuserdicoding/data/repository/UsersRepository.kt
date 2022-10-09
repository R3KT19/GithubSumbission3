package com.batara.gtihubuserdicoding.data.repository

import android.util.Log
import androidx.lifecycle.*
import com.batara.gtihubuserdicoding.UserSearchResponse
import com.batara.gtihubuserdicoding.UsersResponseItem
import com.batara.gtihubuserdicoding.data.local.entity.UsersEntity
import com.batara.gtihubuserdicoding.data.local.room.UsersDao
import com.batara.gtihubuserdicoding.data.remote.retrofit.ApiService
import com.batara.gtihubuserdicoding.utlis.AppExecutors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UsersRepository private constructor(
    private val apiService : ApiService,
    private val usersDao : UsersDao,
    private val appExecutors: AppExecutors
) {
    private val result = MediatorLiveData<Result<List<UsersEntity>>>()

    fun getUsers() : LiveData<Result<List<UsersEntity>>> {
        result.value = Result.Loading
        val client = apiService.retrieveUser()
        client.enqueue(object : Callback<List<UsersResponseItem>> {
            override fun onResponse(
                call: Call<List<UsersResponseItem>>,
                response: Response<List<UsersResponseItem>>
            ) {
                if (response.isSuccessful) {
                    val users = response.body()
                    val usersList = ArrayList<UsersEntity>()
                    appExecutors.diskIO.execute {
                        users?.forEach { users ->
                            val isBookmarked = usersDao.isUsersBookmarked(users.login)
                            val user = UsersEntity(
                                users.login,
                                users.id,
                                users.avatarUrl,
                                isBookmarked
                            )
                            usersList.add(user)
                        }
                        usersDao.deleteAll()
                        usersDao.insertUsers(usersList)
                    }
                }
            }

            override fun onFailure(call: Call<List<UsersResponseItem>>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
            }
        })

        val localData = usersDao.getUsers()
        result.addSource(localData) { newData: List<UsersEntity> ->
            result.value = Result.Success(newData)
        }

        return result
    }


    fun getBookmarkedUsers() : LiveData<List<UsersEntity>> {
        return usersDao.getBookmarkedUsers()
    }

    fun getSearchUser(username: String) : LiveData<Result<List<UsersEntity>>> {
        result.value = Result.Loading
        val client = apiService.searchUser(username)
        client.enqueue(object : Callback<UserSearchResponse> {
            override fun onResponse(
                call: Call<UserSearchResponse>,
                response: Response<UserSearchResponse>
            ) {
                if (response.isSuccessful) {
                    val users = response.body()?.items
                    val usersList = ArrayList<UsersEntity>()
                    appExecutors.diskIO.execute {
                        users?.forEach { users ->
                            val isBookmarked = usersDao.isUsersBookmarked(users.login)
                            val user = UsersEntity(
                                users.login,
                                users.id,
                                users.avatarUrl,
                                isBookmarked
                            )
                            usersList.add(user)
                        }
                        usersDao.deleteAll()
                        usersDao.insertUsers(usersList)
                    }
                }
            }

            override fun onFailure(call: Call<UserSearchResponse>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
            }
        })

        val localData = usersDao.searchUser(username)
        result.addSource(localData) { newData: List<UsersEntity> ->
            result.value = Result.Success(newData)
        }
        return result
    }


    fun setUsersBookmark(users: UsersEntity, bookmarkState: Boolean) {
        users.isBookmarked = bookmarkState
        usersDao.updateUsers(users)
    }

    fun getDetailUsers(username: String) : UsersEntity {
        return usersDao.getDetailUser(username)
    }

    companion object {
        @Volatile
        private var instance: UsersRepository? = null
        fun getInstance(
            apiService: ApiService,
            newsDao: UsersDao,
            appExecutors: AppExecutors
        ): UsersRepository =
            instance ?: synchronized(this) {
                instance ?: UsersRepository(apiService, newsDao, appExecutors)
            }.also { instance = it }

        private const val TAG = "UsersRepository"
    }
}