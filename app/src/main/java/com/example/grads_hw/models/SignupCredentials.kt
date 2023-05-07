package com.example.grads_hw.models

import android.net.Uri
import com.google.firebase.firestore.PropertyName

data class SignupCredentials(
    val imageUri: Uri?,
    val firstName: String,
    val lastName: String,
    val entYear: String,
    val gradYear: String,
    val email: String,
    val password: String,
)
