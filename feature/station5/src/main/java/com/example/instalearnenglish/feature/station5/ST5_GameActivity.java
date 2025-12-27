package com.example.instalearnenglish.feature.station5;

import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class ST5_GameActivity extends AppCompatActivity {

    public static final String EXTRA_GAME_TYPE = "EXTRA_GAME_TYPE";
    public static final String EXTRA_GAME_TITLE = "EXTRA_GAME_TITLE";
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.st5_activity_game);

        String gameType = getIntent().getStringExtra(EXTRA_GAME_TYPE);
        int musicResId = 0;

        if ("DRAG_AND_DROP_LUGGAGE".equals(gameType) || "QUIZ_PROHIBITED_ITEMS".equals(gameType) || "GUESS_MY_TRIP".equals(gameType)) {
            musicResId = R.raw.game_music1;
        } else if ("EMOJI_PACKING".equals(gameType) || "WHATS_IN_MY_BAG".equals(gameType)) {
            musicResId = R.raw.game_music3;
        } else if ("FORGOT_SOMETHING".equals(gameType) || "WORD_IMAGE_MATCH".equals(gameType)) {
            musicResId = R.raw.game_music2;
        }

        if (musicResId != 0) {
            mediaPlayer = MediaPlayer.create(this, musicResId);
            if (mediaPlayer != null) {
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
            }
        }

        if (savedInstanceState == null) {
            Fragment fragment = null;
            if ("DRAG_AND_DROP_LUGGAGE".equals(gameType)) {
                fragment = new ST5_DragAndDropLuggageFragment();
            } else if ("QUIZ_PROHIBITED_ITEMS".equals(gameType)) {
                fragment = new ST5_QuizGameFragment();
            } else if ("GUESS_MY_TRIP".equals(gameType)) {
                fragment = new ST5_GuessTripGameFragment();
            } else if ("EMOJI_PACKING".equals(gameType)) {
                fragment = new ST5_EmojiPackingGameFragment();
            } else if ("WHATS_IN_MY_BAG".equals(gameType)) {
                fragment = new ST5_WhatsInMyBagGameFragment();
            } else if ("FORGOT_SOMETHING".equals(gameType)) {
                fragment = new ST5_ForgotSomethingGameFragment();
            } else if ("WORD_IMAGE_MATCH".equals(gameType)) {
                fragment = new ST5_WordImageMatchGameFragment();
            }

            if (fragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.game_fragment_container, fragment)
                        .commit();
            } else {
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
