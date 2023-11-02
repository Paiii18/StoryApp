package com.example.submission1storyapp.view.detailstory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.submission1storyapp.data.UserRepository
import com.example.submission1storyapp.data.pref.UserModel
import com.example.submission1storyapp.data.response.DetailStoryResponse
import com.example.submission1storyapp.data.response.Story
import com.example.submission1storyapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback

class DetailStoryViewModel(private val repository: UserRepository) : ViewModel() {

    private val _getStories = MutableLiveData<Story>()
    val getStories: LiveData<Story> = _getStories
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getStory(token: String, id: String) {

        _isLoading.postValue(true)
        val client = ApiConfig.getApiService().getDetailStory( "Bearer $token",id)

        client.enqueue(object : Callback<DetailStoryResponse> {
            override fun onResponse(
                call: Call<DetailStoryResponse>,
                response: retrofit2.Response<DetailStoryResponse>,
            ) {
                Log.i("AddStoryViewModel", "${response.code()}")

                if (response.isSuccessful) {

                    val appResponse = response.body()?.story
                    val itemsList = appResponse
                    _getStories.postValue(itemsList!!)
                    Log.i(
                        "AddStoryViewModel", " AddStoryViewModel :${appResponse}"
                    )
                    _isLoading.value = false

                } else {

                    _isLoading.value = false
                }

                _isLoading.postValue(false)
            }

            override fun onFailure(call: Call<DetailStoryResponse>, t: Throwable) {
                Log.e("AddStoryViewModel", "Gagal daftar: ${t.message}")
                _isLoading.postValue(false)
            }
        })

    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

}