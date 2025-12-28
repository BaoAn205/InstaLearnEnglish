package com.example.instalearnenglish.feature.station4;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class ST4_GameHostActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.st4_activity_game);

        String gameType = getIntent().getStringExtra("EXTRA_GAME_TYPE");
        Log.d("ST4_GameHost", "Opening game type: " + gameType);

        if (gameType == null) {
            finish();
            return;
        }

        // Setup Music
        int musicResId = R.raw.game_music1;
        if ("EMOJI_PACKING".equals(gameType) || "WHATS_IN_MY_BAG".equals(gameType)) {
            musicResId = R.raw.game_music3;
        } else if ("FORGOT_SOMETHING".equals(gameType) || "WORD_IMAGE_MATCH".equals(gameType) || "ROOM_SERVICE_MASTER".equals(gameType)) {
            musicResId = R.raw.game_music2;
        }

        try {
            mediaPlayer = MediaPlayer.create(this, musicResId);
            if (mediaPlayer != null) {
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
            }
        } catch (Exception e) {
            Log.e("ST4_GameHost", "Music error: " + e.getMessage());
        }

        if (savedInstanceState == null) {
            Fragment fragment = null;
            switch (gameType) {
                case "POLITE_GUEST":
                    fragment = new ST4_PoliteGuestGameFragment();
                    break;
                case "ROOM_SERVICE_MASTER":
                    fragment = new ST4_RoomServiceMasterGameFragment();
                    break;
                case "DRAG_AND_DROP_LUGGAGE":
                    fragment = new ST4_DragAndDropLuggageFragment();
                    break;
                case "QUIZ_PROHIBITED_ITEMS":
                    fragment = new ST4_QuizGameFragment();
                    break;
                case "GUESS_MY_TRIP":
                    fragment = new ST4_GuessTripGameFragment();
                    break;
                case "EMOJI_PACKING":
                    fragment = new ST4_EmojiPackingGameFragment();
                    break;
                case "WHATS_IN_MY_BAG":
                    fragment = new ST4_WhatsInMyBagGameFragment();
                    break;
                case "FORGOT_SOMETHING":
                    fragment = new ST4_ForgotSomethingGameFragment();
                    break;
                case "WORD_IMAGE_MATCH":
                    fragment = new ST4_WordImageMatchGameFragment();
                    break;
            }

            if (fragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.game_fragment_container, fragment)
                        .commit();
            } else {
                Log.e("ST4_GameHost", "Fragment is NULL for type: " + gameType);
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
