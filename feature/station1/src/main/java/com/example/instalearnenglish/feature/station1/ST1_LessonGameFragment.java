package com.example.instalearnenglish.feature.station1;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ST1_LessonGameFragment extends Fragment {

    private LinearLayout gamesContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.st1_fragment_lesson_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gamesContainer = view.findViewById(R.id.games_container);
        loadGames();
    }

    private void loadGames() {
        if (getContext() == null) return;
        gamesContainer.removeAllViews();

        addGameCardToView(new ST1_Game("Packing Luggage", "DRAG_AND_DROP_LUGGAGE", "Drag items to the correct luggage."));
        addGameCardToView(new ST1_Game("Prohibited Items Quiz", "QUIZ_PROHIBITED_ITEMS", "Test your knowledge on what you can and cannot bring."));
        addGameCardToView(new ST1_Game("Guess My Trip", "GUESS_MY_TRIP", "Deduce the destination from the items listed."));
        addGameCardToView(new ST1_Game("Emoji Packing Challenge", "EMOJI_PACKING", "Guess the items from the emojis and say them out loud."));
        addGameCardToView(new ST1_Game("What’s in My Bag?", "WHATS_IN_MY_BAG", "Listen to the description and guess the item."));
        addGameCardToView(new ST1_Game("Forgot Something!", "FORGOT_SOMETHING", "Memorize the items and find what's missing."));
        addGameCardToView(new ST1_Game("Word → Image Match", "WORD_IMAGE_MATCH", "Match the word to the correct image."));
    }

    private void addGameCardToView(ST1_Game game) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View cardView = inflater.inflate(R.layout.st1_game_card_item, gamesContainer, false);

        TextView title = cardView.findViewById(R.id.game_title);
        TextView description = cardView.findViewById(R.id.game_description);
        View playButton = cardView.findViewById(R.id.play_button);

        title.setText(game.getTitle());
        description.setText(game.getDescription());

        playButton.setOnClickListener(v -> {
            // Updated logic to check if game is implemented
            if (game.getType().equals("DRAG_AND_DROP_LUGGAGE") || 
                game.getType().equals("QUIZ_PROHIBITED_ITEMS") ||
                game.getType().equals("GUESS_MY_TRIP") ||
                game.getType().equals("EMOJI_PACKING") ||
                game.getType().equals("WHATS_IN_MY_BAG") ||
                game.getType().equals("FORGOT_SOMETHING") ||
                game.getType().equals("WORD_IMAGE_MATCH")) {
                
                Intent intent = new Intent(getActivity(), ST1_GameHostActivity.class);
                intent.putExtra(ST1_GameHostActivity.EXTRA_GAME_TYPE, game.getType());
                intent.putExtra(ST1_GameHostActivity.EXTRA_GAME_TITLE, game.getTitle());
                startActivity(intent);
            } else {
                Toast.makeText(getContext(), game.getTitle() + " is coming soon!", Toast.LENGTH_SHORT).show();
            }
        });

        gamesContainer.addView(cardView);
    }

    // Dummy Game class for this fragment
    private static class ST1_Game {
        private String title;
        private String type;
        private String description;

        public ST1_Game(String title, String type, String description) {
            this.title = title;
            this.type = type;
            this.description = description;
        }

        public String getTitle() { return title; }
        public String getType() { return type; }
        public String getDescription() { return description; }
    }
}
