package com.example.instalearnenglish;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;
import java.util.Map;

public class ST1_LessonGameFragment extends Fragment {

    private LinearLayout gamesContainer;
    private FirebaseFirestore db;
    private String lessonId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            lessonId = getArguments().getString("LESSON_ID");
        }
        return inflater.inflate(R.layout.st1_fragment_lesson_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gamesContainer = view.findViewById(R.id.games_container);
        db = FirebaseFirestore.getInstance();
        if (lessonId != null) {
            loadGamesFromFirestore();
        }
    }

    private void loadGamesFromFirestore() {
        db.collection("journey_content").document(lessonId)
            .get()
            .addOnSuccessListener(documentSnapshot -> {
                if (getContext() == null || !documentSnapshot.exists()) return;

                List<Map<String, Object>> gamesList = (List<Map<String, Object>>) documentSnapshot.get("games");

                if (gamesList != null) {
                    gamesContainer.removeAllViews();
                    for (Map<String, Object> gameMap : gamesList) {
                        ST1_Game ST1_Game = new ST1_Game();
                        ST1_Game.setTitle((String) gameMap.get("title"));
                        ST1_Game.setType((String) gameMap.get("type"));
                        ST1_Game.setLevel((Long) gameMap.get("level"));

                        addGameCardToView(ST1_Game);
                    }
                }
            });
    }

    private void addGameCardToView(ST1_Game ST1_Game) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View cardView = inflater.inflate(R.layout.st1_game_card_item, gamesContainer, false);

        TextView title = cardView.findViewById(R.id.game_title);
        TextView description = cardView.findViewById(R.id.game_description);
        View playButton = cardView.findViewById(R.id.play_button);

        title.setText(ST1_Game.getTitle());
        // You can decide how to display the other info
        description.setText("Type: " + ST1_Game.getType() + " - Level: " + ST1_Game.getLevel());

        playButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ST1_GameHostActivity.class);
            intent.putExtra(ST1_GameHostActivity.EXTRA_GAME_TYPE, ST1_Game.getType());
            intent.putExtra(ST1_GameHostActivity.EXTRA_GAME_TITLE, ST1_Game.getTitle());
            startActivity(intent);
        });

        gamesContainer.addView(cardView);
    }
}
