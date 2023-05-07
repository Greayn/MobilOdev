package com.example.grads_hw.models

import android.net.Uri
import java.util.Date

data class AddAnnouncementData(
    val title: String,
    val description: String,
    val imageUri: Uri?,
    val expirationDate: Date?
)