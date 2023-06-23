package com.example.kotlinflow.repository

import com.example.kotlinflow.dataclass.CommentModel
import com.example.kotlinflow.network.ApiService
import com.example.kotlinflow.network.CommentApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class CommentsRepository(private val apiService: ApiService) {
    suspend fun getComment(id: Int): Flow<CommentApiState<CommentModel>> {
        return flow {
            val comment = apiService.getComments(id)
            emit(CommentApiState.success(comment))
        }.flowOn(Dispatchers.IO)
    }
}