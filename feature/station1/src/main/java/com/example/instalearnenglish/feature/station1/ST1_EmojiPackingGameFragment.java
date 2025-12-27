package com.example.instalearnenglish.feature.station1;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognizerIntent;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
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
import java.util.Locale;

public class ST1_EmojiPackingGameFragment extends Fragment {

    private TextView tvProgress, tvEmojiChallenge, tvRecognizedText;
    private ImageButton btnMic, btnBack;
    private Button btnNext;

    private List<EmojiChallenge> challengeList;
    private int currentChallengeIndex = 0;
    private int score = 0;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private final ActivityResultLauncher<Intent> speechResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (!isAdded()) return;
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    ArrayList<String> results = result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (results != null && !results.isEmpty()) {
                        String spokenText = results.get(0);
                        checkAnswer(spokenText);
                    }
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.st1_fragment_emoji_packing_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        tvProgress = view.findViewById(R.id.tv_progress);
        tvEmojiChallenge = view.findViewById(R.id.tv_emoji_challenge);
        tvRecognizedText = view.findViewById(R.id.tv_recognized_text);
        btnMic = view.findViewById(R.id.btn_mic);
        btnBack = view.findViewById(R.id.btn_back);
        btnNext = view.findViewById(R.id.btn_next);

        btnMic.setOnClickListener(v -> startSpeechToText());
        btnBack.setOnClickListener(v -> requireActivity().finish());
        btnNext.setOnClickListener(v -> {
            currentChallengeIndex++;
            displayChallenge();
        });

        startGame();
    }

    private void startGame() {
        currentChallengeIndex = 0;
        score = 0;
        loadChallenges();
        displayChallenge();
    }

    private void loadChallenges() {
        challengeList = new ArrayList<>();
        challengeList.add(new EmojiChallenge("🧴👕📄🔌", Arrays.asList("toiletries", "clothes", "documents", "electronics")));
        challengeList.add(new EmojiChallenge("PASSPORT✈️🏨🔑", Arrays.asList("passport", "ticket", "hotel", "key")));
        challengeList.add(new EmojiChallenge("🕶️☀️🧴👒", Arrays.asList("sunglasses", "sunscreen", "lotion", "hat")));
        challengeList.add(new EmojiChallenge("🧥🧤🧣❄️", Arrays.asList("coat", "gloves", "scarf", "snow")));
        challengeList.add(new EmojiChallenge("📷🗺️👟🎒", Arrays.asList("camera", "map", "shoes", "backpack")));
        challengeList.add(new EmojiChallenge("💊🩹🌡️🤧", Arrays.asList("medicine", "band-aid", "thermometer", "tissues")));
        challengeList.add(new EmojiChallenge("💻📓🖊️☕", Arrays.asList("laptop", "notebook", "pen", "coffee")));
        challengeList.add(new EmojiChallenge("🎧🎶📖😴", Arrays.asList("headphones", "music", "book", "sleep")));
        challengeList.add(new EmojiChallenge("💳💵📱⌚", Arrays.asList("card", "money", "phone", "watch")));
        challengeList.add(new EmojiChallenge("🪥🧼🧻🚿", Arrays.asList("toothbrush", "soap", "toilet paper", "shower")));
        Collections.shuffle(challengeList);
    }

    private void displayChallenge() {
        if (!isAdded()) return;
        if (currentChallengeIndex >= challengeList.size()) {
            updateGameProgress();
            return;
        }

        btnMic.setEnabled(true);
        btnNext.setVisibility(View.INVISIBLE);
        tvRecognizedText.setText("Speak your sentence...");

        EmojiChallenge currentChallenge = challengeList.get(currentChallengeIndex);
        tvProgress.setText("Challenge: " + (currentChallengeIndex + 1) + "/" + challengeList.size());
        tvEmojiChallenge.setText(currentChallenge.getEmojiString());
    }

    private void startSpeechToText() {
        if (!isAdded()) return;
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en-US");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say what you pack!");
        try {
            speechResultLauncher.launch(intent);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Speech recognition is not available on this device.", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkAnswer(String spokenText) {
        if (!isAdded()) return;
        btnMic.setEnabled(false);

        EmojiChallenge currentChallenge = challengeList.get(currentChallengeIndex);
        String spokenTextLower = spokenText.toLowerCase();
        int keywordsFound = 0;

        SpannableString spannable = new SpannableString(spokenText);

        for (String keyword : currentChallenge.getKeywords()) {
            if (spokenTextLower.contains(keyword)) {
                keywordsFound++;
                int startIndex = spokenTextLower.indexOf(keyword);
                int endIndex = startIndex + keyword.length();
                spannable.setSpan(new ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.green_correct)), startIndex, endIndex, 0);
            }
        }

        tvRecognizedText.setText(spannable);

        boolean isCorrect = keywordsFound == currentChallenge.getKeywords().size();

        if (isCorrect) {
            score++;
            playSoundAndShowToast(true, "Perfect!");
        } else {
            playSoundAndShowToast(false, "You missed some items!");
        }

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if(isAdded()) btnNext.setVisibility(View.VISIBLE);
        }, 1000);
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

        userDocRef.update("station1_completed_games", FieldValue.arrayUnion("EMOJI_PACKING"))
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
        String message = "Your score: " + score + "/" + challengeList.size();
        if (justUnlocked) {
            message += "\n\nCongratulations! You have unlocked Station 2!";
        }

        new AlertDialog.Builder(requireContext())
                .setTitle("Challenge Completed!")
                .setMessage(message)
                .setPositiveButton("Play Again", (dialog, which) -> startGame())
                .setNegativeButton("Exit", (dialog, which) -> requireActivity().finish())
                .setCancelable(false)
                .show();
    }

    private static class EmojiChallenge {
        private final String emojiString;
        private final List<String> keywords;

        public EmojiChallenge(String emojiString, List<String> keywords) {
            this.emojiString = emojiString;
            this.keywords = keywords;
        }

        public String getEmojiString() { return emojiString; }
        public List<String> getKeywords() { return keywords; }
    }
}
