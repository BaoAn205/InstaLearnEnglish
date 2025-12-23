package com.example.instalearnenglish.feature.home.tools;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.instalearnenglish.feature.home.HomeActivity;
import com.example.instalearnenglish.feature.home.R;
import com.example.instalearnenglish.feature.home.databinding.FeatureHomeFlashcardsBinding;
import com.example.instalearnenglish.feature.home.profile.ProfileActivity;

public class FlashcardsActivity extends AppCompatActivity {

    private FeatureHomeFlashcardsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            binding = FeatureHomeFlashcardsBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            setupBottomNavigation();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi nạp Thẻ từ", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupBottomNavigation() {
        binding.bottomNavigation.setSelectedItemId(R.id.nav_flashcards);
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
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }
}
