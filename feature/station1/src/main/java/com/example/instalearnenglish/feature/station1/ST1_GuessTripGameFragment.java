package com.example.instalearnenglish.feature.station1;

import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ST1_GuessTripGameFragment extends Fragment {

    private LinearLayout itemsContainer;
    private EditText etGuess;
    private Button btnGuess;
    private TextView tvTripProgress;

    private List<Trip> tripList;
    private int currentTripIndex = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.st1_fragment_guess_trip_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
        Collections.shuffle(tripList);
        displayNextTrip();
    }

    private void displayNextTrip() {
        if (currentTripIndex >= tripList.size()) {
            showCompletionDialog();
            return;
        }

        Trip currentTrip = tripList.get(currentTripIndex);
        tvTripProgress.setText("Trip: " + (currentTripIndex + 1) + "/" + tripList.size());
        
        itemsContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (String item : currentTrip.getItems()) {
            TextView itemView = (TextView) inflater.inflate(R.layout.template_trip_item, itemsContainer, false);
            itemView.setText("• " + item);
            itemsContainer.addView(itemView);
        }
        etGuess.setText("");
    }

    private void checkAnswer() {
        if (currentTripIndex >= tripList.size()) return; 

        String userAnswer = etGuess.getText().toString().toLowerCase().trim();
        if (userAnswer.isEmpty()) {
            Toast.makeText(getContext(), "Please type your guess!", Toast.LENGTH_SHORT).show();
            return;
        }

        Trip currentTrip = tripList.get(currentTripIndex);
        boolean isCorrect = false;
        for (String keyword : currentTrip.getKeywords()) {
            if (userAnswer.contains(keyword)) {
                isCorrect = true;
                break;
            }
        }

        if (isCorrect) {
            Toast.makeText(getContext(), "Correct! That's a good guess.", Toast.LENGTH_LONG).show();
            currentTripIndex++;
            btnGuess.postDelayed(this::displayNextTrip, 1500);
        } else {
            Toast.makeText(getContext(), "Not quite. Try another guess!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showCompletionDialog() {
        new AlertDialog.Builder(requireContext())
            .setTitle("Congratulations!")
            .setMessage("You have completed all the trips!")
            .setPositiveButton("Play Again", (dialog, which) -> startGame())
            .setNegativeButton("Exit", (dialog, which) -> requireActivity().finish())
            .setCancelable(false)
            .show();
    }

    // Inner class for Trip model
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
