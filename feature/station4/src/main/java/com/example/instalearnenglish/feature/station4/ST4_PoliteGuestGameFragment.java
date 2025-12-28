package com.example.instalearnenglish.feature.station4;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.instalearnenglish.feature.station4.utils.ST4_GameHistoryManager;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ST4_PoliteGuestGameFragment extends Fragment {

    private TextView tvSituation, tvRoundCount;
    private FlexboxLayout flexboxAnswer, flexboxWords;
    private Button btnCheckAnswer;
    private ProgressBar progressBar;

    private List<String> shuffledWords;
    private final List<String> userWords = new ArrayList<>();

    private int currentRound = 0;

    private static class GameRound {
        String situation;
        String fullSentence;
        GameRound(String situation, String fullSentence) {
            this.situation = situation;
            this.fullSentence = fullSentence;
        }
    }

    private final List<GameRound> rounds = Arrays.asList(
        new GameRound("Ask for a wake-up call at 7 AM", "Could I schedule a wake-up call for 7 AM please ?"),
        new GameRound("Ask for an extra towel", "May I have an extra towel please ?"),
        new GameRound("Check-in with a reservation", "I have a reservation under the name of Nguyen ."),
        new GameRound("Ask for a quiet room", "Could I have a room away from the elevator ?"),
        new GameRound("Ask if breakfast is free", "Is breakfast complimentary in this hotel ?")
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.st4_fragment_polite_guest_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        tvSituation = view.findViewById(R.id.tv_situation);
        tvRoundCount = view.findViewById(R.id.tv_round_count);
        flexboxAnswer = view.findViewById(R.id.flexbox_answer);
        flexboxWords = view.findViewById(R.id.flexbox_words);
        btnCheckAnswer = view.findViewById(R.id.btn_check_answer);
        progressBar = view.findViewById(R.id.progress_bar);

        progressBar.setMax(rounds.size());
        startNewRound();
        btnCheckAnswer.setOnClickListener(v -> checkAnswer());
    }

    private void startNewRound() {
        if (currentRound >= rounds.size()) {
            showGameComplete();
            return;
        }

        String roundText = "Round " + (currentRound + 1) + "/" + rounds.size();
        tvRoundCount.setText(roundText);
        progressBar.setProgress(currentRound);

        GameRound round = rounds.get(currentRound);
        tvSituation.setText(round.situation);
        
        if (getView() != null && getContext() != null) {
            View card = getView().findViewById(R.id.card_situation);
            if (card != null) {
                Animation anim = AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_in_left);
                card.startAnimation(anim);
            }
        }

        List<String> currentSentence = new ArrayList<>(Arrays.asList(round.fullSentence.split(" ")));
        shuffledWords = new ArrayList<>(currentSentence);
        Collections.shuffle(shuffledWords);
        userWords.clear();

        updateUI();
    }

    private void updateUI() {
        if (!isAdded()) return;
        flexboxAnswer.removeAllViews();
        flexboxWords.removeAllViews();

        for (int i = 0; i < userWords.size(); i++) {
            final String word = userWords.get(i);
            final int index = i;
            MaterialButton btn = createWordButton(word, true);
            btn.setOnClickListener(v -> {
                userWords.remove(index);
                shuffledWords.add(word);
                updateUI();
            });
            flexboxAnswer.addView(btn);
        }

        for (int i = 0; i < shuffledWords.size(); i++) {
            final String word = shuffledWords.get(i);
            final int index = i;
            MaterialButton btn = createWordButton(word, false);
            btn.setOnClickListener(v -> {
                shuffledWords.remove(index);
                userWords.add(word);
                updateUI();
            });
            flexboxWords.addView(btn);
        }
    }

    private MaterialButton createWordButton(String text, boolean isSelected) {
        MaterialButton btn = new MaterialButton(requireContext(), null, com.google.android.material.R.attr.materialButtonStyle);
        btn.setText(text);
        btn.setAllCaps(false);
        btn.setCornerRadius(30);
        
        FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        int margin = (int) (6 * getResources().getDisplayMetrics().density);
        params.setMargins(margin, margin, margin, margin);
        btn.setLayoutParams(params);
        
        if (isSelected) {
            btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            btn.setTextColor(Color.parseColor("#9C27B0"));
        } else {
            btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#33FFFFFF")));
            btn.setTextColor(Color.WHITE);
            btn.setStrokeWidth(2);
            btn.setStrokeColor(ColorStateList.valueOf(Color.WHITE));
        }
        
        return btn;
    }

    private void checkAnswer() {
        StringBuilder userSentence = new StringBuilder();
        for (String w : userWords) {
            userSentence.append(w).append(" ");
        }

        String target = rounds.get(currentRound).fullSentence.trim().replaceAll("\\s+", " ");
        String current = userSentence.toString().trim().replaceAll("\\s+", " ");

        if (target.equalsIgnoreCase(current)) {
            handleSuccess();
        } else {
            handleError();
        }
    }

    private void handleSuccess() {
        Toast.makeText(getContext(), "Correct! Well done!", Toast.LENGTH_SHORT).show();
        btnCheckAnswer.setEnabled(false);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            btnCheckAnswer.setEnabled(true);
            currentRound++;
            startNewRound();
        }, 1500);
    }

    private void handleError() {
        if (getView() != null && getContext() != null) {
            View answerArea = getView().findViewById(R.id.flexbox_answer);
            if (answerArea != null) {
                Animation shake = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);
                answerArea.startAnimation(shake);
            }
        }
        Toast.makeText(getContext(), "Not quite right. Try again!", Toast.LENGTH_SHORT).show();
    }

    private void showGameComplete() {
        progressBar.setProgress(rounds.size());
        tvRoundCount.setText("Complete!");
        
        // Lưu lịch sử game
        ST4_GameHistoryManager.saveResult(getContext(), "The Polite Guest", "Completed " + rounds.size() + "/" + rounds.size());
        
        Toast.makeText(getContext(), "Congratulations! You are a polite guest!", Toast.LENGTH_LONG).show();
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (getActivity() != null) getActivity().finish();
        }, 1500);
    }
}
