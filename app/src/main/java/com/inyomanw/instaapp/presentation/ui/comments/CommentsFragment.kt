package com.inyomanw.instaapp.presentation.ui.comments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.inyomanw.instaapp.R
import com.inyomanw.instaapp.databinding.FragmentCommentsBinding
import com.inyomanw.instaapp.domain.common.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CommentsFragment : Fragment() {

    private var _binding: FragmentCommentsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CommentsViewModel by viewModels()
    private val args: CommentsFragmentArgs by navArgs()
    private lateinit var commentsAdapter: CommentsAdapter

    @Inject
    lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupCommentInput()
        setupClickListeners()
        observeStates()

        viewModel.loadComments(args.postId)
    }

    private fun setupRecyclerView() {
        val currentUserId = auth.currentUser?.uid ?: ""
        commentsAdapter = CommentsAdapter(
            currentUserId = currentUserId,
            onDeleteClick = { comment ->
                viewModel.deleteComment(comment.commentId, comment.postId)
            }
        )

        binding.rvComments.apply {
            adapter = commentsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupCommentInput() {
        val currentUser = auth.currentUser
        Glide.with(this)
            .load(currentUser?.photoUrl)
            .placeholder(R.drawable.ic_image)
            .circleCrop()
            .into(binding.ivUserAvatar)

        binding.etComment.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val hasText = !s.isNullOrBlank()
                binding.btnSend.isEnabled = hasText
                binding.btnSend.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        if (hasText) R.color.purple else android.R.color.darker_gray
                    )
                )
            }
        })
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnSend.setOnClickListener {
            val commentText = binding.etComment.text.toString().trim()
            if (commentText.isNotEmpty()) {
                viewModel.addComment(args.postId, commentText)
            }
        }
    }

    private fun observeStates() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.commentsState.collect { state ->
                        when (state) {
                            is UiState.Idle -> {
                                binding.progressBar.isGone
                            }
                            is UiState.Loading -> {
                                binding.progressBar.isVisible
                                binding.tvEmptyState.isGone
                            }
                            is UiState.Success -> {
                                binding.progressBar.isGone
                                commentsAdapter.submitList(state.data)

                                binding.tvEmptyState.isVisible = state.data.isEmpty()
                            }
                            is UiState.Error -> {
                                binding.progressBar.isGone
                                Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

                launch {
                    viewModel.addCommentState.collect { state ->
                        when (state) {
                            is UiState.Success -> {
                                binding.etComment.text?.clear()
                                viewModel.resetAddCommentState()
                            }
                            is UiState.Error -> {
                                Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                            }
                            else -> {}
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
