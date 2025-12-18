package com.example.instalearnenglish;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    // Constants for SharedPreferences
    private static final String JOURNEY_PREFS = "GoGlobalJourneyPrefs";
    private static final String KEY_CURRENT_LEVEL = "current_level";

    // UI Components for Tabs
    private TextView tabStudySpace;
    private TextView tabNewFeed;
    private View layoutStudySpace;
    private View layoutNewFeed;

    // UI Components for The Journey Map
    private ImageButton station1, station2;
    private TextView tvStation1Title, tvStation2Title;

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
        initializeTabViews();
        initializeJourneyViews();
        
        // --- Setup Listeners --- //
        setupTabListeners();
        setupStationListeners();
        setupBottomNavigation();
        
        // --- Initial UI State --- //
        updateJourneyUI(); // Update stations based on user progress
        showStudySpace();  // Default to showing the Study Space tab
    }

    private void initializeTabViews() {
        tabStudySpace = findViewById(R.id.tab_study_space);
        tabNewFeed = findViewById(R.id.tab_new_feed);
        layoutStudySpace = findViewById(R.id.layout_study_space);
        layoutNewFeed = findViewById(R.id.layout_new_feed);
    }

    private void initializeJourneyViews() {
        station1 = findViewById(R.id.station1);
        station2 = findViewById(R.id.station2);
        tvStation1Title = findViewById(R.id.tv_station1_title);
        tvStation2Title = findViewById(R.id.tv_station2_title);
    }

    private void setupTabListeners() {
        if (tabStudySpace != null && tabNewFeed != null) {
            tabStudySpace.setOnClickListener(v -> showStudySpace());
            tabNewFeed.setOnClickListener(v -> showNewFeed());
        }
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
                } else if (itemId == R.id.nav_flashcards) {
                    startActivity(new Intent(this, FlashcardsActivity.class));
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
        station1.setEnabled(currentLevel >= 1);
        tvStation1Title.setTextColor(station1.isEnabled() ? Color.parseColor("#424242") : Color.parseColor("#BDBDBD"));

        // Station 2
        station2.setEnabled(currentLevel >= 2);
        tvStation2Title.setTextColor(station2.isEnabled() ? Color.parseColor("#424242") : Color.parseColor("#BDBDBD"));

        // TODO: Add logic for more stations as you create them
    }

    private void showStudySpace() {
        if (layoutStudySpace != null && layoutNewFeed != null) {
            layoutStudySpace.setVisibility(View.VISIBLE);
            layoutNewFeed.setVisibility(View.GONE);

            tabStudySpace.setTextColor(Color.parseColor("#673AB7"));
            tabStudySpace.setTypeface(null, Typeface.BOLD);

            tabNewFeed.setTextColor(Color.parseColor("#757575"));
            tabNewFeed.setTypeface(null, Typeface.NORMAL);
        }
    }

    private void showNewFeed() {
        if (layoutStudySpace != null && layoutNewFeed != null) {
            layoutStudySpace.setVisibility(View.GONE);
            layoutNewFeed.setVisibility(View.VISIBLE);

            tabNewFeed.setTextColor(Color.parseColor("#673AB7"));
            tabNewFeed.setTypeface(null, Typeface.BOLD);

            tabStudySpace.setTextColor(Color.parseColor("#757575"));
            tabStudySpace.setTypeface(null, Typeface.NORMAL);
        }
    }
}
