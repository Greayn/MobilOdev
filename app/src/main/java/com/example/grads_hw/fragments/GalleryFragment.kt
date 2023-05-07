package com.example.grads_hw.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.grads_hw.AddGalleryItemActivity
import com.example.grads_hw.GalleryItemDetailActivity
import com.example.grads_hw.adapter.GalleryItemAdapter
import com.example.grads_hw.models.GalleryItem
import com.example.grads_hw.databinding.FragmentGalleryBinding
import com.example.grads_hw.managers.DbManager
import com.example.grads_hw.util.snackbar
import kotlinx.coroutines.launch

class GalleryFragment : Fragment() {

    private lateinit var binding: FragmentGalleryBinding
    private val dbManager = DbManager()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchGalleryItems()

        binding.fabAddGalleryItem.setOnClickListener {
            val intent = Intent(requireContext(), AddGalleryItemActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_ADD_GALLERY_ITEM)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode in arrayOf(REQUEST_CODE_ADD_GALLERY_ITEM, REQUEST_CODE_GALLERY_ITEM_DETAIL)
            && resultCode == RESULT_OK) {
            fetchGalleryItems()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun fetchGalleryItems() {
        lifecycleScope.launch {
            binding.progressBar.show()
            binding.recyclerView.isVisible = false
            try {
                val galleryItems = dbManager.getAllGalleryItems()
                listGalleryItems(galleryItems)
            } catch (e: Exception) {
                if (isAdded) {
                    requireActivity().snackbar(e.message.toString(), isError = true)
                }
            }
            binding.progressBar.hide()
            binding.recyclerView.isVisible = true
        }
    }

    private fun listGalleryItems(galleryItems: List<GalleryItem>) {
        if (galleryItems.isEmpty()) {
            binding.textViewEmptyWarning.isVisible = true
            return
        }
        val adapter = GalleryItemAdapter(galleryItems) {
            val intent = Intent(requireContext(), GalleryItemDetailActivity::class.java)
            intent.putExtra("galleryItem", it)
            startActivityForResult(intent, REQUEST_CODE_GALLERY_ITEM_DETAIL)
        }
        binding.recyclerView.adapter = adapter
    }

    companion object {
        private const val REQUEST_CODE_ADD_GALLERY_ITEM = 1
        private const val REQUEST_CODE_GALLERY_ITEM_DETAIL = 2
    }

}