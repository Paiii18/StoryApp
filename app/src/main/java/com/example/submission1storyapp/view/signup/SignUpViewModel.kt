package com.example.submission1storyapp.view.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.submission1storyapp.data.response.RegisterResponse
import com.example.submission1storyapp.data.retrofit.ApiConfig
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isMessage = MutableLiveData<String>()
    val isMessage: LiveData<String> = _isMessage


    fun registerUser(name: String, email: String, password: String) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().Signup(name, email, password)

        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(

                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    val appResponse = response.body()
                    Log.i("SignupViewModel", "${appResponse}")
                    _isMessage.value = appResponse?.message!!

                } else {
                    val str = response.errorBody()!!.string()
                    try {
                        val json = JSONObject(str)

                        _isMessage.value = json.getString("message")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }

                _isLoading.postValue(false)
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Log.e("SignupViewModel", "Gagal daftar: ${t.message}")
                _isLoading.postValue(false)
            }
        })
    }

}
