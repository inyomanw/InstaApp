package com.inyomanw.instaapp.presentation.ui.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.inyomanw.instaapp.R
import com.inyomanw.instaapp.databinding.FragmentFeedBinding
import com.inyomanw.instaapp.domain.common.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FeedFragment : Fragment() {

    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FeedViewModel by viewModels()
    private lateinit var postsAdapter: PostsAdapter

    @Inject
    lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSwipeRefresh()
        setupClickListeners()
        observePostsState()
    }

    private fun setupRecyclerView() {
        postsAdapter = PostsAdapter(
            onLikeClick = { post, position ->
                val currentUserId = auth.currentUser?.uid ?: return@PostsAdapter
                viewModel.toggleLike(post.postId, currentUserId, position)
            },
            onCommentClick = { post ->
                val action = FeedFragmentDirections.actionFeedFragmentToCommentsFragment(post.postId)
                findNavController().navigate(action)
            }
        )

        binding.rvPosts.apply {
            adapter = postsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun setupClickListeners() {
        binding.btnAddPost.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_createPostFragment)
        }
    }

    private fun observePostsState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.postsState.collect { state ->
                    binding.swipeRefresh.isRefreshing = false

                    when (state) {
                        is UiState.Idle -> {
                            binding.progressBar.isGone
                        }
                        is UiState.Loading -> {
                            if (postsAdapter.itemCount == 0) {
                                binding.progressBar.isVisible
                            }
                        }
                        is UiState.Success -> {
                            binding.progressBar.isGone
                            postsAdapter.submitList(state.data)
                        }
                        is UiState.Error -> {
                            binding.progressBar.isGone
                            Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
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