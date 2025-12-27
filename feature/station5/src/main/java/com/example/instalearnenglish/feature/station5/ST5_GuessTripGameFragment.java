package com.example.instalearnenglish.feature.station5;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ST5_GuessTripGameFragment extends Fragment {

    private LinearLayout itemsContainer;
    private EditText etGuess;
    private Button btnGuess;
    private TextView tvTripProgress;

    private List<Trip> tripList;
    private int currentTripIndex = 0;
    private int score = 0;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.st5_fragment_guess_trip_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        itemsContainer = view.findViewById(R.id.items_container);
        etGuess = view.findViewById(R.id.et_guess);
        btnGuess = view.findViewById(R.id.btn_guess);
        tvTripProgress = view.findViewById(R.id.tv_trip_progress);

        view.findViewById(R.id.btn_back).setOnClickListener(v -> requireActivity().finish());

        loadTrips();
        startGame();

        btnGuess.setOnClickListener(v -> checkAnswer());
    }

    private void loadTrips() {
        tripList = new ArrayList<>();
        tripList.add(new Trip(Arrays.asList("swimsuit", "sunscreen", "sunglasses", "flip-flops"), Arrays.asList("beach", "sea", "island", "coast")));
        tripList.add(new Trip(Arrays.asList("skis", "gloves", "beanie", "winter coat"), Arrays.asList("snow", "mountain", "winter", "skiing")));
        tripList.add(new Trip(Arrays.asList("hiking boots", "backpack", "map", "water bottle"), Arrays.asList("forest", "hiking", "trail", "camping")));
        tripList.add(new Trip(Arrays.asList("camera", "guidebook", "comfortable shoes", "city map"), Arrays.asList("city", "tour", "sightseeing", "urban")));
        tripList.add(new Trip(Arrays.asList("laptop", "formal suit", "tie", "presentation slides"), Arrays.asList("business", "conference", "work", "meeting")));
        tripList.add(new Trip(Arrays.asList("light clothing", "sun hat", "sand goggles", "extra water"), Arrays.asList("desert", "safari", "sand", "dunes")));
        tripList.add(new Trip(Arrays.asList("insect repellent", "raincoat", "binoculars", "first-aid kit"), Arrays.asList("jungle", "rainforest", "expedition", "wildlife")));
        tripList.add(new Trip(Arrays.asList("diving mask", "fins", "wetsuit", "oxygen tank"), Arrays.asList("diving", "scuba", "underwater", "reef")));
    }

    private void startGame() {
        currentTripIndex = 0;
        score = 0;
        Collections.shuffle(tripList);
        displayNextTrip();
    }

    private void displayNextTrip() {
        if (!isAdded()) return;
        if (currentTripIndex >= tripList.size()) {
            updateGameProgress();
            return;
        }

        Trip currentTrip = tripList.get(currentTripIndex);
        tvTripProgress.setText("Trip: " + (currentTripIndex + 1) + "/" + tripList.size());

        itemsContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (String item : currentTrip.getItems()) {
            TextView itemView = (TextView) inflater.inflate(R.layout.template_guess_trip_item, itemsContainer, false);
            itemView.setText("• " + item);
            itemsContainer.addView(itemView);
        }
        etGuess.setText("");
        btnGuess.setEnabled(true);
    }

    private void checkAnswer() {
        if (!isAdded() || currentTripIndex >= tripList.size()) return;

        String userAnswer = etGuess.getText().toString().toLowerCase().trim();
        if (userAnswer.isEmpty()) {
            Toast.makeText(getContext(), "Please type your guess!", Toast.LENGTH_SHORT).show();
            return;
        }

        btnGuess.setEnabled(false);

        Trip currentTrip = tripList.get(currentTripIndex);
        boolean isCorrect = false;
        for (String keyword : currentTrip.getKeywords()) {
            if (userAnswer.contains(keyword)) {
                isCorrect = true;
                break;
            }
        }

        if (isCorrect) {
            score++;
            playSoundAndShowToast(true, "Correct! That's a good guess.");
        } else {
            playSoundAndShowToast(false, "Wrong!");
        }

        currentTripIndex++;
        new Handler(Looper.getMainLooper()).postDelayed(this::displayNextTrip, 1500);
    }

    private void playSoundAndShowToast(boolean isCorrect, String message) {
        if (!isAdded()) return;
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        int soundResId = isCorrect ? R.raw.right_answer : R.raw.wrong_answer;
        MediaPlayer mp = MediaPlayer.create(getContext(), soundResId);
        mp.setOnCompletionListener(MediaPlayer::release);
        mp.start();
    }

    private void updateGameProgress() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null || !isAdded()) {
            showFinalScore(false);
            return;
        }
        DocumentReference userDocRef = db.collection("users").document(user.getUid());

        userDocRef.update("station1_completed_games", FieldValue.arrayUnion("GUESS_MY_TRIP"))
                .addOnSuccessListener(aVoid -> {
                    if (!isAdded()) return;
                    userDocRef.get().addOnSuccessListener(documentSnapshot -> {
                        if (!isAdded()) return;
                        if (documentSnapshot.exists()) {
                            List<String> completedGames = (List<String>) documentSnapshot.get("station1_completed_games");
                            if (completedGames != null && completedGames.size() >= 7) {
                                if (documentSnapshot.getLong("currentLevel") == 1L) {
                                    userDocRef.update("currentLevel", 2L)
                                            .addOnSuccessListener(aVoid1 -> showFinalScore(true));
                                } else {
                                    showFinalScore(false);
                                }
                            } else {
                                showFinalScore(false);
                            }
                        }
                    });
                })
                .addOnFailureListener(e -> showFinalScore(false));
    }

    private void showFinalScore(boolean justUnlocked) {
        if (!isAdded()) return;
        String message = "Your score: " + score + "/" + tripList.size();
        if (justUnlocked) {
            message += "\n\nCongratulations! You have unlocked Station 2!";
        }

        new AlertDialog.Builder(requireContext())
                .setTitle("Congratulations!")
                .setMessage(message)
                .setPositiveButton("Play Again", (dialog, which) -> startGame())
                .setNegativeButton("Exit", (dialog, which) -> requireActivity().finish())
                .setCancelable(false)
                .show();
    }

    private static class Trip {
        private final List<String> items;
        private final List<String> keywords;

        public Trip(List<String> items, List<String> keywords) {
            this.items = items;
            this.keywords = keywords;
        }

        public List<String> getItems() { return items; }
        public List<String> getKeywords() { return keywords; }
    }
}
