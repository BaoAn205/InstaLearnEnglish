package com.example.instalearnenglish.feature.station5;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ST5_LessonPagerAdapter extends FragmentStateAdapter {
    private final String lessonId;

    public ST5_LessonPagerAdapter(@NonNull FragmentActivity fragmentActivity, String lessonId) {
        super(fragmentActivity);
        this.lessonId = lessonId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new ST5_LessonTipsFragment();
                break;
            case 1:
                fragment = new ST5_LessonVocabFragment();
                break;
            case 2:
                fragment = new ST5_LessonSimulationFragment();
                break;
            case 3:
                fragment = new ST5_LessonGameFragment();
                break;
            default:
                fragment = new Fragment();
                break;
        }

        // Pass lessonId to fragments
        Bundle args = new Bundle();
        args.putString("LESSON_ID", lessonId);
        fragment.setArguments(args);
        
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 4; // Tip, Vocab, Simulation, Game
    }
}
