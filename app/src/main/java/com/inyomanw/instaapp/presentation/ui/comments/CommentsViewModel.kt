package com.inyomanw.instaapp.presentation.ui.comments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inyomanw.instaapp.domain.common.UiState
import com.inyomanw.instaapp.domain.model.CommentDomain
import com.inyomanw.instaapp.domain.repository.AuthRepository
import com.inyomanw.instaapp.domain.usecase.AddCommentUseCase
import com.inyomanw.instaapp.domain.usecase.DeleteCommentUseCase
import com.inyomanw.instaapp.domain.usecase.GetCommentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentsViewModel @Inject constructor(
    private val getCommentsUseCase: GetCommentsUseCase,
    private val addCommentUseCase: AddCommentUseCase,
    private val deleteCommentUseCase: DeleteCommentUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _commentsState = MutableStateFlow<UiState<List<CommentDomain>>>(UiState.Idle)
    val commentsState: StateFlow<UiState<List<CommentDomain>>> = _commentsState.asStateFlow()

    private val _addCommentState = MutableStateFlow<UiState<CommentDomain>>(UiState.Idle)
    val addCommentState: StateFlow<UiState<CommentDomain>> = _addCommentState.asStateFlow()

    private val _deleteCommentState = MutableStateFlow<UiState<Boolean>>(UiState.Idle)
    val deleteCommentState: StateFlow<UiState<Boolean>> = _deleteCommentState.asStateFlow()

    fun loadComments(postId: String) {
        viewModelScope.launch {
            getCommentsUseCase(postId).collect { result ->
                _commentsState.value = result
            }
        }
    }

    fun addComment(postId: String, text: String) {
        viewModelScope.launch {
            val currentUser = authRepository.getCurrentUser()

            if (currentUser == null) {
                _addCommentState.value = UiState.Error("User not logged in")
                return@launch
            }

            addCommentUseCase(
                postId = postId,
                userId = currentUser.uid,
                userName = currentUser.displayName,
                userPhotoUrl = currentUser.photoUrl,
                text = text
            ).collect { result ->
                _addCommentState.value = result

                if (result is UiState.Success) {
                    loadComments(postId)
                }
            }
        }
    }

    fun deleteComment(commentId: String, postId: String) {
        viewModelScope.launch {
            val currentUser = authRepository.getCurrentUser()

            if (currentUser == null) {
                _deleteCommentState.value = UiState.Error("User not logged in")
                return@launch
            }

            deleteCommentUseCase(commentId, postId, currentUser.uid).collect { result ->
                _deleteCommentState.value = result

                if (result is UiState.Success) {
                    loadComments(postId)
                }
            }
        }
    }

    fun resetAddCommentState() {
        _addCommentState.value = UiState.Idle
    }
}
