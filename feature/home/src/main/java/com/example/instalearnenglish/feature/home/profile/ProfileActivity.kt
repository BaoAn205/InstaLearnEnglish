package com.example.instalearnenglish.feature.home.profile

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.instalearnenglish.feature.home.HomeActivity
import com.example.instalearnenglish.feature.home.R
import com.example.instalearnenglish.feature.home.databinding.FeatureHomeProfileBinding
import com.example.instalearnenglish.feature.home.tools.DictionaryActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: FeatureHomeProfileBinding
    private val mAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            // SỬA: Dùng đúng Binding mới
            binding = FeatureHomeProfileBinding.inflate(layoutInflater)
            setContentView(binding.root)

            setupProfileData()
            setupBottomNavigation()
            
            binding.btnLogout.setOnClickListener {
                mAuth.signOut()
                Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show()
                val intent = Intent()
                intent.setClassName(this, "com.example.instalearnenglish.feature.home.auth.LoginActivity")
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Lỗi nạp Hồ sơ", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setupProfileData() {
        val user = mAuth.currentUser
        user?.let {
            binding.tvUserEmail.text = it.email
            db.collection("users").document(it.uid).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        binding.tvUserName.text = document.getString("fullName") ?: "Traveler"
                    }
                }
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.selectedItemId = R.id.nav_profile
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, HomeActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                    })
                    finish()
                    true
                }
                R.id.nav_dictionary -> {
                    startActivity(Intent(this, DictionaryActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_flashcards -> {
                    val intent = Intent()
                    intent.setClassName(this, "com.example.instalearnenglish.feature.home.tools.FlashcardsActivity")
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.nav_profile -> true
                else -> false
            }
        }
    }
}
