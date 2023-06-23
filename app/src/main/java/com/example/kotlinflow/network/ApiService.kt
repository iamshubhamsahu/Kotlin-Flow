package com.example.kotlinflow.network

import com.example.kotlinflow.dataclass.CommentModel
import retrofit2.http.Path
import retrofit2.http.GET


interface ApiService {
    @GET("/comments/{id}")
    suspend fun getComments(@Path("id") id: Int): CommentModel
}

