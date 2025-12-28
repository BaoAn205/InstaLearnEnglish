package com.example.instalearnenglish.feature.station23;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class Station23Activity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private Station23PagerAdapter pagerAdapter;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station23);

        toolbar = findViewById(R.id.toolbar);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        // Get stationId from Intent (default to 2 if not provided)
        int stationId = getIntent().getIntExtra("STATION_ID", 2);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(stationId == 2 ? "Trạm 2: Sân bay" : "Trạm 3: Di chuyển");
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Pass stationId to the PagerAdapter
        pagerAdapter = new Station23PagerAdapter(this, stationId);
        viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0: tab.setText("Travel Tip"); break;
                        case 1: tab.setText("Vocabulary"); break;
                        case 2: tab.setText("Simulator"); break;
                        case 3: tab.setText("Mini-Game"); break;
                    }
                }
        ).attach();
    }
}
