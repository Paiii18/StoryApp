package com.example.submission1storyapp.view.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.submission1storyapp.data.UserRepository
import com.example.submission1storyapp.data.pref.UserModel
import com.example.submission1storyapp.data.response.ListStoryItem
import com.example.submission1storyapp.data.response.StoryResponse
import com.example.submission1storyapp.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback

class MainViewModel(private val repository: UserRepository) : ViewModel() {

    private val _listStories = MutableLiveData<List<ListStoryItem>>()
    val listStories: LiveData<List<ListStoryItem>> = _listStories
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    fun fetchListStories(token: String) {
        _isLoading.postValue(true)
        val client = ApiConfig.getApiService(token).getStory()
        client.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(

                call: Call<StoryResponse>,
                response: retrofit2.Response<StoryResponse>,
            ) {
                Log.i("AddStoryViewModel", "${response.code()}")

                if (response.isSuccessful) {


                    val appResponse = response.body()
                    val itemsList = appResponse?.listStory ?: emptyList()
                    _listStories.postValue(itemsList as List<ListStoryItem>?)

                    _isLoading.value = false

                } else {

                    _isLoading.value = false
                }

                _isLoading.postValue(false)
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                Log.e("AddStoryViewModel", "Gagal daftar: ${t.message}")
                _isLoading.postValue(false)
            }
        })

    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}