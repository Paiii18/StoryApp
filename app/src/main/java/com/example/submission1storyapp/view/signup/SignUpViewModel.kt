package com.example.submission1storyapp.view.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.submission1storyapp.data.response.RegisterResponse
import com.example.submission1storyapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

//    fun registerUser(name: String, email: String, password: String) {
//        _isLoading.value = true
//
//        val client = ApiConfig.getApiService().Signup(name, email, password)
//
//        client.enqueue(object : Callback<RegisterResponse> {
//            override fun onResponse(
//                call: Call<RegisterResponse>,
//                response: Response<RegisterResponse>
//            ) {
//                Log.i("SignupViewModel", "${response.code()}")
//
//                if (response.isSuccessful) {
//                    Log.i("SignupViewModel", "Berhasil")
//
//                    val appResponse = response.body()
//                    if (appResponse != null) {
//                        Log.i("SignupViewModel", "${appResponse}")
//                    } else {
//                        Log.e("SignupViewModel", "Response body is null")
//                    }
//                } else {
//                    Log.e("SignupViewModel", "Gagal daftar: ${response.message()}")
//                }
//
//                _isLoading.postValue(false)
//            }
//
//            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
//                Log.e("SignupViewModel", "Gagal daftar: ${t.message}")
//                _isLoading.postValue(false)
//            }
//        })
//    }
}
