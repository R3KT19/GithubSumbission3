package com.batara.gtihubuserdicoding.data.remote.retrofit

import com.batara.gtihubuserdicoding.BuildConfig
import com.batara.gtihubuserdicoding.UserDetailResponse
import com.batara.gtihubuserdicoding.UserSearchResponse
import com.batara.gtihubuserdicoding.UsersResponseItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/users")
    @Headers("Authorization: token ${BuildConfig.API_KEY}")
    fun retrieveUser(): Call<List<UsersResponseItem>>

    @GET("/search/users")
    @Headers("Authorization: token ${BuildConfig.API_KEY}")
    fun searchUser(
        @Query("q") username: String
    ): Call<UserSearchResponse>

    @GET("/users/{username}")
    @Headers("Authorization: token ${BuildConfig.API_KEY}")
    fun userDetail(
        @Path("username") username : String
    ): Call<UserDetailResponse>

    @GET("/users/{username}/followers")
    @Headers("Authorization: token ${BuildConfig.API_KEY}")
    fun userFollowersDetail(
        @Path("username") username : String
    ): Call<List<UsersResponseItem>>

    @GET("/users/{username}/following")
    @Headers("Authorization: token ${BuildConfig.API_KEY}")
    fun userFollowingDetail(
        @Path("username") username : String
    ): Call<List<UsersResponseItem>>
}