package com.example.instalearnenglish.feature.station23;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.instalearnenglish.feature.home.tools.DictionaryDialogFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class Station23Activity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private Station23PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station23);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        ImageButton btnBack = findViewById(R.id.btn_back);
        ImageButton btnDictionary = findViewById(R.id.btn_dictionary);
        TextView toolbarTitle = findViewById(R.id.toolbar_title);

        // Get stationId from Intent using the correct key "LEVEL" (consistent with HomeActivity)
        int stationId = getIntent().getIntExtra("LEVEL", 2);

        if (toolbarTitle != null) {
            toolbarTitle.setText(stationId == 2 ? "Trạm 2: Sân bay" : "Trạm 3: Di chuyển");
        }

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> onBackPressed());
        }

        if (btnDictionary != null) {
            btnDictionary.setOnClickListener(v -> {
                DictionaryDialogFragment dialogFragment = new DictionaryDialogFragment();
                dialogFragment.show(getSupportFragmentManager(), "DictionaryDialog");
            });
        }

        // Pass stationId to the PagerAdapter
        pagerAdapter = new Station23PagerAdapter(this, stationId);
        viewPager.setAdapter(pagerAdapter);
        
        // Vô hiệu hóa lướt ngang của ViewPager để ưu tiên lướt các thẻ Tip
        viewPager.setUserInputEnabled(false);

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
