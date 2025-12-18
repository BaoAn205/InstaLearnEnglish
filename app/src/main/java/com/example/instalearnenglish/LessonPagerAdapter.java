package com.example.instalearnenglish;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class LessonPagerAdapter extends FragmentStateAdapter {

    public LessonPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            // For now, we will return a blank fragment for all tabs.
            // We will create the specific fragments in the next steps.
            case 0:
            case 1:
            case 2:
            case 3:
            default:
                return new Fragment(); 
        }
    }

    @Override
    public int getItemCount() {
        return 4; // We have 4 tabs
    }
}
