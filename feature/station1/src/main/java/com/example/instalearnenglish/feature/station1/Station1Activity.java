package com.example.instalearnenglish.feature.station1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

// Import the R class for this module
import com.example.instalearnenglish.feature.station1.R;
import com.example.instalearnenglish.feature.station1.databinding.FeatureStation1MainBinding;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Arrays;
import java.util.List;

public class Station1Activity extends AppCompatActivity {

    private FeatureStation1MainBinding binding;
    private String lessonId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            binding = FeatureStation1MainBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            // Get level and determine lessonId from Intent
            int level = getIntent().getIntExtra("LEVEL", 1);
            lessonId = String.valueOf(level); // The lessonId is the level itself

            setupToolbar();
            setupViewPager();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khởi tạo Trạm 1: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> {
            // Navigate back to HomeActivity
            Intent intent = new Intent();
            intent.setClassName(this, "com.example.instalearnenglish.feature.home.HomeActivity");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void setupViewPager() {
        // Use ST1_LessonPagerAdapter and pass the lessonId
        ST1_LessonPagerAdapter adapter = new ST1_LessonPagerAdapter(this, lessonId);
        binding.viewPager.setAdapter(adapter);

        List<String> tabTitles = Arrays.asList("Tip", "Vocab", "Simulation", "Game");
        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
            tab.setText(tabTitles.get(position));
        }).attach();
    }
}
