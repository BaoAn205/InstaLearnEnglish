package com.example.instalearnenglish.feature.station45

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.instalearnenglish.core.ui.adapter.LessonPagerAdapter
import com.example.instalearnenglish.feature.station45.databinding.FeatureStation45MainBinding
import com.google.android.material.tabs.TabLayoutMediator

class Station45Activity : AppCompatActivity() {

    private lateinit var binding: FeatureStation45MainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = FeatureStation45MainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            val level = intent.getIntExtra("LEVEL", 4)
            setupToolbar(level)
            setupViewPager()
        } catch (e: Exception) {
            Toast.makeText(this, "Error loading Station 4/5: ${e.message}", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setupToolbar(level: Int) {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.title = if (level == 4) "Station 4: Hotel" else "Station 5: Dining & Shopping"
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
