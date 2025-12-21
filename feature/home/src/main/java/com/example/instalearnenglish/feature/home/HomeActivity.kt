package com.example.instalearnenglish.feature.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.instalearnenglish.feature.home.databinding.FeatureHomeMainBinding
import com.example.instalearnenglish.feature.home.profile.ProfileActivity
import com.example.instalearnenglish.feature.home.tools.DictionaryActivity
import com.example.instalearnenglish.feature.home.tools.FlashcardsActivity

class HomeActivity : AppCompatActivity() {

    private var _binding: FeatureHomeMainBinding? = null
    private val binding get() = _binding!!
    
    private val JOURNEY_PREFS = "GoGlobalJourneyPrefs"
    private val KEY_CURRENT_LEVEL = "current_level"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            _binding = FeatureHomeMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            setupUserHeader()
            
            // ÉP TRẠNG THÁI: Hoàn thành tất cả (level = 6) để mở khóa toàn bộ map
            val currentLevel = 6
            
            updateMapStatus(currentLevel)
            setupNavigation()
            
        } catch (e: Exception) {
            Log.e("HomeActivity", "Lỗi nạp giao diện: ${e.message}")
            Toast.makeText(this, "Lỗi nạp giao diện, vui lòng thử lại!", Toast.LENGTH_LONG).show()
        }
    }

    private fun setupUserHeader() {
        try {
            binding.tvGreeting.text = "Hi, Traveler!"
            binding.tvPassportLevel.text = "Level: Master Explorer"
            binding.progressBarJourney.progress = 100 // Full tiến trình
        } catch (e: Exception) {
            Log.e("HomeActivity", "Lỗi Header: ${e.message}")
        }
    }

    private fun updateMapStatus(level: Int) {
        try {
            val stations = listOf(
                binding.btnStation1,
                binding.btnStation2,
                binding.btnStation3,
                binding.btnStation4,
                binding.btnStation5
            )

            for (i in stations.indices) {
                val stationId = i + 1
                val button = stations[i]

                // Vì level = 6 nên tất cả stationId (1-5) đều < level -> Luôn mở khóa
                when {
                    stationId < level -> {
                        button.setBackgroundColor(android.graphics.Color.parseColor("#4CAF50"))
                        button.setIconResource(android.R.drawable.ic_menu_edit)
                        button.isEnabled = true
                        button.setOnClickListener { openLesson(stationId) }
                    }
                    stationId == level -> {
                        button.setBackgroundColor(android.graphics.Color.parseColor("#9C27B0"))
                        button.setIconResource(android.R.drawable.ic_menu_send)
                        button.isEnabled = true
                        button.scaleX = 1.1f
                        button.scaleY = 1.1f
                        button.setOnClickListener { openLesson(stationId) }
                    }
                    else -> {
                        button.setBackgroundColor(android.graphics.Color.parseColor("#9E9E9E"))
                        button.setIconResource(android.R.drawable.ic_lock_idle_lock)
                        button.isEnabled = false
                        button.alpha = 0.5f
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("HomeActivity", "Lỗi Bản đồ: ${e.message}")
        }
    }

    private fun openLesson(level: Int) {
        try {
            val intent = Intent()
            // Sửa lại class name chính xác để tránh crash ActivityNotFound
            val className = when (level) {
                1 -> "com.example.instalearnenglish.feature.station1.Station1Activity"
                2, 3 -> "com.example.instalearnenglish.feature.station23.Station23Activity"
                4, 5 -> "com.example.instalearnenglish.feature.station45.Station45Activity"
                else -> return
            }
            
            intent.setClassName(this.packageName, className)
            intent.putExtra("LEVEL", level)
            // Thêm flag để quản lý stack tốt hơn
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(this, "Trạm $level chưa được đăng ký trong Manifest!", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("HomeActivity", "Lỗi Intent: ${e.message}")
            Toast.makeText(this, "Không thể mở bài học: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupNavigation() {
        try {
            binding.bottomNavigation.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_home -> true
                    R.id.nav_dictionary -> {
                        startActivity(Intent(this, DictionaryActivity::class.java))
                        true
                    }
                    R.id.nav_flashcards -> {
                        startActivity(Intent(this, FlashcardsActivity::class.java))
                        true
                    }
                    R.id.nav_profile -> {
                        startActivity(Intent(this, ProfileActivity::class.java))
                        true
                    }
                    else -> false
                }
            }
        } catch (e: Exception) {
            Log.e("HomeActivity", "Lỗi Điều hướng: ${e.message}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
