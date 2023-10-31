package com.example.submission1storyapp.di

import android.content.Context
import com.example.submission1storyapp.data.UserRepository
import com.example.submission1storyapp.data.pref.UserPreference
import com.example.submission1storyapp.data.pref.dataStore
import com.example.submission1storyapp.data.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(pref)
    }
}