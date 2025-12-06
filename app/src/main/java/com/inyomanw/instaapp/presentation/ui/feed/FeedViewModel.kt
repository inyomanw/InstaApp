package com.inyomanw.instaapp.presentation.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inyomanw.instaapp.domain.common.UiState
import com.inyomanw.instaapp.domain.model.PostDomain
import com.inyomanw.instaapp.domain.usecase.GetAllPostsUseCase
import com.inyomanw.instaapp.domain.usecase.ToggleLikeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val getAllPostsUseCase: GetAllPostsUseCase,
    private val toggleLikeUseCase: ToggleLikeUseCase
) : ViewModel() {

    private val _postsState = MutableStateFlow<UiState<List<PostDomain>>>(UiState.Idle)
    val postsState: StateFlow<UiState<List<PostDomain>>> = _postsState.asStateFlow()

    private val _posts = mutableListOf<PostDomain>()

    init {
        loadPosts()
    }

    fun loadPosts() {
        viewModelScope.launch {
            getAllPostsUseCase().collect { result ->
                when (result) {
                    is UiState.Success -> {
                        _posts.clear()
                        _posts.addAll(result.data)
                        _postsState.value = result
                    }
                    else -> _postsState.value = result
                }
            }
        }
    }

    fun toggleLike(postId: String, userId: String, position: Int) {
        viewModelScope.launch {
            val post = _posts.getOrNull(position) ?: return@launch
            val isLiked = post.isLikedByCurrentUser

            val updatedPost = post.copy(
                isLikedByCurrentUser = !isLiked,
                likesCount = if (isLiked) post.likesCount - 1 else post.likesCount + 1
            )
            _posts[position] = updatedPost
            _postsState.value = UiState.Success(_posts.toList())

            toggleLikeUseCase(postId, userId, isLiked).collect { result ->
                if (result is UiState.Error) {
                    _posts[position] = post
                    _postsState.value = UiState.Success(_posts.toList())
                }
            }
        }
    }

    fun refresh() {
        loadPosts()
    }
}