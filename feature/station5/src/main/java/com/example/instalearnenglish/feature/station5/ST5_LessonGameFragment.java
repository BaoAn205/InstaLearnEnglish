package com.example.instalearnenglish.feature.station5;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instalearnenglish.feature.station5.adapter.ST5_GameAdapter;

import java.util.ArrayList;
import java.util.List;

public class ST5_LessonGameFragment extends Fragment {

    private RecyclerView gamesRecyclerView;
    private List<ST5_Game> gameList;
    private Button btnViewHistory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.st5_fragment_lesson_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gamesRecyclerView = view.findViewById(R.id.games_recycler_view);
        btnViewHistory = view.findViewById(R.id.btn_view_history_st5);
        
        gamesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        loadGames();

        ST5_GameAdapter adapter = new ST5_GameAdapter(gameList, game -> {
            if (game.isImplemented()) {
                Intent intent = new Intent(getActivity(), ST5_GameHostActivity.class);
                intent.putExtra("EXTRA_GAME_TYPE", game.getType());
                intent.putExtra("EXTRA_GAME_TITLE", game.getTitle());
                startActivity(intent);
            } else {
                Toast.makeText(getContext(), game.getTitle() + " is coming soon!", Toast.LENGTH_SHORT).show();
            }
        });
        gamesRecyclerView.setAdapter(adapter);

        if (btnViewHistory != null) {
            btnViewHistory.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), ST5_GameHistoryActivity.class);
                startActivity(intent);
            });
        }
    }

    private void loadGames() {
        gameList = new ArrayList<>();
        // Chỉ giữ lại các game của Station 5
        gameList.add(new ST5_Game("The Menu Catcher", "MENU_CATCHER", R.raw.game_audio, true));
        gameList.add(new ST5_Game("Price Detective", "PRICE_DETECTIVE", R.raw.game_quiz, true));
    }

    public static class ST5_Game {
        private final String title;
        private final String type;
        private final int iconResId; 
        private final boolean isImplemented;

        public ST5_Game(String title, String type, int iconResId, boolean isImplemented) {
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
