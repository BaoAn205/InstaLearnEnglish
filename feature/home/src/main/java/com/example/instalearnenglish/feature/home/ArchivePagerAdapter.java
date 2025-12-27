package com.example.instalearnenglish.feature.home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ArchivePagerAdapter extends FragmentStateAdapter {

    public ArchivePagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new TipsFragment(); // Will be created next
            case 1:
                return new VocabsFragment(); // Will be created next
            default:
                return new Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2; // We have two tabs
    }
}
