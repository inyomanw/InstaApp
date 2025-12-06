package com.inyomanw.instaapp.presentation.ui.createpost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inyomanw.instaapp.domain.common.UiState
import com.inyomanw.instaapp.domain.model.PostDomain
import com.inyomanw.instaapp.domain.repository.AuthRepository
import com.inyomanw.instaapp.domain.usecase.CreatePostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val createPostUseCase: CreatePostUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _createPostState = MutableStateFlow<UiState<PostDomain>>(UiState.Idle)
    val createPostState: StateFlow<UiState<PostDomain>> = _createPostState.asStateFlow()

    fun createPost(imageBytes: ByteArray, caption: String) {
        viewModelScope.launch {
            val currentUser = authRepository.getCurrentUser()

            if (currentUser == null) {
                _createPostState.value = UiState.Error("User not logged in")
                return@launch
            }

            createPostUseCase(
                userId = currentUser.uid,
                userName = currentUser.displayName,
                userPhotoUrl = currentUser.photoUrl,
                imageBytes = imageBytes,
                caption = caption
            ).collect { result ->
                _createPostState.value = result
            }
        }
    }

    fun resetState() {
        _createPostState.value = UiState.Idle
    }
}