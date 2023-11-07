package com.example.submission1storyapp.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.submission1storyapp.data.database.Entities
import com.example.submission1storyapp.data.database.StoryDatabase
import com.example.submission1storyapp.data.pref.UserModel
import com.example.submission1storyapp.data.pref.UserPreference
import com.example.submission1storyapp.data.retrofit.ApiService
import com.example.submission1storyapp.setting.RemoteMediaStory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class UserRepository private constructor(
    private val userPreference: UserPreference,

    private val Database: StoryDatabase,

    private val Apiservice: ApiService,
) {
    fun getQuote(): LiveData<PagingData<Entities>> {
        userPreference.getSession()
        val user = runBlocking { userPreference.getSession().first() }
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 9
            ),
            remoteMediator = RemoteMediaStory(user.token, Database, Apiservice),
            pagingSourceFactory = {
                Database.storyDao().getListStoryPaging()
            }
        ).liveData
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            Database: StoryDatabase,
            Apiservice: ApiService,
            token: String,
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, Database, Apiservice)
            }.also { instance = it }
    }
}