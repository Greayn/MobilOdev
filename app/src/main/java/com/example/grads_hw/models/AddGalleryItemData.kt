package com.example.grads_hw.models

import android.net.Uri

data class AddGalleryItemData(
    val title: String = "",
    val mediaUri: Uri? = null,
    val thumbnailUri: Uri? = null,
)