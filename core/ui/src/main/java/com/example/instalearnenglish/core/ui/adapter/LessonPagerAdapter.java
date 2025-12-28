package com.example.instalearnenglish.core.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class LessonPagerAdapter extends FragmentStateAdapter {

    public LessonPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    // 4 Tab: Tip, Vocab, Simulation, Game
    @Override
    public int getItemCount() {
        return 4;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // This will be overridden by feature-specific adapters
        return new Fragment();
    }
}
