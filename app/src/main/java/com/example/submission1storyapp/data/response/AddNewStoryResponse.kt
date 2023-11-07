package com.example.submission1storyapp.data.response

import com.google.gson.annotations.SerializedName

class AddNewStoryResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)
