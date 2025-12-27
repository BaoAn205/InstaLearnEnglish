package com.example.instalearnenglish.feature.station1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.instalearnenglish.feature.station1.databinding.FeatureStation1MainBinding;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Arrays;
import java.util.List;

public class Station1Activity extends AppCompatActivity {

    private FeatureStation1MainBinding binding;
    private String lessonId;

    private final ActivityResultLauncher<String> requestPermissionLauncher = 
        registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (!isGranted) {
                Toast.makeText(this, "Microphone permission is required for speech recognition games.", Toast.LENGTH_LONG).show();
            }
        });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            binding = FeatureStation1MainBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            int level = getIntent().getIntExtra("LEVEL", 1);
            lessonId = String.valueOf(level);

            setupToolbar();
            setupViewPager();
            checkAndRequestPermissions();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khởi tạo Trạm 1: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
        }
    }

    private void setupToolbar() {
        binding.btnBack.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClassName(this, "com.example.instalearnenglish.feature.home.HomeActivity");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        binding.btnDictionary.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClassName(this, "com.example.instalearnenglish.feature.home.tools.DictionaryActivity");
            startActivity(intent);
        });
    }

    private void setupViewPager() {
        ST1_LessonPagerAdapter adapter = new ST1_LessonPagerAdapter(this, lessonId);
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setUserInputEnabled(false);

        List<String> tabTitles = Arrays.asList("Tip", "Vocab", "Simulation", "Game");
        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
            tab.setText(tabTitles.get(position));
        }).attach();
    }
}
