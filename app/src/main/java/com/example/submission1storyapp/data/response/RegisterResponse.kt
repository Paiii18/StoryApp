package com.example.submission1storyapp.data.response

import com.google.gson.annotations.SerializedName

 class RegisterResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
