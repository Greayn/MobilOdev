package com.example.grads_hw

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.grads_hw.models.Announcement
import com.example.grads_hw.databinding.ActivityAnnouncementDetailBinding

class AnnouncementDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnnouncementDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnnouncementDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val announcement = intent.getParcelableExtra<Announcement>("announcement")!!

        supportActionBar?.title = announcement.title
        binding.textViewContent.text = announcement.content
        binding.textViewAuthor.text = "${announcement.author.firstName} ${announcement.author.lastName}"
        Glide.with(this).load(announcement.imageUrl).into(binding.imageView)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}