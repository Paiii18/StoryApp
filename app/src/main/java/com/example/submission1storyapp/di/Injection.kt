package com.example.submission1storyapp.di

import android.content.Context
import com.example.submission1storyapp.data.UserRepository
import com.example.submission1storyapp.data.pref.UserPreference
import com.example.submission1storyapp.data.pref.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }
}