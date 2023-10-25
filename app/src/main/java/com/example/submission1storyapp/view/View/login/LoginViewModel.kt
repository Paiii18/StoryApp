package com.example.submission1storyapp.view.View.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.submission1storyapp.view.data.UserRepository
import com.example.submission1storyapp.view.data.pref.UserModel
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}