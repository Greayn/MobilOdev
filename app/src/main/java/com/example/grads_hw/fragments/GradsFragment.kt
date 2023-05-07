package com.example.grads_hw.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.grads_hw.ProfileActivity
import com.example.grads_hw.adapter.GradsAdapter
import com.example.grads_hw.models.User
import com.example.grads_hw.databinding.FragmentGradsBinding
import com.example.grads_hw.managers.AuthenticationManager
import com.example.grads_hw.managers.DbManager
import com.example.grads_hw.util.snackbar
import kotlinx.coroutines.launch

class GradsFragment : Fragment() {

    private lateinit var binding: FragmentGradsBinding
    private val dbManager = DbManager()
    private val authenticator = AuthenticationManager()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentGradsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchUsers()
    }

    private fun fetchUsers() {
        lifecycleScope.launch {
            binding.progressBar.show()
            try {
                val users = dbManager.getAllUsers(authenticator.getCurrentUser()!!.uid)
                listUsers(users)
            } catch (e: Exception) {
                if (isAdded) {
                    requireActivity().snackbar(e.message.toString(), isError = true)
                }
            }
            binding.progressBar.hide()
        }
    }

    private fun listUsers(items: List<User>) {
        val adapter = GradsAdapter(items) { user ->
            val intent = Intent(requireContext(), ProfileActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
        }
        binding.recyclerView.adapter = adapter
    }

}