package com.example.submission1storyapp.view.login


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.submission1storyapp.data.repository.UserRepository
import com.example.submission1storyapp.data.pref.UserModel
import com.example.submission1storyapp.data.response.LoginResponse
import com.example.submission1storyapp.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel (private val repository: UserRepository) : ViewModel() {

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading


    private val _isSuccess = MutableLiveData<Boolean>(false)
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _isMessage = MutableLiveData<String>()
    val isMessage: LiveData<String> = _isMessage


    fun signIn(email: String, password: String) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().Login(email, password)

        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(

                call: Call<LoginResponse>,
                response: Response<LoginResponse>,
            ) {
                Log.i("SignupViewModel", "${response.code()}")

                if (response.isSuccessful) {

                    val appResponse = response.body()
                    saveSession(UserModel(token = appResponse?.loginResult?.token!!, isLogin = true))
                    Log.i(
                        "LoginViewModel", "${appResponse?.loginResult?.token!!}"
                    )
                    _isSuccess.value = true
                    _isLoading.value = false
                    _isMessage.value = appResponse.message!!

                } else {

                    val str = response.errorBody()!!.string()
                    try {
                        val json = JSONObject(str)

                        _isMessage.value =
                            json.getString("message")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }

                _isLoading.postValue(false)
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("SignupViewModel", "Gagal daftar: ${t.message}")
                _isLoading.postValue(false)
            }
        })
    }

}