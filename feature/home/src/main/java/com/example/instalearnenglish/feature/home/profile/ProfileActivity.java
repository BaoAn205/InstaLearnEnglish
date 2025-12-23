package com.example.instalearnenglish.feature.home.profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.instalearnenglish.feature.home.HomeActivity;
import com.example.instalearnenglish.feature.home.R;
import com.example.instalearnenglish.feature.home.auth.LoginActivity;
import com.example.instalearnenglish.feature.home.databinding.FeatureHomeProfileBinding;
import com.example.instalearnenglish.feature.home.tools.DictionaryActivity;
import com.example.instalearnenglish.feature.home.tools.FlashcardsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {

    private FeatureHomeProfileBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            binding = FeatureHomeProfileBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            mAuth = FirebaseAuth.getInstance();
            db = FirebaseFirestore.getInstance();

            setupProfileData();
            setupBottomNavigation();

            binding.btnLogout.setOnClickListener(v -> {
                mAuth.signOut();
                Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi nạp Hồ sơ", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupProfileData() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            binding.tvUserEmail.setText(user.getEmail());
            db.collection("users").document(user.getUid()).get()
                .addOnSuccessListener(document -> {
                    if (document != null && document.exists()) {
                        String fullName = document.getString("fullName");
                        binding.tvUserName.setText(fullName != null ? fullName : "Traveler");
                    }
                });
        }
    }

    private void setupBottomNavigation() {
        binding.bottomNavigation.setSelectedItemId(R.id.nav_profile);
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                Intent intent = new Intent(this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.nav_dictionary) {
                startActivity(new Intent(this, DictionaryActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_flashcards) {
                startActivity(new Intent(this, FlashcardsActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_profile) {
                return true;
            }
            return false;
        });
    }
}
