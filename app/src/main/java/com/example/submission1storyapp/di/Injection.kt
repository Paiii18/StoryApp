package com.example.submission1storyapp.di

import android.content.Context
import com.example.submission1storyapp.data.database.StoryDatabase
import com.example.submission1storyapp.data.repository.UserRepository
import com.example.submission1storyapp.data.pref.UserPreference
import com.example.submission1storyapp.data.pref.dataStore
import com.example.submission1storyapp.data.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val database = StoryDatabase.getDatabase(context)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(pref, database, apiService, user.token)
    }
}