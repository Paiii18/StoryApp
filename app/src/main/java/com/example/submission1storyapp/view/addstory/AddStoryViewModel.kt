package com.example.submission1storyapp.view.addstory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.submission1storyapp.data.repository.UserRepository
import com.example.submission1storyapp.data.pref.UserModel
import com.example.submission1storyapp.data.response.AddNewStoryResponse
import com.example.submission1storyapp.data.retrofit.ApiConfig
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call

class AddStoryViewModel(private val repository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSuccess = MutableLiveData<Boolean>(false)
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _uploadSuccess = MutableLiveData<Boolean>()
    val uploadSuccess: LiveData<Boolean> = _uploadSuccess


    fun addStory(token: String, file: MultipartBody.Part, description: RequestBody) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().postStory(token = "Bearer $token", file, description)
        Log.i("AddStoryViewModel", "AddStoryViewModel: ${token} ")

        client.enqueue(object : retrofit2.Callback<AddNewStoryResponse> {
            override fun onResponse(
                call: Call<AddNewStoryResponse>,
                response: retrofit2.Response<AddNewStoryResponse>,
            ) {
                Log.i("AddStoryViewModel", "${response.code()}")

                if (response.isSuccessful) {
                    Log.i("AddStoryViewModel", "Berhasil")

                    val appResponse = response.body()
                    Log.i(
                        "AddStoryViewModel", "${appResponse}"
                    )
                    _isSuccess.value = true
                    _uploadSuccess.value = true
                    _isLoading.value = false

                } else {
                    _isLoading.value = false
                }
                _isLoading.postValue(false)
            }

            override fun onFailure(call: Call<AddNewStoryResponse>, t: Throwable) {
                Log.e("AddStoryViewModel", "Gagal daftar: ${t.message}")
                _isLoading.postValue(false)
            }
        })

    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

}