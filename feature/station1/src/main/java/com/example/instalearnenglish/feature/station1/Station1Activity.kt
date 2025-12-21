package com.example.instalearnenglish.feature.station1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.instalearnenglish.core.ui.adapter.LessonPagerAdapter
import com.example.instalearnenglish.feature.station1.databinding.FeatureStation1MainBinding
import com.google.android.material.tabs.TabLayoutMediator

class Station1Activity : AppCompatActivity() {

    private lateinit var binding: FeatureStation1MainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = FeatureStation1MainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            setupToolbar()
            setupViewPager()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Lỗi khởi tạo Trạm 1: ${e.message}", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { 
            val intent = Intent()
            intent.setClassName(this, "com.example.instalearnenglish.feature.home.HomeActivity")
            startActivity(intent)
            finish()
        }
    }

    private fun setupViewPager() {
        // SỬA LỖI: Gán Adapter trước khi dùng Mediator
        binding.viewPager.adapter = LessonPagerAdapter(this)
        
        val tabTitles = listOf("Tip", "Vocab", "Simulation", "Game")
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }
}
