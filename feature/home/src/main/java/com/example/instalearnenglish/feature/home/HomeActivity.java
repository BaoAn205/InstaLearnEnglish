package com.example.instalearnenglish.feature.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.instalearnenglish.feature.home.auth.LoginActivity;
import com.example.instalearnenglish.feature.home.profile.ProfileActivity;
import com.example.instalearnenglish.feature.home.tools.DictionaryActivity;
import com.example.instalearnenglish.feature.home.tools.FlashcardsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot; // <-- Import added here
import com.google.firebase.Timestamp;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private ImageButton station1, station2, station3, station4, station5;
    private TextView tvStation1Title, tvStation2Title, tvStation3Title, tvStation4Title, tvStation5Title;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        initializeJourneyViews();
        setupStationListeners();
        setupBottomNavigation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchUserProgressAndSetupUI();
    }

    private void fetchUserProgressAndSetupUI() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        DocumentReference userDocRef = db.collection("users").document(currentUser.getUid());

        userDocRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot != null && documentSnapshot.exists()) {
                // Check and update day streak
                checkAndUpdateDayStreak(documentSnapshot, userDocRef);

                // Update journey UI
                Long currentLevel = documentSnapshot.getLong("currentLevel");
                updateJourneyUI(currentLevel != null ? currentLevel : 1L);

            } else {
                // If user document doesn't exist, treat as a new user
                updateJourneyUI(1L);
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Could not fetch user progress.", Toast.LENGTH_SHORT).show();
            updateJourneyUI(1L);
        });
    }

    private void checkAndUpdateDayStreak(DocumentSnapshot snapshot, DocumentReference userDocRef) {
        Timestamp lastLoginTimestamp = snapshot.getTimestamp("lastLoginDate");
        Long dayStreak = snapshot.getLong("dayStreak");

        if (lastLoginTimestamp == null || dayStreak == null) {
            // If fields are missing, initialize them
            Map<String, Object> initialStreak = new HashMap<>();
            initialStreak.put("dayStreak", 1L);
            initialStreak.put("lastLoginDate", new Date());
            userDocRef.update(initialStreak);
            return;
        }

        Date lastLoginDate = lastLoginTimestamp.toDate();
        Date today = new Date();

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(lastLoginDate);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(today);

        boolean isSameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);

        if (isSameDay) {
            // User already logged in today, do nothing.
            return;
        }

        cal1.add(Calendar.DAY_OF_YEAR, 1);
        boolean isYesterday = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                              cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);

        Map<String, Object> streakUpdate = new HashMap<>();
        if (isYesterday) {
            // It's a consecutive day, increment streak
            streakUpdate.put("dayStreak", dayStreak + 1);
        } else {
            // The streak is broken, reset to 1
            streakUpdate.put("dayStreak", 1L);
        }
        streakUpdate.put("lastLoginDate", today);
        userDocRef.update(streakUpdate);
    }


    private void updateJourneyUI(long currentLevel) {
        updateStation(station1, tvStation1Title, 1, currentLevel);
        updateStation(station2, tvStation2Title, 2, currentLevel);
        updateStation(station3, tvStation3Title, 3, currentLevel);
        updateStation(station4, tvStation4Title, 4, currentLevel);
        updateStation(station5, tvStation5Title, 5, currentLevel);
    }

    private void updateStation(View stationButton, TextView stationTitle, int stationLevel, long userLevel) {
        if (stationLevel <= userLevel) {
            stationButton.setEnabled(true);
            stationButton.setAlpha(1.0f);
            stationTitle.setAlpha(1.0f);
        } else {
            stationButton.setEnabled(false);
            stationButton.setAlpha(0.5f);
            stationTitle.setAlpha(0.5f);
        }
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
        station1.setOnClickListener(v -> {
            if (v.isEnabled()) openLesson(1);
        });
        station2.setOnClickListener(v -> {
            if (v.isEnabled()) openLesson(2);
        });
        station3.setOnClickListener(v -> {
            if (v.isEnabled()) openLesson(3);
        });
        station4.setOnClickListener(v -> {
            if (v.isEnabled()) openLesson(4);
        });
        station5.setOnClickListener(v -> {
            if (v.isEnabled()) openLesson(5);
        });
    }

    private void openLesson(int level) {
        try {
            Intent intent = new Intent();
            String className;
            switch (level) {
                case 1:
                    className = "com.example.instalearnenglish.feature.station1.Station1Activity";
                    break;
                case 2:
                case 3:
                    className = "com.example.instalearnenglish.feature.station23.Station23Activity";
                    break;
                case 4:
                case 5:
                    className = "com.example.instalearnenglish.feature.station45.Station45Activity";
                    break;
                default:
                    return;
            }
            intent.setClassName(this, className);
            intent.putExtra("LEVEL", level);
            startActivity(intent);
        } catch (Exception e) {
            Log.e("HomeActivity", "Intent Error: " + e.getMessage());
            Toast.makeText(this, "Cannot open lesson: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                    return true;
                } else if (itemId == R.id.nav_flashcards) {
                    startActivity(new Intent(this, FlashcardsActivity.class));
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    startActivity(new Intent(this, ProfileActivity.class));
                    return true;
                }
                return false;
            });
        }
    }
}
