package com.example.instalearnenglish.feature.home.tools;

import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.instalearnenglish.feature.home.HomeActivity;
import com.example.instalearnenglish.feature.home.R;
import com.example.instalearnenglish.feature.home.databinding.FeatureHomeDictionaryBinding;
import com.example.instalearnenglish.feature.home.profile.ProfileActivity;

public class DictionaryActivity extends AppCompatActivity {

    private FeatureHomeDictionaryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            binding = FeatureHomeDictionaryBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            setupSearch();
            setupBottomNavigation();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Không thể khởi tạo giao diện Từ điển", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupSearch() {
        binding.etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String query = binding.etSearch.getText().toString().trim();
                if (!query.isEmpty()) {
                    Toast.makeText(this, "Đang tra: " + query, Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            return false;
        });
    }

    private void setupBottomNavigation() {
        binding.bottomNavigation.setSelectedItemId(R.id.nav_dictionary);
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                Intent intent = new Intent(this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.nav_dictionary) {
                return true;
            } else if (itemId == R.id.nav_flashcards) {
                startActivity(new Intent(this, FlashcardsActivity.class));
                finish();
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
