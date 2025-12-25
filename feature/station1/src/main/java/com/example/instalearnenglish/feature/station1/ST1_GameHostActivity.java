package com.example.instalearnenglish.feature.station1;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class ST1_GameHostActivity extends AppCompatActivity {

    public static final String EXTRA_GAME_TYPE = "EXTRA_GAME_TYPE";
    public static final String EXTRA_GAME_TITLE = "EXTRA_GAME_TITLE"; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.st1_activity_game);

        if (savedInstanceState == null) {
            String gameType = getIntent().getStringExtra(EXTRA_GAME_TYPE);

            Fragment fragment = null;
            if ("DRAG_AND_DROP_LUGGAGE".equals(gameType)) {
                fragment = new ST1_DragAndDropLuggageFragment();
            } else if ("QUIZ_PROHIBITED_ITEMS".equals(gameType)) {
                fragment = new ST1_QuizGameFragment();
            } else if ("GUESS_MY_TRIP".equals(gameType)) {
                fragment = new ST1_GuessTripGameFragment();
            } else if ("EMOJI_PACKING".equals(gameType)) {
                fragment = new ST1_EmojiPackingGameFragment();
            } else if ("WHATS_IN_MY_BAG".equals(gameType)) {
                fragment = new ST1_WhatsInMyBagGameFragment();
            } else if ("FORGOT_SOMETHING".equals(gameType)) {
                fragment = new ST1_ForgotSomethingGameFragment();
            } else if ("WORD_IMAGE_MATCH".equals(gameType)) {
                fragment = new ST1_WordImageMatchGameFragment();
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
}