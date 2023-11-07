package com.example.submission1storyapp.view.Map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.submission1storyapp.data.repository.UserRepository
import com.example.submission1storyapp.data.pref.UserModel
import com.example.submission1storyapp.data.response.ListStoryItem
import com.example.submission1storyapp.data.response.StoryResponse
import com.example.submission1storyapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback

class MapsViewModel(private val repository: UserRepository) : ViewModel() {

    private val _listStories = MutableLiveData<List<ListStoryItem>>()
    val listStories: LiveData<List<ListStoryItem>> = _listStories
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    fun fetchStoryLocation(token: String) {
        _isLoading.postValue(true)
        val client = ApiConfig.getApiService().getStoryWithLocation("Bearer $token")

        client.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(
                call: Call<StoryResponse>,
                response: retrofit2.Response<StoryResponse>,
            ) {

                if (response.isSuccessful) {

                    val locResponse = response.body()
                    val itemsList = locResponse?.listStory ?: emptyList()
                    _listStories.postValue(itemsList as List<ListStoryItem>?)

                    _isLoading.value = false

                } else {

                    _isLoading.value = false
                }

                _isLoading.postValue(false)
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                _isLoading.postValue(false)
            }
        })

    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}