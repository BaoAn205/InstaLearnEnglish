package com.example.instalearnenglish.feature.station23.model;

import androidx.annotation.DrawableRes;

public class GameMenuItem {
    private final String title;
    @DrawableRes
    private final int iconResId;
    private final Class<?> fragmentClass; // The fragment to launch for this game

    public GameMenuItem(String title, @DrawableRes int iconResId, Class<?> fragmentClass) {
        this.title = title;
        this.iconResId = iconResId;
        this.fragmentClass = fragmentClass;
    }

    public String getTitle() {
        return title;
    }

    public int getIconResId() {
        return iconResId;
    }

    public Class<?> getFragmentClass() {
        return fragmentClass;
    }
}
