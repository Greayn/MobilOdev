package com.example.grads_hw.util


import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.example.grads_hw.R



fun Activity.snackbar(message: String, isError: Boolean = false) {
    val snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
    if (isError) {
        snackbar.setBackgroundTint(resources.getColor(R.color.black))
    }
    snackbar.show()
}

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}