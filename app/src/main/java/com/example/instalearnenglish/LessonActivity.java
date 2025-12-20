package com.example.instalearnenglish;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;

public class LessonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int level = getIntent().getIntExtra("LEVEL", 1);

        int layoutId;
        switch (level) {
            case 1:
                layoutId = R.layout.activity_lesson_station_1;
                break;
            case 2:
                layoutId = R.layout.activity_lesson_station_2;
                break;
            case 3:
                layoutId = R.layout.activity_lesson_station_3;
                break;
            case 4:
                layoutId = R.layout.activity_lesson_station_4;
                break;
            case 5:
                layoutId = R.layout.activity_lesson_station_5;
                break;
            default:
                layoutId = R.layout.activity_lesson_station_1; // Fallback to a valid lesson layout
                break;
        }
        setContentView(layoutId);

        MaterialToolbar toolbar = findViewById(R.id.toolbar_lesson);
        // Handle the back button
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }
}
