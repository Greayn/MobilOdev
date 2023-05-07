package com.example.grads_hw

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.example.grads_hw.models.SignupCredentials
import com.example.grads_hw.models.User
import com.example.grads_hw.databinding.ActivitySignupBinding
import com.example.grads_hw.managers.AuthenticationManager
import com.example.grads_hw.managers.DbManager
import com.example.grads_hw.managers.LocalStorageManager
import com.example.grads_hw.managers.RemoteStorageManager
import com.example.grads_hw.util.getFileName
import com.example.grads_hw.util.snackbar
import com.example.grads_hw.util.toast
import com.example.grads_hw.validator.SignupValidator
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var validator: SignupValidator
    private lateinit var authenticator: AuthenticationManager
    private lateinit var dbManager: DbManager
    private lateinit var localStorageManager: LocalStorageManager
    private lateinit var remoteStorageManager: RemoteStorageManager
    private var imageUri: Uri? = null

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                imageUri = uri
                binding.imageView.setImageURI(uri)
                binding.buttonPickImage.text = "Profil Resmini Değiştir"
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        validator = SignupValidator()
        authenticator = AuthenticationManager()
        dbManager = DbManager()
        localStorageManager = LocalStorageManager(this)
        remoteStorageManager = RemoteStorageManager()

        binding.buttonSignup.setOnClickListener {
            onClickSignup()
        }

        binding.textViewLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.buttonPickImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun onClickSignup() {
        try {
            val firstName = binding.editTextFirstName.text.toString()
            val lastName = binding.editTextLastName.text.toString()
            val entYear = binding.editTextEntranceYear.text.toString()
            val gradYear = binding.editTextGradYear.text.toString()
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            val credentials = SignupCredentials(
                imageUri, firstName, lastName, entYear, gradYear, email, password
            )
            validator.validate(credentials)
            // signup to firebase
            onSignupValidationSuccess(credentials)
        } catch (e: Exception) {
            snackbar(e.message.toString(), isError = true)
        }
    }

    private fun onSignupValidationSuccess(credentials: SignupCredentials) {
        // signup to firebase
        lifecycleScope.launch {
            binding.apply {
                buttonSignup.isEnabled = false
                buttonSignup.text = ""
                progressBar.show()
            }
            try {
                signup(credentials)
            } catch (e: Exception) {
                snackbar(e.message.toString(), isError = true)
            }
            binding.apply {
                buttonSignup.isEnabled = true
                buttonSignup.text = getString(R.string.signup)
                progressBar.hide()
            }
        }
    }

    private suspend fun signup(credentials: SignupCredentials) {
        val firebaseUser = authenticator.signup(credentials.email, credentials.password)
        val imagePath = credentials.imageUri?.let { uri ->
            remoteStorageManager.uploadImage(uri, uri.getFileName(contentResolver)!!)
        }
        val user = User(
            id = firebaseUser.uid,
            firstName = credentials.firstName,
            lastName = credentials.lastName,
            entYear = credentials.entYear.toInt(),
            gradYear = credentials.gradYear.toInt(),
            email = credentials.email,
            imageUrl = imagePath
        )
        dbManager.createUser(user)
        // save user to local storage
        val imageUrl = imagePath?.let {
            Firebase.storage.reference.child(it).downloadUrl.await()
                .toString()
        }
        localStorageManager.saveUser(user.copy(imageUrl = imageUrl))
        toast("Başarıyla kayıt oldunuz.")
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }


}
