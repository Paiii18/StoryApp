package com.example.submission1storyapp.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RemoteKey")
data class Remotekeys(
    @PrimaryKey val id: String,
    val prevKey: Int?,
    val nextKey: Int?
)
