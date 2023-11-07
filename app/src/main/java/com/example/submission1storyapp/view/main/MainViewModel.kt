package com.example.submission1storyapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.submission1storyapp.data.database.Entities
import com.example.submission1storyapp.data.repository.UserRepository
import com.example.submission1storyapp.data.pref.UserModel
import kotlinx.coroutines.launch

class MainViewModel(private val reps: UserRepository) : ViewModel() {

    val storyPage: LiveData<PagingData<Entities>> =
        reps.getQuote().cachedIn(viewModelScope)


    fun getSession(): LiveData<UserModel> {
        return reps.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            reps.logout()
        }
    }
}