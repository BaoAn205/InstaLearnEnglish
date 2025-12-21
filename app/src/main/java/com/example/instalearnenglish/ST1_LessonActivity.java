package com.example.instalearnenglish;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ST1_LessonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int level = getIntent().getIntExtra("LEVEL", 1);

        int layoutId;
        String lessonId;
        switch (level) {
            case 1:
                layoutId = R.layout.activity_lesson_station_1;
                lessonId = "1";
                break;
            case 2:
                layoutId = R.layout.activity_lesson_station_2;
                lessonId = "2";
                break;
            case 3:
                layoutId = R.layout.activity_lesson_station_3;
                lessonId = "3";
                break;
            case 4:
                layoutId = R.layout.activity_lesson_station_4;
                lessonId = "4";
                break;
            case 5:
                layoutId = R.layout.activity_lesson_station_5;
                lessonId = "5";
                break;
            default:
                layoutId = R.layout.activity_lesson_station_1; // Fallback
                lessonId = "1";
                break;
        }
        setContentView(layoutId);

        // --- Toolbar Setup --- //
        MaterialToolbar toolbar = findViewById(R.id.toolbar_lesson);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // --- ViewPager and TabLayout Setup --- //
        ViewPager2 viewPager = findViewById(R.id.view_pager_lesson);
        TabLayout tabLayout = findViewById(R.id.tab_layout_lesson);
        ST1_LessonPagerAdapter adapter = new ST1_LessonPagerAdapter(this, lessonId);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Tip");
                    break;
                case 1:
                    tab.setText("Vocab");
                    break;
                case 2:
                    tab.setText("Simulation");
                    break;
                case 3:
                    tab.setText("Game");
                    break;
            }
        }).attach();
    }
}
