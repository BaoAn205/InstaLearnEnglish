package com.example.instalearnenglish;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    // Constants for SharedPreferences
    private static final String JOURNEY_PREFS = "GoGlobalJourneyPrefs";
    private static final String KEY_CURRENT_LEVEL = "current_level";

    // UI Components for The Journey Map
    private ImageButton station1, station2, station3, station4, station5;
    private TextView tvStation1Title, tvStation2Title, tvStation3Title, tvStation4Title, tvStation5Title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // --- Window Insets for Edge-to-Edge --- //
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.top_bar), (v, insets) -> {
             Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
             v.setPadding(v.getPaddingLeft(), systemBars.top, v.getPaddingRight(), v.getPaddingBottom());
             return insets;
        });

        // --- Initialize Views --- //
        initializeJourneyViews();

        // --- Setup Listeners --- //
        setupStationListeners();
        setupBottomNavigation();

        // --- Initial UI State --- //
        updateJourneyUI(); // Update stations based on user progress
    }

    private void initializeJourneyViews() {
        station1 = findViewById(R.id.station1);
        station2 = findViewById(R.id.station2);
        station3 = findViewById(R.id.station3);
        station4 = findViewById(R.id.station4);
        station5 = findViewById(R.id.station5);
        tvStation1Title = findViewById(R.id.tv_station1_title);
        tvStation2Title = findViewById(R.id.tv_station2_title);
        tvStation3Title = findViewById(R.id.tv_station3_title);
        tvStation4Title = findViewById(R.id.tv_station4_title);
        tvStation5Title = findViewById(R.id.tv_station5_title);
    }

    private void setupStationListeners() {
        if (station1 != null) {
            station1.setOnClickListener(v -> {
                if (v.isEnabled()) {
                    Intent intent = new Intent(MainActivity.this, LessonActivity.class);
                    intent.putExtra("LEVEL", 1);
                    startActivity(intent);
                }
            });
        }
        if (station2 != null) {
            station2.setOnClickListener(v -> {
                 if (v.isEnabled()) {
                    Intent intent = new Intent(MainActivity.this, LessonActivity.class);
                    intent.putExtra("LEVEL", 2);
                    startActivity(intent);
                 }
            });
        }
        if (station3 != null) {
            station3.setOnClickListener(v -> {
                 if (v.isEnabled()) {
                    Intent intent = new Intent(MainActivity.this, LessonActivity.class);
                    intent.putExtra("LEVEL", 3);
                    startActivity(intent);
                 }
            });
        }
        if (station4 != null) {
            station4.setOnClickListener(v -> {
                 if (v.isEnabled()) {
                    Intent intent = new Intent(MainActivity.this, LessonActivity.class);
                    intent.putExtra("LEVEL", 4);
                    startActivity(intent);
                 }
            });
        }
        if (station5 != null) {
            station5.setOnClickListener(v -> {
                 if (v.isEnabled()) {
                    Intent intent = new Intent(MainActivity.this, LessonActivity.class);
                    intent.putExtra("LEVEL", 5);
                    startActivity(intent);
                 }
            });
        }
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
            bottomNavigationView.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    return true;
                } else if (itemId == R.id.nav_dictionary) {
                    startActivity(new Intent(this, DictionaryActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    startActivity(new Intent(this, ProfileActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                return false;
            });
        }
    }

    private void updateJourneyUI() {
        SharedPreferences prefs = getSharedPreferences(JOURNEY_PREFS, MODE_PRIVATE);
        int currentLevel = prefs.getInt(KEY_CURRENT_LEVEL, 1); // Default to level 1

        // Station 1
        station1.setEnabled(true);
        tvStation1Title.setTextColor(Color.parseColor("#FFFFFF"));

        // Station 2
        station2.setEnabled(true);
        tvStation2Title.setTextColor(Color.parseColor("#FFFFFF"));

        // Station 3
        station3.setEnabled(true);
        tvStation3Title.setTextColor(Color.parseColor("#FFFFFF"));

        // Station 4
        station4.setEnabled(true);
        tvStation4Title.setTextColor(Color.parseColor("#FFFFFF"));

        // Station 5
        station5.setEnabled(true);
        tvStation5Title.setTextColor(Color.parseColor("#FFFFFF"));
    }
}
