package com.batara.gtihubuserdicoding

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.batara.gtihubuserdicoding.api.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val _listUser = MutableLiveData<List<UsersResponseItem>>()
    val listUser : LiveData<List<UsersResponseItem>> = _listUser

    private val _dtlUser = MutableLiveData<UserDetailResponse>()
    val dtlUser : LiveData<UserDetailResponse> = _dtlUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    fun getUser(){
        _isLoading.value = true
        val client = ApiConfig.getApiService().retrieveUser()
        client.enqueue(object : Callback<List<UsersResponseItem>> {
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

    fun getSearch(username:String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().searchUser(username)
        client.enqueue(object : Callback<UserSearchResponse> {
            override fun onResponse(
                call: Call<UserSearchResponse>,
                response: Response<UserSearchResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listUser.value = response.body()?.items
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserSearchResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getDetailUser(username: String){
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

    companion object{
        private const val TAG = "MainViewModel"
    }
}