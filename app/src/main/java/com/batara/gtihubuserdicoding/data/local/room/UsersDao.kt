package com.batara.gtihubuserdicoding.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.batara.gtihubuserdicoding.data.local.entity.UsersEntity

@Dao
interface UsersDao {
    @Query("SELECT * FROM users ORDER BY id ASC")
    fun getUsers(): LiveData<List<UsersEntity>>

    @Query("SELECT * FROM users where isBookmarked = 1")
    fun getBookmarkedUsers(): LiveData<List<UsersEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUsers(users: List<UsersEntity>)

    @Update
    suspend fun updateUsers(users: UsersEntity)

    @Query("DELETE FROM users WHERE isBookmarked = 0")
    suspend fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM users WHERE username = :username AND isBookmarked = 1)")
    suspend fun isUsersBookmarked(username: String): Boolean

    @Query("SELECT * FROM users WHERE username = :username ")
    suspend fun getDetailUser(username: String): UsersEntity

    @Query("SELECT * FROM users WHERE username = :username ")
    suspend fun getDetailUserSearch(username: String): UsersEntity

    @Query("SELECT * FROM users WHERE username LIKE '' || :username  || '%' ORDER BY id ASC")
    fun searchUser(username: String?): LiveData<List<UsersEntity>>
}