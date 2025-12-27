package com.example.instalearnenglish.feature.station4;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instalearnenglish.feature.station4.adapter.ST4_GameAdapter;

import java.util.ArrayList;
import java.util.List;

public class ST4_LessonGameFragment extends Fragment {

    private RecyclerView gamesRecyclerView;
    private List<ST4_Game> gameList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.st4_fragment_lesson_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gamesRecyclerView = view.findViewById(R.id.games_recycler_view);
        gamesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        loadGames();

        ST4_GameAdapter adapter = new ST4_GameAdapter(gameList, game -> {
            if (game.isImplemented()) {
                Intent intent = new Intent(getActivity(), ST4_GameHostActivity.class);
                intent.putExtra(ST4_GameHostActivity.EXTRA_GAME_TYPE, game.getType());
                intent.putExtra(ST4_GameHostActivity.EXTRA_GAME_TITLE, game.getTitle());
                startActivity(intent);
            } else {
                Toast.makeText(getContext(), game.getTitle() + " is coming soon!", Toast.LENGTH_SHORT).show();
            }
        });
        gamesRecyclerView.setAdapter(adapter);
    }

    private void loadGames() {
        gameList = new ArrayList<>();
        // Using R.raw resource IDs instead of string names
        gameList.add(new ST4_Game("Packing Luggage", "DRAG_AND_DROP_LUGGAGE", R.raw.game_drag, true));
        gameList.add(new ST4_Game("Prohibited Items Quiz", "QUIZ_PROHIBITED_ITEMS", R.raw.game_quiz, true));
        gameList.add(new ST4_Game("Guess My Trip", "GUESS_MY_TRIP", R.raw.game_guess, true));
        gameList.add(new ST4_Game("Emoji Packing", "EMOJI_PACKING", R.raw.game_emoji, true));
        gameList.add(new ST4_Game("What’s in My Bag?", "WHATS_IN_MY_BAG", R.raw.game_audio, true));
        gameList.add(new ST4_Game("Forgot Something!", "FORGOT_SOMETHING", R.raw.game_memory, true));
        gameList.add(new ST4_Game("Word → Image Match", "WORD_IMAGE_MATCH", R.raw.game_match, true));
    }

    public static class ST4_Game {
        private final String title;
        private final String type;
        private final int iconResId; 
        private final boolean isImplemented;

        public ST4_Game(String title, String type, int iconResId, boolean isImplemented) {
            this.title = title;
            this.type = type;
            this.iconResId = iconResId;
            this.isImplemented = isImplemented;
        }

        public String getTitle() { return title; }
        public String getType() { return type; }
        public int getIconResId() { return iconResId; } 
        public boolean isImplemented() { return isImplemented; }
    }
}
