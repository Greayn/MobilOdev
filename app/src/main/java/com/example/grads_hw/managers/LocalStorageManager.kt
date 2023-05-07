package com.example.grads_hw.managers


import android.content.Context
import androidx.core.content.edit
import com.example.grads_hw.models.User

class LocalStorageManager(context: Context) {

    private val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

    fun saveUser(user: User) {
        prefs.edit {
            // save user as json string
            putString("user", user.toJson())
        }
    }

    fun getUser(): User? {
        val userJson = prefs.getString("user", null) ?: return null
        return User.fromJson(userJson)
    }

    fun clearUser() {
        prefs.edit {
            remove("user")
        }
    }

}