package com.example.instalearnenglish.feature.station1;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ST1_WhatsInMyBagGameFragment extends Fragment {

    private TextView tvProgress;
    private ImageButton btnPlayAudio, btnBack;
    private EditText etGuess;
    private Button btnGuess;

    private List<MysteryItem> itemList;
    private int currentItemIndex = 0;
    private int score = 0;
    private TextToSpeech tts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        tts = new TextToSpeech(getContext(), status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.US);
            } else {
                Log.e("TTS", "Initialization Failed!");
            }
        });
        return inflater.inflate(R.layout.st1_fragment_whats_in_my_bag_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvProgress = view.findViewById(R.id.tv_progress);
        btnPlayAudio = view.findViewById(R.id.btn_play_audio);
        btnBack = view.findViewById(R.id.btn_back);
        etGuess = view.findViewById(R.id.et_guess);
        btnGuess = view.findViewById(R.id.btn_guess);

        btnBack.setOnClickListener(v -> requireActivity().finish());
        btnPlayAudio.setOnClickListener(v -> speakDescription());
        btnGuess.setOnClickListener(v -> checkAnswer());

        loadItems();
        startGame();
    }

    private void startGame() {
        currentItemIndex = 0;
        score = 0;
        Collections.shuffle(itemList);
        displayItem();
    }

    private void loadItems() {
        itemList = new ArrayList<>();
        itemList.add(new MysteryItem("It’s small. I use it every day. I need it to brush my teeth.", "toothbrush"));
        itemList.add(new MysteryItem("I use this to unlock my hotel room.", "keycard"));
        itemList.add(new MysteryItem("This protects my eyes from the sun.", "sunglasses"));
        itemList.add(new MysteryItem("I put this on my skin at the beach.", "sunscreen"));
        itemList.add(new MysteryItem("I read this on the plane.", "book"));
        itemList.add(new MysteryItem("I use this to charge my phone.", "charger"));
        itemList.add(new MysteryItem("I drink from this to stay hydrated.", "bottle"));
        itemList.add(new MysteryItem("This is an official document I need to travel to another country.", "passport"));
        itemList.add(new MysteryItem("I wear this on my feet on the sand.", "flip-flops"));
        itemList.add(new MysteryItem("I use this to take pictures.", "camera"));
    }

    private void displayItem() {
        if (currentItemIndex >= itemList.size()) {
            showFinalScore();
            return;
        }
        tvProgress.setText("Item: " + (currentItemIndex + 1) + "/" + itemList.size());
        etGuess.setText("");
        btnPlayAudio.setEnabled(true);
        btnGuess.setEnabled(true);
    }

    private void speakDescription() {
        if (tts != null) {
            String description = itemList.get(currentItemIndex).getDescription();
            tts.speak(description, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    private void checkAnswer() {
        String userAnswer = etGuess.getText().toString().trim().toLowerCase();
        String correctAnswer = itemList.get(currentItemIndex).getAnswer();

        btnPlayAudio.setEnabled(false);
        btnGuess.setEnabled(false);

        if (userAnswer.equals(correctAnswer)) {
            score++;
            Toast.makeText(getContext(), "Correct!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Wrong!", Toast.LENGTH_SHORT).show();
        }

        // Move to the next question regardless of the answer
        etGuess.postDelayed(() -> {
            currentItemIndex++;
            displayItem();
        }, 1500); // Wait 1.5 seconds
    }

    private void showFinalScore() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Game Over!")
                .setMessage("Your score: " + score + "/" + itemList.size())
                .setPositiveButton("Play Again", (dialog, which) -> startGame())
                .setNegativeButton("Exit", (dialog, which) -> requireActivity().finish())
                .setCancelable(false)
                .show();
    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    // Inner class for MysteryItem model
    private static class MysteryItem {
        private final String description;
        private final String answer;

        public MysteryItem(String description, String answer) {
            this.description = description;
            this.answer = answer;
        }

        public String getDescription() { return description; }
        public String getAnswer() { return answer; }
    }
}
