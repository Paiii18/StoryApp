package com.example.submission1storyapp.view.di

import android.content.Context
import com.example.submission1storyapp.view.data.UserRepository
import com.example.submission1storyapp.view.data.pref.UserPreference
import com.example.submission1storyapp.view.data.pref.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }
}