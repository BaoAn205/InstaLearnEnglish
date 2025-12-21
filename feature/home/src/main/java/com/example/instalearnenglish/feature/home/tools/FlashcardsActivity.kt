package com.example.instalearnenglish.feature.home.tools

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.instalearnenglish.feature.home.HomeActivity
import com.example.instalearnenglish.feature.home.R
import com.example.instalearnenglish.feature.home.databinding.FeatureHomeFlashcardsBinding
import com.example.instalearnenglish.feature.home.profile.ProfileActivity

class FlashcardsActivity : AppCompatActivity() {

    private lateinit var binding: FeatureHomeFlashcardsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            // SỬA: Dùng đúng Binding mới cho Flashcards
            binding = FeatureHomeFlashcardsBinding.inflate(layoutInflater)
            setContentView(binding.root)

            setupBottomNavigation()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Lỗi nạp Thẻ từ", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.selectedItemId = R.id.nav_flashcards
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
                R.id.nav_flashcards -> true
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }
}
