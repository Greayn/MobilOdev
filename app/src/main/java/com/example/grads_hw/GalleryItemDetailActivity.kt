package com.example.grads_hw

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.grads_hw.models.GalleryItem
import com.example.grads_hw.databinding.ActivityGalleryItemDetailBinding
import com.example.grads_hw.managers.AuthenticationManager
import com.example.grads_hw.managers.DbManager
import com.example.grads_hw.util.openImage
import com.example.grads_hw.util.openVideo
import com.example.grads_hw.util.snackbar
import com.example.grads_hw.util.toast
import kotlinx.coroutines.launch


class GalleryItemDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGalleryItemDetailBinding
    private val authenticator = AuthenticationManager()
    private val dbManager = DbManager()
    private val progressDialog by lazy {
        ProgressDialog(this).apply {
            setCancelable(false)
            setCanceledOnTouchOutside(false)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryItemDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val galleryItem = intent.getParcelableExtra<GalleryItem>("galleryItem")!!

        supportActionBar?.title = galleryItem.title
        Glide.with(this).load(galleryItem.mediaUrl).into(binding.imageView)
        binding.imageViewPlay.isVisible = galleryItem.isVideo
        binding.textViewAuthor.text =
            "${galleryItem.author.firstName} ${galleryItem.author.lastName}"

        binding.buttonDeleteGalleryItem.isVisible =
            galleryItem.author.id == authenticator.getCurrentUser()!!.uid

        binding.imageView.setOnClickListener {
            if(galleryItem.isVideo) {
                openVideo(galleryItem.mediaUrl)
            } else {
                openImage(galleryItem.mediaUrl)
            }
        }

        binding.buttonDeleteGalleryItem.setOnClickListener {
            onClickDeleteGalleryItem(galleryItem)
        }
    }

    private fun onClickDeleteGalleryItem(galleryItem: GalleryItem) {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Silme Onayı")
            .setMessage("${galleryItem.title} adlı galeri öğesini silmek istediğinize emin misiniz?")
            .setPositiveButton("Evet") { _, _ ->
                deleteGalleryItem(galleryItem)
            }
            .setNegativeButton("İptal", null)
            .create()

        dialog.show()
    }

    private fun deleteGalleryItem(galleryItem: GalleryItem) {
        lifecycleScope.launch {
            progressDialog.setMessage("Galeri öğesi siliniyor...")
            progressDialog.show()
            try {
                dbManager.deleteGalleryItem(galleryItem)
                toast("Galeri öğesi silindi")
                setResult(RESULT_OK)
                finish()
            } catch (e: Exception) {
                snackbar(e.message.toString(), isError = true)
            } finally {
                progressDialog.dismiss()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}