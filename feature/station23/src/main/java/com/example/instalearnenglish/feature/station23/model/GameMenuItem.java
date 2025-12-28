package com.example.instalearnenglish.feature.station23.model;

import androidx.annotation.DrawableRes;
import androidx.annotation.RawRes;

public class GameMenuItem {
    private final String title;
    // This can be used for both Drawable and Raw resources
    @RawRes @DrawableRes
    private final int resourceId;
    private final Class<?> fragmentClass; // The fragment to launch for this game

    public GameMenuItem(String title, @RawRes @DrawableRes int resourceId, Class<?> fragmentClass) {
        this.title = title;
        this.resourceId = resourceId;
        this.fragmentClass = fragmentClass;
    }

    public String getTitle() {
        return title;
    }

    public int getResourceId() {
        return resourceId;
    }

    public Class<?> getFragmentClass() {
        return fragmentClass;
    }
}
