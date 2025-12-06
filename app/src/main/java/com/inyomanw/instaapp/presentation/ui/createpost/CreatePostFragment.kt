package com.inyomanw.instaapp.presentation.ui.createpost

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.inyomanw.instaapp.databinding.FragmentCreatePostBinding
import com.inyomanw.instaapp.domain.common.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

@AndroidEntryPoint
class CreatePostFragment : Fragment() {

    private var _binding: FragmentCreatePostBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CreatePostViewModel by viewModels()
    private var selectedImageUri: Uri? = null
    private var selectedImageBytes: ByteArray? = null

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            selectedImageUri = uri
            binding.ivPreview.setImageURI(uri)
            binding.btnSelectImage.isGone

            try {
                val inputStream = requireContext().contentResolver.openInputStream(uri)
                val outputStream = ByteArrayOutputStream()
                inputStream?.copyTo(outputStream)
                selectedImageBytes = outputStream.toByteArray()
                inputStream?.close()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Failed to load image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        observeCreatePostState()
    }

    private fun setupClickListeners() {
        binding.btnClose.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnSelectImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.ivPreview.setOnClickListener {
            if (selectedImageUri != null) {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
        }

        binding.btnShare.setOnClickListener {
            val caption = binding.etCaption.text.toString().trim()
            val imageBytes = selectedImageBytes

            when {
                imageBytes == null -> {
                    Toast.makeText(requireContext(), "Please select an image", Toast.LENGTH_SHORT).show()
                }
                caption.isEmpty() -> {
                    Toast.makeText(requireContext(), "Please write a caption", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    viewModel.createPost(imageBytes, caption)
                }
            }
        }
    }

    private fun observeCreatePostState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.createPostState.collect { state ->
                    when (state) {
                        is UiState.Idle -> {
                            binding.progressBar.isGone
                            setInputEnabled(true)
                        }
                        is UiState.Loading -> {
                            binding.progressBar.isVisible
                            setInputEnabled(false)
                        }
                        is UiState.Success -> {
                            binding.progressBar.isGone
                            Toast.makeText(requireContext(), "Post created successfully!", Toast.LENGTH_SHORT).show()
                            viewModel.resetState()
                            findNavController().navigateUp()
                        }
                        is UiState.Error -> {
                            binding.progressBar.isGone
                            setInputEnabled(true)
                            Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun setInputEnabled(enabled: Boolean) {
        binding.btnClose.isEnabled = enabled
        binding.btnSelectImage.isEnabled = enabled
        binding.btnShare.isEnabled = enabled
        binding.etCaption.isEnabled = enabled
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
