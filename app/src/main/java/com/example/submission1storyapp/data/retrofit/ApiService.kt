package com.example.submission1storyapp.data.retrofit

import com.example.submission1storyapp.data.response.AddNewStoryResponse
import com.example.submission1storyapp.data.response.DetailStoryResponse
import com.example.submission1storyapp.data.response.LoginResponse
import com.example.submission1storyapp.data.response.RegisterResponse
import com.example.submission1storyapp.data.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

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
    ): Call<RegisterResponse>

    @Multipart
    @POST("addstory")
    fun postStory(
        @Header("Authorization")token: String,
        @Part file: MultipartBody.Part,
        @Part("Description") description: RequestBody,
    ): Call<AddNewStoryResponse>
    @GET("stories")
    fun getStory(@Header("Authorization")token : String
    ): Call<StoryResponse>
    @GET("stories/{id}")
    fun getDetailStory(@Header("Authorization")token : String,
        @Path("id") id: String): Call<DetailStoryResponse>
}