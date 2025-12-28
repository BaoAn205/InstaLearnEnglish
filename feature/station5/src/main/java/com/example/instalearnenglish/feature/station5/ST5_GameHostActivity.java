package com.example.instalearnenglish.feature.station5;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class ST5_GameHostActivity extends AppCompatActivity {

    public static final String EXTRA_GAME_TYPE = "EXTRA_GAME_TYPE";
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.st5_activity_game);

        String gameType = getIntent().getStringExtra("EXTRA_GAME_TYPE");
        Log.d("ST5_GameHost", "Opening game type: " + gameType);

        if (gameType == null) {
            finish();
            return;
        }

        // Setup Music
        int musicResId = R.raw.game_music1;
        if ("MENU_CATCHER".equals(gameType)) {
            musicResId = R.raw.game_music3;
        } else if ("PRICE_DETECTIVE".equals(gameType)) {
            musicResId = R.raw.game_music2;
        }

        try {
            mediaPlayer = MediaPlayer.create(this, musicResId);
            if (mediaPlayer != null) {
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
            }
        } catch (Exception e) {
            Log.e("ST5_GameHost", "Music error: " + e.getMessage());
        }

        if (savedInstanceState == null) {
            Fragment fragment = null;
            switch (gameType) {
                case "MENU_CATCHER":
                    fragment = new ST5_MenuCatcherGameFragment();
                    break;
                case "PRICE_DETECTIVE":
                    fragment = new ST5_PriceDetectiveGameFragment();
                    break;
                case "DRAG_AND_DROP_LUGGAGE":
                    fragment = new ST5_DragAndDropLuggageFragment();
                    break;
                case "QUIZ_PROHIBITED_ITEMS":
                    fragment = new ST5_QuizGameFragment();
                    break;
                case "GUESS_MY_TRIP":
                    fragment = new ST5_GuessTripGameFragment();
                    break;
                case "EMOJI_PACKING":
                    fragment = new ST5_EmojiPackingGameFragment();
                    break;
                case "WHATS_IN_MY_BAG":
                    fragment = new ST5_WhatsInMyBagGameFragment();
                    break;
                case "FORGOT_SOMETHING":
                    fragment = new ST5_ForgotSomethingGameFragment();
                    break;
                case "WORD_IMAGE_MATCH":
                    fragment = new ST5_WordImageMatchGameFragment();
                    break;
            }

            if (fragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.game_fragment_container, fragment)
                        .commit();
            } else {
                Log.e("ST5_GameHost", "Fragment is NULL for type: " + gameType);
                finish();
            }
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
    protected void onResume() {
        super.onResume();
        if (mediaPlayer != null) {
            mediaPlayer.start();
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
