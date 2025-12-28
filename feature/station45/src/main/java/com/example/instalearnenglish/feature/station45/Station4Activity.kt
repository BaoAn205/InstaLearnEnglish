package com.example.instalearnenglish.feature.station45

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.instalearnenglish.core.ui.adapter.LessonPagerAdapter
import com.example.instalearnenglish.feature.station45.databinding.FeatureStation4MainBinding
import com.google.android.material.tabs.TabLayoutMediator

class Station4Activity : AppCompatActivity() {

    private lateinit var binding: FeatureStation4MainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = FeatureStation4MainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            setupToolbar()
            setupViewPager()
        } catch (e: Exception) {
            Toast.makeText(this, "Error loading Station 4: ${e.message}", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.title = "Station 4: Hotel"
        binding.toolbar.setNavigationOnClickListener { 
            val intent = Intent()
            intent.setClassName(this, "com.example.instalearnenglish.feature.home.HomeActivity")
            startActivity(intent)
            finish()
        }
    }

    private fun setupViewPager() {
        binding.viewPager.adapter = LessonPagerAdapter(this)
        
        val tabTitles = listOf("Tip", "Vocab", "Simulation", "Game")
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }
}