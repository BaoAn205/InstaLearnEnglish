package com.example.instalearnenglish.feature.home.tools

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.instalearnenglish.feature.home.HomeActivity
import com.example.instalearnenglish.feature.home.R
import com.example.instalearnenglish.feature.home.databinding.FeatureHomeDictionaryBinding

class DictionaryActivity : AppCompatActivity() {

    private lateinit var binding: FeatureHomeDictionaryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            // SỬA: Dùng đúng Binding của layout đã đổi tên
            binding = FeatureHomeDictionaryBinding.inflate(layoutInflater)
            setContentView(binding.root)

            setupSearch()
            setupBottomNavigation()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Không thể khởi tạo giao diện Từ điển", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setupSearch() {
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = binding.etSearch.text.toString().trim()
                if (query.isNotEmpty()) {
                    Toast.makeText(this, "Đang tra: $query", Toast.LENGTH_SHORT).show()
                }
                true
            } else false
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.selectedItemId = R.id.nav_dictionary
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, HomeActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                    })
                    finish()
                    true
                }
                R.id.nav_dictionary -> true
                R.id.nav_flashcards -> {
                    startActivity(Intent(this, FlashcardsActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, com.example.instalearnenglish.feature.home.profile.ProfileActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }
}
