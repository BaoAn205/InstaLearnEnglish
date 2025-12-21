package com.example.instalearnenglish.feature.station23

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.instalearnenglish.core.ui.adapter.LessonPagerAdapter
import com.example.instalearnenglish.feature.station23.databinding.FeatureStation23MainBinding
import com.google.android.material.tabs.TabLayoutMediator

class Station23Activity : AppCompatActivity() {

    private lateinit var binding: FeatureStation23MainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = FeatureStation23MainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            val level = intent.getIntExtra("LEVEL", 2)
            setupToolbar(level)
            setupViewPager()
        } catch (e: Exception) {
            Toast.makeText(this, "Lỗi khởi tạo Trạm 2/3: ${e.message}", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setupToolbar(level: Int) {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.title = if (level == 2) "Trạm 2: Tại Sân bay" else "Trạm 3: Di chuyển"
        binding.toolbar.setNavigationOnClickListener { 
            val intent = Intent()
            intent.setClassName(this, "com.example.instalearnenglish.feature.home.HomeActivity")
            startActivity(intent)
            finish()
        }
    }

    private fun setupViewPager() {
        // Gán Adapter trước khi dùng Mediator
        binding.viewPager.adapter = LessonPagerAdapter(this)
        
        val tabTitles = listOf("Tip", "Vocab", "Simulation", "Game")
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }
}
