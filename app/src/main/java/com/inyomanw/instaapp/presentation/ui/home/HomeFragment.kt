package com.inyomanw.instaapp.presentation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.inyomanw.instaapp.R
import com.inyomanw.instaapp.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        displayUserInfo()

        binding.btnLogout.setOnClickListener {
            auth.signOut()
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        }
    }

    private fun displayUserInfo() {
        val user = auth.currentUser
        user?.let {
            val userInfo = """
                Name: ${it.displayName ?: "N/A"}
                Email: ${it.email}
                UID: ${it.uid}
            """.trimIndent()
            binding.tvUserInfo.text = userInfo
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}