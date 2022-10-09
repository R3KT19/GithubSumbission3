package com.batara.gtihubuserdicoding

import android.util.Log
import androidx.lifecycle.*
import com.batara.gtihubuserdicoding.data.local.entity.UsersEntity
import com.batara.gtihubuserdicoding.data.remote.retrofit.ApiConfig
import com.batara.gtihubuserdicoding.data.repository.Result
import com.batara.gtihubuserdicoding.data.repository.UsersRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsersViewModel (private val usersRepository: UsersRepository) : ViewModel() {
    private val _listUser = MutableLiveData<List<UsersResponseItem>>()
    val listUser : LiveData<List<UsersResponseItem>> = _listUser

    private val _dtlUser = MutableLiveData<UserDetailResponse>()
    val dtlUser : LiveData<UserDetailResponse> = _dtlUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _list = MutableLiveData<Result<List<UsersEntity>>>()
    val list : LiveData<Result<List<UsersEntity>>> = _list

    fun getUsers() = usersRepository.getUsers()

    fun getBookmarkedUsers() = usersRepository.getBookmarkedUsers()

    fun getSearchUsers(username : String) = usersRepository.getSearchUser(username)


    fun getDetailUsers(username: String) {

        _isLoading.value = true
        val client = ApiConfig.getApiService().userDetail(username)
        client.enqueue(object : Callback<UserDetailResponse> {
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    _dtlUser.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                _isLoading.value = false
                Result.Error(t.message.toString())
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    fun getFollowersList(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().userFollowersDetail(username)
        client.enqueue(object : Callback<List<UsersResponseItem>>{
            override fun onResponse(
                call: Call<List<UsersResponseItem>>,
                response: Response<List<UsersResponseItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listUser.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<UsersResponseItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    fun getFollowingList(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().userFollowingDetail(username)
        client.enqueue(object : Callback<List<UsersResponseItem>>{
            override fun onResponse(
                call: Call<List<UsersResponseItem>>,
                response: Response<List<UsersResponseItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listUser.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<UsersResponseItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    fun saveUsers(users : UsersEntity) {
        usersRepository.setUsersBookmark(users, true)
    }

    fun deleteUsers(users: UsersEntity){
        usersRepository.setUsersBookmark(users, false)
    }

    fun getDetailUsersEntity(username: String) : UsersEntity {
        return usersRepository.getDetailUsers(username)
    }

    companion object{
        private const val TAG = "ViewModel"
    }

}