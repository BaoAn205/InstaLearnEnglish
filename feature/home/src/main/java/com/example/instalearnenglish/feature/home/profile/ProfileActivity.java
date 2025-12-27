package com.example.instalearnenglish.feature.home.profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.instalearnenglish.feature.home.ArchiveActivity;
import com.example.instalearnenglish.feature.home.HomeActivity;
import com.example.instalearnenglish.feature.home.auth.LoginActivity;
import com.example.instalearnenglish.feature.home.databinding.FeatureHomeProfileBinding;
import com.example.instalearnenglish.feature.home.tools.DictionaryDialogFragment;
import com.example.instalearnenglish.feature.home.utils.MusicManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.instalearnenglish.feature.home.R;

public class ProfileActivity extends AppCompatActivity {

    private FeatureHomeProfileBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FeatureHomeProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        setupBottomNavigation();
        setupLogoutButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MusicManager.isNavigationToMusicActivity = false;
        MusicManager.start(this);
        fetchAndDisplayUserData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MusicManager.pause();
    }

    private void fetchAndDisplayUserData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            goToLogin();
            return;
        }

        binding.tvUserEmail.setText(currentUser.getEmail());

        db.collection("users").document(currentUser.getUid()).get()
            .addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    String fullName = documentSnapshot.getString("fullName");
                    binding.tvUserName.setText(fullName != null ? fullName : "Traveler");

                    Long currentLevel = documentSnapshot.getLong("currentLevel");
                    binding.tvCurrentStation.setText(currentLevel != null ? String.valueOf(currentLevel) : "1");

                    Long dayStreak = documentSnapshot.getLong("dayStreak");
                    binding.tvDayStreak.setText(dayStreak != null ? String.valueOf(dayStreak) : "1");

                } else {
                    binding.tvUserName.setText("Traveler");
                    binding.tvCurrentStation.setText("1");
                    binding.tvDayStreak.setText("1");
                }
            })
            .addOnFailureListener(e -> {
                Toast.makeText(ProfileActivity.this, "Failed to load profile data.", Toast.LENGTH_SHORT).show();
                binding.tvUserName.setText("Traveler");
                binding.tvCurrentStation.setText("-");
                binding.tvDayStreak.setText("-");
            });
    }

    private void setupLogoutButton() {
        binding.btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
            goToLogin();
        });
    }

    private void goToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void setupBottomNavigation() {
        binding.bottomNavigation.setSelectedItemId(R.id.nav_profile);
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                MusicManager.isNavigationToMusicActivity = true;
                startActivity(new Intent(this, HomeActivity.class));
                finish();
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
                return true;
            }
            return false;
        });
    }
}
