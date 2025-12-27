package com.example.instalearnenglish.feature.station1;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

public class ST1_WordImageMatchGameFragment extends Fragment implements View.OnClickListener {

    private TextView tvProgress, tvWordToMatch;
    private ImageView ivOption1, ivOption2, ivOption3;
    private ProgressBar progressTimer;
    private List<ImageView> optionImageViews;

    private List<MatchRound> roundList;
    private int currentRoundIndex = 0;
    private int score = 0;

    private CountDownTimer countDownTimer;
    private static final long TIME_LIMIT = 10000; // 10 seconds

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.st1_fragment_word_image_match_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvProgress = view.findViewById(R.id.tv_progress);
        tvWordToMatch = view.findViewById(R.id.tv_word_to_match);
        ivOption1 = view.findViewById(R.id.iv_option1);
        ivOption2 = view.findViewById(R.id.iv_option2);
        ivOption3 = view.findViewById(R.id.iv_option3);
        progressTimer = view.findViewById(R.id.progress_timer);

        optionImageViews = new ArrayList<>(Arrays.asList(ivOption1, ivOption2, ivOption3));

        for (ImageView iv : optionImageViews) {
            iv.setOnClickListener(this);
        }

        view.findViewById(R.id.btn_back).setOnClickListener(v -> requireActivity().finish());

        loadRounds();
        startGame();
    }

    private void startGame() {
        currentRoundIndex = 0;
        score = 0;
        Collections.shuffle(roundList);
        displayRound();
    }

    private void loadRounds() {
        roundList = new ArrayList<>();
        roundList.add(new MatchRound("passport", R.drawable.passport, R.drawable.hat, R.drawable.camera));
        roundList.add(new MatchRound("ticket", R.drawable.planeticket, R.drawable.towel, R.drawable.soap));
        roundList.add(new MatchRound("sunglasses", R.drawable.sunglasses, R.drawable.phone, R.drawable.map1));
        roundList.add(new MatchRound("hat", R.drawable.hat, R.drawable.waterbottle, R.drawable.notebook));
        roundList.add(new MatchRound("camera", R.drawable.camera, R.drawable.charger, R.drawable.mouse));
        roundList.add(new MatchRound("map", R.drawable.map1, R.drawable.sunscreen, R.drawable.book));
        roundList.add(new MatchRound("water bottle", R.drawable.waterbottle, R.drawable.headphone, R.drawable.pajamas));
        roundList.add(new MatchRound("toothbrush", R.drawable.toothbrush, R.drawable.wallet, R.drawable.key));
        roundList.add(new MatchRound("shampoo", R.drawable.shampoo, R.drawable.mask, R.drawable.shirt));
        roundList.add(new MatchRound("laptop", R.drawable.laptop, R.drawable.trouser, R.drawable.socks));
        roundList.add(new MatchRound("charger", R.drawable.charger, R.drawable.shoes, R.drawable.umbrella));
        roundList.add(new MatchRound("notebook", R.drawable.notebook, R.drawable.raincoat, R.drawable.boots));
        roundList.add(new MatchRound("book", R.drawable.book, R.drawable.scarf, R.drawable.snacks));
        roundList.add(new MatchRound("headphones", R.drawable.headphone, R.drawable.first_aid, R.drawable.tissue));
        roundList.add(new MatchRound("wallet", R.drawable.wallet, R.drawable.wipe, R.drawable.pen));
    }

    private void displayRound() {
        if (currentRoundIndex >= roundList.size()) {
            showFinalScore();
            return;
        }

        resetImageViews();
        MatchRound currentRound = roundList.get(currentRoundIndex);
        tvProgress.setText((currentRoundIndex + 1) + "/" + roundList.size());
        tvWordToMatch.setText(currentRound.getWord());

        List<Integer> imageOptions = currentRound.getShuffledImages();
        for (int i = 0; i < optionImageViews.size(); i++) {
            ImageView imageView = optionImageViews.get(i);
            imageView.setImageResource(imageOptions.get(i));
            imageView.setTag(imageOptions.get(i));
            imageView.setClickable(true);
        }
        startTimer();
    }

    private void startTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        progressTimer.setProgress(progressTimer.getMax());
        countDownTimer = new CountDownTimer(TIME_LIMIT, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                progressTimer.setProgress((int) (millisUntilFinished / 100));
            }

            @Override
            public void onFinish() {
                handleTimeout();
            }
        }.start();
    }

    private void handleTimeout() {
        for (ImageView iv : optionImageViews) {
            iv.setClickable(false);
        }
        Toast.makeText(getContext(), "Time\'s up!", Toast.LENGTH_SHORT).show();
        moveToNextRound();
    }

    @Override
    public void onClick(View v) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        for (ImageView iv : optionImageViews) {
            iv.setClickable(false);
        }

        MatchRound currentRound = roundList.get(currentRoundIndex);
        int selectedImageResId = (int) v.getTag();

        if (selectedImageResId == currentRound.getCorrectImageResId()) {
            score++;
            v.setBackgroundColor(Color.GREEN);
            Toast.makeText(getContext(), "Correct!", Toast.LENGTH_SHORT).show();
        } else {
            v.setBackgroundColor(Color.RED);
            Toast.makeText(getContext(), "Wrong!", Toast.LENGTH_SHORT).show();
        }
        moveToNextRound();
    }

    private void moveToNextRound() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            currentRoundIndex++;
            displayRound();
        }, 1500);
    }

    private void resetImageViews() {
        for (ImageView iv : optionImageViews) {
            iv.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    private void showFinalScore() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Game Over!")
                .setMessage("Your score: " + score + "/" + roundList.size())
                .setPositiveButton("Play Again", (dialog, which) -> startGame())
                .setNegativeButton("Exit", (dialog, which) -> requireActivity().finish())
                .setCancelable(false)
                .show();
    }

    @Override
    public void onDestroy() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        super.onDestroy();
    }

    private static class MatchRound {
        private final String word;
        private final int correctImageResId;
        private final int wrongImage1ResId;
        private final int wrongImage2ResId;

        public MatchRound(String word, int correctImage, int wrongImage1, int wrongImage2) {
            this.word = word;
            this.correctImageResId = correctImage;
            this.wrongImage1ResId = wrongImage1;
            this.wrongImage2ResId = wrongImage2;
        }

        public String getWord() { return word; }
        public int getCorrectImageResId() { return correctImageResId; }

        public List<Integer> getShuffledImages() {
            List<Integer> images = new ArrayList<>(Arrays.asList(correctImageResId, wrongImage1ResId, wrongImage2ResId));
            Collections.shuffle(images);
            return images;
        }
    }
}
