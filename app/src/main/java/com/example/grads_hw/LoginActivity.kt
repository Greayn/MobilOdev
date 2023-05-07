package com.example.grads_hw

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.grads_hw.models.LoginCredentials
import com.example.grads_hw.databinding.ActivityLoginBinding
import com.example.grads_hw.managers.AuthenticationManager
import com.example.grads_hw.managers.DbManager
import com.example.grads_hw.managers.LocalStorageManager
import com.example.grads_hw.util.snackbar
import com.example.grads_hw.util.toast
import com.example.grads_hw.validator.LoginValidator
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var validator: LoginValidator
    private lateinit var authenticator: AuthenticationManager
    private lateinit var dbManager: DbManager
    private lateinit var localStorageManager: LocalStorageManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        validator = LoginValidator()
        authenticator = AuthenticationManager()
        dbManager = DbManager()
        localStorageManager = LocalStorageManager(this)

        binding.buttonLogin.setOnClickListener {
            onClickButtonLogin()
        }

        binding.textViewSignup.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun onClickButtonLogin() {
        try {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            val credentials = LoginCredentials(email, password)
            validator.validate(credentials)
            // login to firebase
            onLoginValidationSuccess(credentials)
        } catch (e: Exception) {
            // show error
            snackbar(e.message.toString(), isError = true)
        }
    }

    private fun onLoginValidationSuccess(credentials: LoginCredentials) {
        lifecycleScope.launch {
            binding.apply {
                buttonLogin.isEnabled = false
                buttonLogin.text = ""
                progressBar.show()
            }
            try {
                login(credentials)
            } catch (e: Exception) {
                e.printStackTrace()
                snackbar(e.message.toString(), isError = true)
            }
            binding.apply {
                buttonLogin.isEnabled = true
                buttonLogin.text = getString(R.string.login)
                progressBar.hide()
            }
        }
    }

    private suspend fun login(credentials: LoginCredentials) {
        val firebaseUser = authenticator.login(credentials.email, credentials.password)
        val user = dbManager.getUser(firebaseUser.uid)
        // save user to shared preferences
        localStorageManager.saveUser(user)
        toast("Başarıyla giriş yapıldı.")
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}