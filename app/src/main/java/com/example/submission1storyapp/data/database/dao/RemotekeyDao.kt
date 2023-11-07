package com.example.submission1storyapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.submission1storyapp.data.database.Remotekeys

@Dao
interface RemotekeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<Remotekeys>)

    @Query("SELECT * FROM RemoteKey WHERE id = :id")
    suspend fun getRemoteKeysId(id: String): Remotekeys?

    @Query("DELETE FROM RemoteKey")
    suspend fun deleteRemoteKeys()
}