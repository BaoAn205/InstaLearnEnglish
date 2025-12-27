package com.example.instalearnenglish.feature.home;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.instalearnenglish.feature.home.auth.LoginActivity;
import com.example.instalearnenglish.feature.home.profile.ProfileActivity;
import com.example.instalearnenglish.feature.home.tools.DictionaryDialogFragment;
import com.example.instalearnenglish.feature.home.utils.MusicManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
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
    private MediaPlayer clickSoundPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        initializeJourneyViews();
        setupStationListeners();
        setupBottomNavigation();

        clickSoundPlayer = MediaPlayer.create(this, R.raw.click_to_station);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MusicManager.isNavigationToMusicActivity = false;
        MusicManager.start(this);
        fetchUserProgressAndSetupUI();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MusicManager.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (clickSoundPlayer != null) {
            clickSoundPlayer.release();
            clickSoundPlayer = null;
        }
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
                checkAndUpdateDayStreak(documentSnapshot, userDocRef);
                Long currentLevel = documentSnapshot.getLong("currentLevel");
                updateJourneyUI(currentLevel != null ? currentLevel : 1L);
            } else {
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

        if (isSameDay) return;

        cal1.add(Calendar.DAY_OF_YEAR, 1);
        boolean isYesterday = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                              cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);

        Map<String, Object> streakUpdate = new HashMap<>();
        if (isYesterday) {
            streakUpdate.put("dayStreak", dayStreak + 1);
        } else {
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
            if (v.isEnabled()) {
                if (clickSoundPlayer != null) clickSoundPlayer.start();
                openLesson(1); 
            }
        });
        station2.setOnClickListener(v -> { 
            if (v.isEnabled()) {
                if (clickSoundPlayer != null) clickSoundPlayer.start();
                openLesson(2); 
            }
        });
        station3.setOnClickListener(v -> { 
            if (v.isEnabled()) {
                if (clickSoundPlayer != null) clickSoundPlayer.start();
                openLesson(3); 
            }
        });
        station4.setOnClickListener(v -> { 
            if (v.isEnabled()) {
                if (clickSoundPlayer != null) clickSoundPlayer.start();
                openLesson(4); 
            }
        });
        station5.setOnClickListener(v -> { 
            if (v.isEnabled()) {
                if (clickSoundPlayer != null) clickSoundPlayer.start();
                openLesson(5); 
            }
        });
    }

    private void openLesson(int level) {
        try {
            Intent intent = new Intent();
            String className;
            switch (level) {
                case 1: className = "com.example.instalearnenglish.feature.station1.Station1Activity"; break;
                case 2: case 3: className = "com.example.instalearnenglish.feature.station23.Station23Activity"; break;
                case 4: case 5: className = "com.example.instalearnenglish.feature.station45.Station45Activity"; break;
                default: return;
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
                    MusicManager.isNavigationToMusicActivity = true;
                    DictionaryDialogFragment dialogFragment = new DictionaryDialogFragment();
                    dialogFragment.show(getSupportFragmentManager(), "DictionaryDialog");
                    return true;
                } else if (itemId == R.id.nav_archive) {
                    startActivity(new Intent(this, ArchiveActivity.class));
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    MusicManager.isNavigationToMusicActivity = true;
                    startActivity(new Intent(this, ProfileActivity.class));
                    return true;
                }
                return false;
            });
        }
    }
}
