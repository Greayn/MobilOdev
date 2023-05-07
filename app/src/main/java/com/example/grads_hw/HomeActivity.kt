package com.example.grads_hw

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.grads_hw.databinding.ActivityHomeBinding
import com.example.grads_hw.fragments.AnnouncementsFragment
import com.example.grads_hw.fragments.GalleryFragment
import com.example.grads_hw.fragments.GradsFragment
import com.example.grads_hw.managers.LocalStorageManager

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var localStorageManager: LocalStorageManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        localStorageManager = LocalStorageManager(this)

        // get user from intent
        val user = localStorageManager.getUser()!!

        // change actionbar title
        // supportActionBar?.title = "> ${user.firstName} ${user.lastName}"

        // configure bottom navigation
        binding.navView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_grads -> {
                    replaceFragment(GradsFragment())
                    true
                }
                R.id.navigation_announcements -> {
                    replaceFragment(AnnouncementsFragment())
                    true
                }
                R.id.navigation_gallery -> {
                    replaceFragment(GalleryFragment())
                    true
                }
                R.id.navigation_profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    intent.putExtra("isEditable", true)
                    intent.putExtra("user", user)
                    startActivity(intent)
                    false
                }
                else -> error("Unknown navigation item")
            }
        }

        // set default fragment
        binding.navView.selectedItemId = R.id.navigation_grads
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            replace(R.id.fragment_container, fragment)
        }
    }


}