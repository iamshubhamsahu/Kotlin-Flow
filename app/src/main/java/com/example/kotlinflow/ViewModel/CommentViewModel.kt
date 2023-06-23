package com.example.kotlinflow.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinflow.dataclass.CommentModel
import com.example.kotlinflow.network.CommentApiState
import com.example.kotlinflow.network.Status
import com.example.kotlinflow.repository.CommentsRepository
import com.example.kotlinflow.utils.AppConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class CommentViewModel : ViewModel() {

    // Create a Repository and pass the api
    // service we created in AppConfig file
    private val repository = CommentsRepository(AppConfig.ApiService())

    val commentState = MutableStateFlow(
        CommentApiState(
            Status.LOADING,
            CommentModel(),
            ""
        )
    )

    init {
        getNewComment(1)
    }

    fun getNewComment(id: Int) {

        commentState.value = CommentApiState.loading()

        viewModelScope.launch {
            repository.getComment(id)
                .catch {
                    commentState.value = CommentApiState.error(it.message.toString())
                }
                .collect {
                    commentState.value = CommentApiState.success(it.data)
                }
        }

    }
}