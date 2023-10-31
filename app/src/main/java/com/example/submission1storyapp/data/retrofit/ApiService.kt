package com.example.submission1storyapp.data.retrofit

import com.example.submission1storyapp.data.response.LoginResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("login")
     fun Login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    fun Signup(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<LoginResponse>

//    @GET("Story")
//    fun getStory(@Header("Authorization")token : String
//    ): Call<List<Story>>
}