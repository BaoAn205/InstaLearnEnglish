package com.example.instalearnenglish.feature.station23;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.instalearnenglish.core.ui.adapter.LessonPagerAdapter;

public class Station23PagerAdapter extends LessonPagerAdapter {

    private final int stationId;

    public Station23PagerAdapter(@NonNull FragmentActivity fragmentActivity, int stationId) {
        super(fragmentActivity);
        this.stationId = stationId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: // Travel Tip Tab
                if (stationId == 3) {
                    return new St3TravelTipFragment();
                } else {
                    return new St2TravelTipFragment();
                }
            case 1: // Vocabulary Tab
                // Use the new, categorized vocabulary fragments
                if (stationId == 3) {
                    return new St3VocabularyFragment(); 
                } else {
                    return new St2VocabularyFragment();
                }
            case 2: // Simulator Tab
                if (stationId == 3) {
                    return new St3SimulatorFragment();
                } else {
                    return new St23SimulatorFragment();
                }
            case 3: // Mini-Game Tab
                if (stationId == 3) {
                    return new St3GameHubFragment();
                } else {
                    return new St23MiniGameFragment();
                }
            default:
                return super.createFragment(position);
        }
    }
}
