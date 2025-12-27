package com.example.instalearnenglish.feature.home;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.example.instalearnenglish.feature.home.profile.ProfileActivity;
import com.example.instalearnenglish.feature.home.tools.DictionaryDialogFragment;
import com.example.instalearnenglish.feature.home.utils.MusicManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ArchiveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);

        Toolbar toolbar = findViewById(R.id.toolbar_archive);
        setSupportActionBar(toolbar);

        ViewPager2 viewPager = findViewById(R.id.view_pager_archive);
        TabLayout tabLayout = findViewById(R.id.tab_layout_archive);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        ArchivePagerAdapter adapter = new ArchivePagerAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Tips");
                    break;
                case 1:
                    tab.setText("Vocabs");
                    break;
            }
        }).attach();

        bottomNavigationView.setSelectedItemId(R.id.nav_archive);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                MusicManager.isNavigationToMusicActivity = true;
                startActivity(new Intent(this, HomeActivity.class));
                return true;
            } else if (itemId == R.id.nav_dictionary) {
                DictionaryDialogFragment dialogFragment = new DictionaryDialogFragment();
                dialogFragment.show(getSupportFragmentManager(), "DictionaryDialog");
                return true;
            } else if (itemId == R.id.nav_archive) {
                return true;
            } else if (itemId == R.id.nav_profile) {
                MusicManager.isNavigationToMusicActivity = true;
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MusicManager.isNavigationToMusicActivity = false;
        MusicManager.start(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!MusicManager.isNavigationToMusicActivity) {
            MusicManager.pause();
        }
    }
}
