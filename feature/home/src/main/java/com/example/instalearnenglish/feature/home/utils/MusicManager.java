package com.example.instalearnenglish.feature.home.utils;

import android.content.Context;
import android.media.MediaPlayer;
import com.example.instalearnenglish.feature.home.R;

public class MusicManager {
    private static MediaPlayer mediaPlayer;
    public static boolean isNavigationToMusicActivity = false;

    public static void start(Context context) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context.getApplicationContext(), R.raw.backgroundmusic);
            mediaPlayer.setLooping(true);
        }
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    public static void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying() && !isNavigationToMusicActivity) {
            mediaPlayer.pause();
        }
    }

    public static void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
