package com.example.grads_hw.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.grads_hw.AddAnnouncementActivity
import com.example.grads_hw.AnnouncementDetailActivity
import com.example.grads_hw.adapter.AnnouncementAdapter
import com.example.grads_hw.models.Announcement
import com.example.grads_hw.databinding.FragmentAnnouncementsBinding
import com.example.grads_hw.managers.DbManager
import com.example.grads_hw.util.snackbar
import kotlinx.coroutines.launch

class AnnouncementsFragment : Fragment() {

    private lateinit var binding: FragmentAnnouncementsBinding
    private val dbManager = DbManager()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAnnouncementsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchAnnouncements()

        binding.fabAddAnnouncement.setOnClickListener {
            val intent = Intent(requireContext(), AddAnnouncementActivity::class.java)
            startActivity(intent)
        }
    }

    private fun fetchAnnouncements() {
        lifecycleScope.launch {
            binding.progressBar.show()
            try {
                val announcements = dbManager.getAllAnnouncements()
                listAnnouncements(announcements)
            } catch (e: Exception) {
                if (isAdded) {
                    requireActivity().snackbar(e.message.toString(), isError = true)
                }
            }
            binding.progressBar.hide()
        }
    }

    private fun listAnnouncements(announcements: List<Announcement>) {
        if (announcements.isEmpty()) {
            binding.textViewEmptyWarning.isVisible = true
            return
        }
        val adapter = AnnouncementAdapter(announcements) {
            val intent = Intent(requireContext(), AnnouncementDetailActivity::class.java)
            intent.putExtra("announcement", it)
            startActivity(intent)
        }
        binding.recyclerView.adapter = adapter
    }

}