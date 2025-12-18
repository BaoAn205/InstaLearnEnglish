package com.example.instalearnenglish;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class LessonActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        toolbar = findViewById(R.id.toolbar_lesson);
        tabLayout = findViewById(R.id.tab_layout_lesson);
        viewPager = findViewById(R.id.view_pager_lesson);

        // Handle back button
        toolbar.setNavigationOnClickListener(v -> finish());

        // Setup ViewPager with Adapter
        LessonPagerAdapter adapter = new LessonPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // Link TabLayout with ViewPager
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Mẹo");
                            break;
                        case 1:
                            tab.setText("Từ vựng");
                            break;
                        case 2:
                            tab.setText("Giả lập");
                            break;
                        case 3:
                            tab.setText("Game");
                            break;
                    }
                }
        ).attach();
    }
}
