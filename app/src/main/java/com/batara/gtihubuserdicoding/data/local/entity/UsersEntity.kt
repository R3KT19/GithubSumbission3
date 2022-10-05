package com.batara.gtihubuserdicoding.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
class UsersEntity(
    @field:ColumnInfo(name = "username")
    @field:PrimaryKey
    val username : String,

    @field:ColumnInfo(name = "id")
    val id : Int,

    @field:ColumnInfo(name = "urlImage")
    val urlImage : String,

    @field:ColumnInfo(name = "isBookmarked")
    var isBookmarked : Boolean
)