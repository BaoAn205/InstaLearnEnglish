package com.example.instalearnenglish;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private TextView tabStudySpace;
    private TextView tabNewFeed;
    private View layoutStudySpace;
    private View layoutNewFeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.top_bar), (v, insets) -> {
             Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
             v.setPadding(v.getPaddingLeft(), systemBars.top + v.getPaddingTop(), v.getPaddingRight(), v.getPaddingBottom());
             return insets;
        });

        tabStudySpace = findViewById(R.id.tab_study_space);
        tabNewFeed = findViewById(R.id.tab_new_feed);
        layoutStudySpace = findViewById(R.id.layout_study_space);
        layoutNewFeed = findViewById(R.id.layout_new_feed);

        if (tabStudySpace != null && tabNewFeed != null && layoutStudySpace != null && layoutNewFeed != null) {
            showNewFeed();
            tabStudySpace.setOnClickListener(v -> showStudySpace());
            tabNewFeed.setOnClickListener(v -> showNewFeed());
        }

        // Bottom Navigation Logic
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_home);

            bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int itemId = item.getItemId();
                    if (itemId == R.id.nav_home) {
                        return true;
                    } else if (itemId == R.id.nav_dictionary) {
                        startActivity(new Intent(MainActivity.this, DictionaryActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    } else if (itemId == R.id.nav_flashcards) {
                        startActivity(new Intent(MainActivity.this, FlashcardsActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    } else if (itemId == R.id.nav_profile) {
                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    private void showStudySpace() {
        if (layoutStudySpace != null && layoutNewFeed != null) {
            layoutStudySpace.setVisibility(View.VISIBLE);
            layoutNewFeed.setVisibility(View.GONE);

            tabStudySpace.setTextColor(Color.parseColor("#673AB7")); // Purple
            tabStudySpace.setTypeface(null, Typeface.BOLD);

            tabNewFeed.setTextColor(Color.parseColor("#757575")); // Grey
            tabNewFeed.setTypeface(null, Typeface.NORMAL);
        }
    }

    private void showNewFeed() {
        if (layoutStudySpace != null && layoutNewFeed != null) {
            layoutStudySpace.setVisibility(View.GONE);
            layoutNewFeed.setVisibility(View.VISIBLE);

            tabNewFeed.setTextColor(Color.parseColor("#673AB7")); // Purple
            tabNewFeed.setTypeface(null, Typeface.BOLD);

            tabStudySpace.setTextColor(Color.parseColor("#757575")); // Grey
            tabStudySpace.setTypeface(null, Typeface.NORMAL);
        }
    }
}