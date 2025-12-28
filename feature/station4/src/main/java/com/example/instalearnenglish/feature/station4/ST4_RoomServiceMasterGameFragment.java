package com.example.instalearnenglish.feature.station4;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.instalearnenglish.feature.station4.utils.ST4_GameHistoryManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ST4_RoomServiceMasterGameFragment extends Fragment {

    private TextView tvWordToSort, tvScore;
    private CardView cardRoomTypes, cardAmenities, cardStaff, cardTargetWord;
    
    private List<GameItem> gameItems;
    private int currentIndex = 0;
    private int score = 0;

    private static class GameItem {
        String word;
        String category; // "ROOM", "AMENITY", "STAFF"
        GameItem(String word, String category) {
            this.word = word;
            this.category = category;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.st4_fragment_room_service_master_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvWordToSort = view.findViewById(R.id.tv_word_to_sort);
        tvScore = view.findViewById(R.id.tv_score);
        cardRoomTypes = view.findViewById(R.id.card_room_types);
        cardAmenities = view.findViewById(R.id.card_amenities);
        cardStaff = view.findViewById(R.id.card_staff);
        cardTargetWord = view.findViewById(R.id.card_target_word);

        loadData();
        showNextWord();

        cardRoomTypes.setOnClickListener(v -> checkAnswer("ROOM"));
        cardAmenities.setOnClickListener(v -> checkAnswer("AMENITY"));
        cardStaff.setOnClickListener(v -> checkAnswer("STAFF"));
    }

    private void loadData() {
        gameItems = new ArrayList<>();
        // Room Types
        gameItems.add(new GameItem("Single Room", "ROOM"));
        gameItems.add(new GameItem("Double Room", "ROOM"));
        gameItems.add(new GameItem("Suite", "ROOM"));
        gameItems.add(new GameItem("Penthouse", "ROOM"));
        gameItems.add(new GameItem("Twin Room", "ROOM"));
        
        // Amenities
        gameItems.add(new GameItem("Mini-bar", "AMENITY"));
        gameItems.add(new GameItem("Safe", "AMENITY"));
        gameItems.add(new GameItem("Air Conditioner", "AMENITY"));
        gameItems.add(new GameItem("Hairdryer", "AMENITY"));
        gameItems.add(new GameItem("Slippers", "AMENITY"));
        
        // Staff
        gameItems.add(new GameItem("Receptionist", "STAFF"));
        gameItems.add(new GameItem("Bellhop", "STAFF"));
        gameItems.add(new GameItem("Housekeeper", "STAFF"));
        gameItems.add(new GameItem("Room Service", "STAFF"));
        gameItems.add(new GameItem("Laundry Service", "STAFF"));

        Collections.shuffle(gameItems);
    }

    private void showNextWord() {
        if (currentIndex < gameItems.size()) {
            tvWordToSort.setText(gameItems.get(currentIndex).word);
            tvScore.setText("Score: " + score + "/" + gameItems.size());
            
            // Animation for new word
            Animation anim = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);
            cardTargetWord.startAnimation(anim);
        } else {
            showGameFinish();
        }
    }

    private void checkAnswer(String selectedCategory) {
        String correctCategory = gameItems.get(currentIndex).category;
        
        if (selectedCategory.equals(correctCategory)) {
            score++;
            Toast.makeText(getContext(), "Correct!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Wrong category!", Toast.LENGTH_SHORT).show();
        }

        currentIndex++;
        new Handler(Looper.getMainLooper()).postDelayed(this::showNextWord, 500);
    }

    private void showGameFinish() {
        tvWordToSort.setText("Finish!");
        tvScore.setText("Final Score: " + score + "/" + gameItems.size());
        
        // Lưu lịch sử game
        ST4_GameHistoryManager.saveResult(getContext(), "Room Service Master", "Score: " + score + "/" + gameItems.size());
        
        Toast.makeText(getContext(), "Great job! You master the room service!", Toast.LENGTH_LONG).show();
        
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (getActivity() != null) getActivity().finish();
        }, 2000);
    }
}
