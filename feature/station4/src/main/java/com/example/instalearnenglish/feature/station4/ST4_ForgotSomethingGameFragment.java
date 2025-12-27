package com.example.instalearnenglish.feature.station4;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

public class ST4_ForgotSomethingGameFragment extends Fragment {

    private LinearLayout memorizeContainer, choiceContainer, optionsButtonsContainer, itemsGrid;
    private TextView tvProgress;

    private List<GameRound> roundList;
    private int currentRoundIndex = 0;
    private int score = 0;
    private final Handler handler = new Handler(Looper.getMainLooper());

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.st4_fragment_forgot_something_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        memorizeContainer = view.findViewById(R.id.memorize_container);
        choiceContainer = view.findViewById(R.id.choice_container);
        optionsButtonsContainer = view.findViewById(R.id.options_buttons_container);
        itemsGrid = view.findViewById(R.id.items_grid);
        tvProgress = view.findViewById(R.id.tv_progress);
        view.findViewById(R.id.btn_back).setOnClickListener(v -> requireActivity().finish());

        loadRounds();
        startGame();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null);
    }

    private void loadRounds() {
        roundList = new ArrayList<>();
        roundList.add(new GameRound(Arrays.asList(
                new GameItem("passport", R.drawable.passport),
                new GameItem("ticket", R.drawable.planeticket),
                new GameItem("sunglasses", R.drawable.sunglasses)
        ), new GameItem("phone", R.drawable.phone)));

        roundList.add(new GameRound(Arrays.asList(
                new GameItem("camera", R.drawable.camera),
                new GameItem("map", R.drawable.map1),
                new GameItem("water bottle", R.drawable.waterbottle)
        ), new GameItem("hat", R.drawable.hat)));

        roundList.add(new GameRound(Arrays.asList(
                new GameItem("toothbrush", R.drawable.toothbrush),
                new GameItem("towel", R.drawable.towel),
                new GameItem("soap", R.drawable.soap)
        ), new GameItem("shampoo", R.drawable.shampoo)));

        roundList.add(new GameRound(Arrays.asList(
                new GameItem("laptop", R.drawable.laptop),
                new GameItem("charger", R.drawable.charger),
                new GameItem("notebook", R.drawable.notebook)
        ), new GameItem("mouse", R.drawable.mouse)));

        roundList.add(new GameRound(Arrays.asList(
                new GameItem("sunscreen", R.drawable.sunscreen),
                new GameItem("book", R.drawable.book),
                new GameItem("headphones", R.drawable.headphone)
        ), new GameItem("pajamas", R.drawable.pajamas)));

        roundList.add(new GameRound(Arrays.asList(
                new GameItem("wallet", R.drawable.wallet),
                new GameItem("keys", R.drawable.key),
                new GameItem("mask", R.drawable.mask)
        ), new GameItem("hand sanitizer", R.drawable.hand_sanitizer)));

        roundList.add(new GameRound(Arrays.asList(
                new GameItem("shirt", R.drawable.shirt),
                new GameItem("trousers", R.drawable.trouser),
                new GameItem("socks", R.drawable.socks)
        ), new GameItem("shoes", R.drawable.shoes)));

        roundList.add(new GameRound(Arrays.asList(
                new GameItem("umbrella", R.drawable.umbrella),
                new GameItem("raincoat", R.drawable.raincoat),
                new GameItem("boots", R.drawable.boots)
        ), new GameItem("scarf", R.drawable.scarf)));

        roundList.add(new GameRound(Arrays.asList(
                new GameItem("snacks", R.drawable.snacks),
                new GameItem("first-aid kit", R.drawable.first_aid),
                new GameItem("tissues", R.drawable.tissue)
        ), new GameItem("wipes", R.drawable.wipe)));

        roundList.add(new GameRound(Arrays.asList(
                new GameItem("pen", R.drawable.pen),
                new GameItem("diary", R.drawable.diary),
                new GameItem("reading glasses", R.drawable.readingglasses)
        ), new GameItem("bookmark", R.drawable.bookmark)));
    }

    private void startGame() {
        currentRoundIndex = 0;
        score = 0;
        Collections.shuffle(roundList);
        displayRound();
    }

    private void displayRound() {
        if (!isAdded() || currentRoundIndex >= roundList.size()) {
            updateGameProgress();
            return;
        }
        tvProgress.setText("Round: " + (currentRoundIndex + 1) + "/" + roundList.size());
        choiceContainer.setVisibility(View.GONE);
        memorizeContainer.setVisibility(View.VISIBLE);

        GameRound currentRound = roundList.get(currentRoundIndex);
        itemsGrid.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (GameItem item : currentRound.getShownItems()) {
            ImageView itemView = (ImageView) inflater.inflate(R.layout.template_trip_item, itemsGrid, false);
            itemView.setImageResource(item.getImageResId());
            itemsGrid.addView(itemView);
        }

        handler.postDelayed(this::showChoiceScreen, 5000);
    }

    private void showChoiceScreen() {
        if (!isAdded()) return;
        memorizeContainer.setVisibility(View.GONE);
        choiceContainer.setVisibility(View.VISIBLE);

        GameRound currentRound = roundList.get(currentRoundIndex);
        List<GameItem> options = new ArrayList<>(currentRound.getShownItems());
        options.add(currentRound.getMissingItem());
        Collections.shuffle(options);

        optionsButtonsContainer.removeAllViews();
        for (GameItem option : options) {
            Button optionButton = new Button(getContext());
            optionButton.setText(option.getName());
            optionButton.setOnClickListener(v -> checkAnswer(option.getName()));
            optionsButtonsContainer.addView(optionButton);
        }
    }

    private void checkAnswer(String selectedAnswer) {
        if (!isAdded()) return;
        for (int i = 0; i < optionsButtonsContainer.getChildCount(); i++) {
            optionsButtonsContainer.getChildAt(i).setEnabled(false);
        }

        GameRound currentRound = roundList.get(currentRoundIndex);
        boolean isCorrect = selectedAnswer.equals(currentRound.getMissingItem().getName());

        if (isCorrect) {
            score++;
            playSoundAndShowToast(true, "Correct!");
        } else {
            playSoundAndShowToast(false, "Wrong!");
        }

        handler.postDelayed(() -> {
            if (!isAdded()) return;
            currentRoundIndex++;
            displayRound();
        }, 1500);
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

        userDocRef.update("station4_completed_games", FieldValue.arrayUnion("FORGOT_SOMETHING"))
                .addOnSuccessListener(aVoid -> {
                    if (!isAdded()) return;
                    userDocRef.get().addOnSuccessListener(documentSnapshot -> {
                        if (!isAdded()) return;
                        if (documentSnapshot.exists()) {
                            List<String> completedGames = (List<String>) documentSnapshot.get("station4_completed_games");
                            if (completedGames != null && completedGames.size() >= 7) {
                                if (documentSnapshot.getLong("currentLevel") == 4L) {
                                    userDocRef.update("currentLevel", 5L).addOnSuccessListener(aVoid1 -> showFinalScore(true));
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
        String message = "Your score: " + score + "/" + roundList.size();
        if (justUnlocked) {
            message += "\n\nCongratulations! You have unlocked Station 5!";
        }

        new AlertDialog.Builder(requireContext())
                .setTitle("Game Over!")
                .setMessage(message)
                .setPositiveButton("Play Again", (dialog, which) -> startGame())
                .setNegativeButton("Exit", (dialog, which) -> requireActivity().finish())
                .setCancelable(false)
                .show();
    }

    private static class GameItem {
        private final String name;
        private final int imageResId;
        public GameItem(String name, int imageResId) { this.name = name; this.imageResId = imageResId; }
        public String getName() { return name; }
        public int getImageResId() { return imageResId; }
    }

    private static class GameRound {
        private final List<GameItem> shownItems;
        private final GameItem missingItem;
        public GameRound(List<GameItem> shownItems, GameItem missingItem) { this.shownItems = shownItems; this.missingItem = missingItem; }
        public List<GameItem> getShownItems() { return shownItems; }
        public GameItem getMissingItem() { return missingItem; }
    }
}
