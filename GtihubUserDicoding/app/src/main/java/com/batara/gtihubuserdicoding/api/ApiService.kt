package com.batara.gtihubuserdicoding.api

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
    @Headers("Authorization: token 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")
    fun retrieveUser(): Call<List<UsersResponseItem>>

    @GET("/search/users")
    @Headers("Authorization: token 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")
    fun searchUser(
        @Query("q") username: String
    ): Call<UserSearchResponse>

    @GET("/users/{username}")
    @Headers("Authorization: token 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")
    fun userDetail(
        @Path("username") username : String
    ): Call<UserDetailResponse>

    @GET("/users/{username}/followers")
    @Headers("Authorization: token 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")
    fun userFollowersDetail(
        @Path("username") username : String
    ): Call<List<UsersResponseItem>>

    @GET("/users/{username}/following")
    @Headers("Authorization: token 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")
    fun userFollowingDetail(
        @Path("username") username : String
    ): Call<List<UsersResponseItem>>
}