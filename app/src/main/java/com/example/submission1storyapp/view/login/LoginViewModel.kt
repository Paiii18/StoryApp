package com.example.submission1storyapp.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.submission1storyapp.data.UserRepository
import com.example.submission1storyapp.data.pref.UserModel
import com.example.submission1storyapp.data.response.LoginResponse
import com.example.submission1storyapp.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSuccess = MutableLiveData<Boolean>(false)
    val isSuccess: LiveData<Boolean> = _isSuccess

    fun signIn(email: String, password: String) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().signIn(email, password)

        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>,
            ) {
                log.i("SignUpViewModel", "${response.code()}")

                if (response.isSuccessful) {
                    log.i("SignUpViewModel", "Berhasil")

                    val appResponse = response.body()
                    saveSession(UserModel(token = appResponse?.token!!, isLogin = true))
                    log.i(
                        "SignUpViewModel", "${appResponse}"
                    )
                    _isSuccess.value = true
                    _isLoading.value = false
                } else {

                }

                _isLoading.postValue(false)
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                log.e("SignUpViewModel", "Gagal daftar: ${t.message}")
                _isLoading.postValue(false)
            }
        })
    }
}