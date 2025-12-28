package com.example.instalearnenglish.feature.station23;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.instalearnenglish.feature.station23.adapter.St3GameMenuAdapter;
import com.example.instalearnenglish.feature.station23.adapter.St3MatchAdapter;
import com.example.instalearnenglish.feature.station23.model.GameMenuItem;
import com.example.instalearnenglish.feature.station23.model.MatchGameDataProvider;
import com.example.instalearnenglish.feature.station23.model.QuizDataProvider;
import com.example.instalearnenglish.feature.station23.model.QuizQuestion;
import com.example.instalearnenglish.feature.station23.model.ScrambleQuestion;
import com.example.instalearnenglish.feature.station23.model.ScrambleQuestionProvider;
import com.example.instalearnenglish.feature.station23.model.St23VocabItem;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class St3GameHubFragment extends Fragment implements St3GameMenuAdapter.OnGameClickListener {

    // Main Containers
    private View menuContainer, quizGameContainer, matchGameContainer, scrambleGameContainer;
    private RecyclerView rvGameMenu;

    // --- Quiz Game ---
    private TextView tvQuizQuestion, tvQuizTimer;
    private Button btnQuizAnswerA, btnQuizAnswerB;
    private LottieAnimationView lottieQuizCheckmark, lottieQuizFailed;
    private List<QuizQuestion> quizQuestions;
    private int currentQuestionIndex = 0;
    private CountDownTimer quizCountDownTimer;
    private static final int QUIZ_QUESTION_DURATION = 10000;

    // --- Match Game ---
    private TextView tvMatchTimer;
    private RecyclerView rvEnglish, rvVietnamese;
    private St3MatchAdapter englishAdapter, vietnameseAdapter;
    private List<St23VocabItem> originalVocabList;
    private List<String> englishWords = new ArrayList<>();
    private List<String> vietnameseWords = new ArrayList<>();
    private String selectedEnglishWord, selectedVietnameseWord;
    private int selectedEnglishPos = -1, selectedVietnamesePos = -1;
    private LottieAnimationView lottieMatchCheckmark, lottieMatchFailed;
    private CountDownTimer matchCountDownTimer;
    private long matchTimeRemaining = 0;
    private static final int MATCH_GAME_DURATION = 45000;

    // --- Scramble Game ---
    private TextView tvSentenceVi, tvScrambleTimer;
    private FlexboxLayout flexboxAnswer, flexboxChoices;
    private Button btnScrambleCheck;
    private List<ScrambleQuestion> scrambleQuestions;
    private int currentScrambleIndex = 0;
    private LottieAnimationView lottieScrambleCheckmark, lottieScrambleFailed;
    private CountDownTimer scrambleCountDownTimer;
    private static final int SCRAMBLE_QUESTION_DURATION = 20000;

    // --- Sound related ---
    private MediaPlayer backgroundMusicPlayer;
    private SoundPool soundPool;
    private int rightAnswerSoundId, wrongAnswerSoundId;
    private boolean soundsLoaded = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.st3_fragment_main_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        menuContainer = view.findViewById(R.id.st3_game_menu_container);
        quizGameContainer = view.findViewById(R.id.st3_quiz_game_layout);
        matchGameContainer = view.findViewById(R.id.st3_match_game_layout);
        scrambleGameContainer = view.findViewById(R.id.st3_scramble_game_container);

        setupNewGameMenu();
        setupSounds();
        showMenu();
    }

    private void setupNewGameMenu() {
        rvGameMenu = menuContainer.findViewById(R.id.st3_rv_game_menu);
        rvGameMenu.setLayoutManager(new GridLayoutManager(getContext(), 2));

        List<GameMenuItem> gameList = new ArrayList<>();
        gameList.add(new GameMenuItem("Trắc nghiệm nhanh", R.raw.animation_quiz, null));
        gameList.add(new GameMenuItem("Nối từ siêu tốc", R.raw.animation_word_match, null));
        gameList.add(new GameMenuItem("Sắp xếp câu", R.raw.animation_sentence_scramble, null));

        St3GameMenuAdapter menuAdapter = new St3GameMenuAdapter(getContext(), gameList, this);
        rvGameMenu.setAdapter(menuAdapter);
    }

    @Override
    public void onGameClick(GameMenuItem game) {
        if (game.getTitle().contains("Trắc nghiệm")) {
            showQuizGame();
        } else if (game.getTitle().contains("Nối từ")) {
            showMatchGame();
        } else if (game.getTitle().contains("Sắp xếp")) {
            showScrambleGame();
        }
    }
    
    private void setupSounds() {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(2)
                .setAudioAttributes(audioAttributes)
                .build();
        soundPool.setOnLoadCompleteListener((pool, sampleId, status) -> soundsLoaded = (status == 0));
        rightAnswerSoundId = soundPool.load(getContext(), R.raw.right_answer, 1);
        wrongAnswerSoundId = soundPool.load(getContext(), R.raw.wrong_answer, 1);
    }

    private void showMenu() {
        menuContainer.setVisibility(View.VISIBLE);
        quizGameContainer.setVisibility(View.GONE);
        matchGameContainer.setVisibility(View.GONE);
        scrambleGameContainer.setVisibility(View.GONE);
        stopBackgroundMusic();
        if (quizCountDownTimer != null) quizCountDownTimer.cancel();
        if (matchCountDownTimer != null) matchCountDownTimer.cancel();
        if (scrambleCountDownTimer != null) scrambleCountDownTimer.cancel();
    }

    private void showQuizGame() {
        menuContainer.setVisibility(View.GONE);
        matchGameContainer.setVisibility(View.GONE);
        scrambleGameContainer.setVisibility(View.GONE);
        quizGameContainer.setVisibility(View.VISIBLE);
        if (matchCountDownTimer != null) matchCountDownTimer.cancel();
        if (scrambleCountDownTimer != null) scrambleCountDownTimer.cancel();

        if (((ViewGroup) quizGameContainer).getChildCount() == 0) getLayoutInflater().inflate(R.layout.st3_fragment_game_quiz, (ViewGroup) quizGameContainer, true);
        setupQuizGame(quizGameContainer);
        currentQuestionIndex = 0;
        loadQuizData();
        startBackgroundMusic(R.raw.gameplay3_1);
        displayCurrentQuestion();
    }

    private void showMatchGame() {
        menuContainer.setVisibility(View.GONE);
        quizGameContainer.setVisibility(View.GONE);
        scrambleGameContainer.setVisibility(View.GONE);
        matchGameContainer.setVisibility(View.VISIBLE);
        if (quizCountDownTimer != null) quizCountDownTimer.cancel();
        if (scrambleCountDownTimer != null) scrambleCountDownTimer.cancel();

        if (((ViewGroup) matchGameContainer).getChildCount() == 0) getLayoutInflater().inflate(R.layout.st3_fragment_minigame_match, (ViewGroup) matchGameContainer, true);
        setupMatchGame(matchGameContainer);
        loadAndPrepareMatchData();
        startBackgroundMusic(R.raw.gameplay3_3);
    }

    private void showScrambleGame() {
        menuContainer.setVisibility(View.GONE);
        quizGameContainer.setVisibility(View.GONE);
        matchGameContainer.setVisibility(View.GONE);
        scrambleGameContainer.setVisibility(View.VISIBLE);
        if (quizCountDownTimer != null) quizCountDownTimer.cancel();
        if (matchCountDownTimer != null) matchCountDownTimer.cancel();

        if (((ViewGroup) scrambleGameContainer).getChildCount() == 0) getLayoutInflater().inflate(R.layout.st3_fragment_game_scramble, (ViewGroup) scrambleGameContainer, true);
        setupScrambleGame(scrambleGameContainer);
        startScrambleGame();
        startBackgroundMusic(R.raw.gameplay3_4);
    }

    private void setupQuizGame(View quizView) {
        tvQuizQuestion = quizView.findViewById(R.id.st3_tv_quiz_question);
        tvQuizTimer = quizView.findViewById(R.id.st3_tv_quiz_timer);
        btnQuizAnswerA = quizView.findViewById(R.id.st3_btn_answer_a);
        btnQuizAnswerB = quizView.findViewById(R.id.st3_btn_answer_b);
        lottieQuizCheckmark = quizView.findViewById(R.id.st3_quiz_lottie_checkmark);
        lottieQuizFailed = quizView.findViewById(R.id.st3_quiz_lottie_failed);
        setupFeedbackAnimations(lottieQuizCheckmark, lottieQuizFailed);
        setupQuizGameListeners();
    }

    private void loadQuizData() {
        quizQuestions = QuizDataProvider.getStation3QuizQuestions();
        Collections.shuffle(quizQuestions);
    }

    private void displayCurrentQuestion() {
        if (quizCountDownTimer != null) quizCountDownTimer.cancel();
        if (quizQuestions != null && currentQuestionIndex < quizQuestions.size()) {
            QuizQuestion q = quizQuestions.get(currentQuestionIndex);
            tvQuizQuestion.setText(q.getQuestionText());
            btnQuizAnswerA.setText(q.getOptionA());
            btnQuizAnswerB.setText(q.getOptionB());
            btnQuizAnswerA.setEnabled(true);
            btnQuizAnswerB.setEnabled(true);
            startQuizTimer();
        } else {
            Toast.makeText(getContext(), "Chúc mừng! Bạn đã hoàn thành!", Toast.LENGTH_SHORT).show();
            showMenu();
        }
    }

    private void startQuizTimer() {
        quizCountDownTimer = new CountDownTimer(QUIZ_QUESTION_DURATION, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (isAdded() && tvQuizTimer != null) tvQuizTimer.setText(String.valueOf(millisUntilFinished / 1000));
            }
            @Override
            public void onFinish() {
                if (isAdded()) handleQuizAnswer(false);
            }
        }.start();
    }

    private void setupQuizGameListeners() {
        btnQuizAnswerA.setOnClickListener(v -> {
            if (quizQuestions.size() > currentQuestionIndex) handleQuizAnswer(!quizQuestions.get(currentQuestionIndex).isOptionBCorrect());
        });
        btnQuizAnswerB.setOnClickListener(v -> {
            if (quizQuestions.size() > currentQuestionIndex) handleQuizAnswer(quizQuestions.get(currentQuestionIndex).isOptionBCorrect());
        });
    }

    private void handleQuizAnswer(boolean isCorrect) {
        if (quizCountDownTimer != null) quizCountDownTimer.cancel();
        btnQuizAnswerA.setEnabled(false);
        btnQuizAnswerB.setEnabled(false);
        if (isCorrect) {
            if (soundsLoaded) soundPool.play(rightAnswerSoundId, 1, 1, 0, 0, 1);
            if (lottieQuizCheckmark != null) { lottieQuizCheckmark.setVisibility(View.VISIBLE); lottieQuizCheckmark.playAnimation(); }
        } else {
            if (soundsLoaded) soundPool.play(wrongAnswerSoundId, 1, 1, 0, 0, 1);
            if (lottieQuizFailed != null) { lottieQuizFailed.setVisibility(View.VISIBLE); lottieQuizFailed.playAnimation(); }
        }
        currentQuestionIndex++;
        new Handler(Looper.getMainLooper()).postDelayed(this::displayCurrentQuestion, 1500);
    }

    private void setupMatchGame(View matchView) {
        tvMatchTimer = matchView.findViewById(R.id.st3_tv_match_timer);
        rvEnglish = matchView.findViewById(R.id.st3_rv_english_words);
        rvVietnamese = matchView.findViewById(R.id.st3_rv_vietnamese_words);
        lottieMatchCheckmark = matchView.findViewById(R.id.st3_match_lottie_checkmark);
        lottieMatchFailed = matchView.findViewById(R.id.st3_match_lottie_failed);
        setupFeedbackAnimations(lottieMatchCheckmark, lottieMatchFailed);
        englishAdapter = new St3MatchAdapter(getContext(), englishWords, (word, position) -> { selectedEnglishWord = word; selectedEnglishPos = position; checkMatch(); });
        vietnameseAdapter = new St3MatchAdapter(getContext(), vietnameseWords, (word, position) -> { selectedVietnameseWord = word; selectedVietnamesePos = position; checkMatch(); });
        rvEnglish.setLayoutManager(new LinearLayoutManager(getContext()));
        rvVietnamese.setLayoutManager(new LinearLayoutManager(getContext()));
        rvEnglish.setAdapter(englishAdapter);
        rvVietnamese.setAdapter(vietnameseAdapter);
    }

    private void loadAndPrepareMatchData() {
        originalVocabList = MatchGameDataProvider.getStation3MatchData();
        englishWords.clear();
        vietnameseWords.clear();
        for (St23VocabItem item : originalVocabList) {
            englishWords.add(item.getName()); // Corrected method call
            vietnameseWords.add(item.getVietnameseMeaning());
        }
        Collections.shuffle(vietnameseWords);
        if(englishAdapter!=null) englishAdapter.notifyDataSetChanged();
        if(vietnameseAdapter!=null) vietnameseAdapter.notifyDataSetChanged();
        startMatchTimer(MATCH_GAME_DURATION);
    }

    private void startMatchTimer(long duration) {
        if (matchCountDownTimer != null) matchCountDownTimer.cancel();
        matchTimeRemaining = duration;
        matchCountDownTimer = new CountDownTimer(matchTimeRemaining, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                matchTimeRemaining = millisUntilFinished;
                if (isAdded() && tvMatchTimer != null) tvMatchTimer.setText(String.valueOf(millisUntilFinished / 1000));
            }
            @Override
            public void onFinish() {
                if (isAdded()) handleMatchGameEnd(false);
            }
        }.start();
    }

    private void checkMatch() {
        if (selectedEnglishWord == null || selectedVietnameseWord == null) return;
        boolean isMatch = false;
        for (St23VocabItem item : originalVocabList) {
            if (item.getName().equals(selectedEnglishWord) && item.getVietnameseMeaning().equals(selectedVietnameseWord)) { // Corrected method call
                isMatch = true;
                break;
            }
        }

        if (isMatch) {
            if (soundsLoaded) soundPool.play(rightAnswerSoundId, 1, 1, 0, 0, 1);
            handleCorrectMatch();
        } else {
            if (soundsLoaded) soundPool.play(wrongAnswerSoundId, 1, 1, 0, 0, 1);
            if (matchCountDownTimer != null) matchCountDownTimer.cancel();
            matchTimeRemaining -= 5000;
            if (matchTimeRemaining > 0) {
                startMatchTimer(matchTimeRemaining);
            } else {
                handleMatchGameEnd(false);
            }
            if(lottieMatchFailed != null) { lottieMatchFailed.setVisibility(View.VISIBLE); lottieMatchFailed.playAnimation(); }
        }
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            selectedEnglishWord = null;
            selectedVietnameseWord = null;
            if (englishAdapter != null) englishAdapter.clearSelection();
            if (vietnameseAdapter != null) vietnameseAdapter.clearSelection();
        }, 300);
    }

    private void handleCorrectMatch() {
        if(lottieMatchCheckmark != null) { lottieMatchCheckmark.setVisibility(View.VISIBLE); lottieMatchCheckmark.playAnimation(); }
        englishAdapter.removeItem(selectedEnglishPos);
        vietnameseAdapter.removeItem(selectedVietnamesePos);
        if (englishWords.isEmpty()) {
            handleMatchGameEnd(true);
        }
    }
    
    private void handleMatchGameEnd(boolean playerWon) {
        if (matchCountDownTimer != null) matchCountDownTimer.cancel();
        stopBackgroundMusic();
        if (playerWon) {
            Toast.makeText(getContext(), "Chúc mừng! Bạn đã hoàn thành!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), "Hết giờ!", Toast.LENGTH_LONG).show();
        }
        new Handler(Looper.getMainLooper()).postDelayed(this::showMenu, 2000);
    }

    // --- Scramble Game Logic ---
    private void setupScrambleGame(View scrambleView) {
        tvScrambleTimer = scrambleView.findViewById(R.id.st3_scramble_tv_timer);
        tvSentenceVi = scrambleView.findViewById(R.id.st3_scramble_tv_sentence_vi);
        flexboxAnswer = scrambleView.findViewById(R.id.st3_scramble_flexbox_answer);
        flexboxChoices = scrambleView.findViewById(R.id.st3_scramble_flexbox_choices);
        btnScrambleCheck = scrambleView.findViewById(R.id.st3_scramble_btn_check);
        lottieScrambleCheckmark = scrambleView.findViewById(R.id.st3_scramble_lottie_checkmark);
        lottieScrambleFailed = scrambleView.findViewById(R.id.st3_scramble_lottie_failed);
        setupFeedbackAnimations(lottieScrambleCheckmark, lottieScrambleFailed);
        btnScrambleCheck.setOnClickListener(v -> checkScrambleAnswer(true));
    }

    private void startScrambleGame() {
        scrambleQuestions = ScrambleQuestionProvider.getQuestions();
        Collections.shuffle(scrambleQuestions);
        currentScrambleIndex = 0;
        loadScrambleQuestion(currentScrambleIndex);
    }

    private void loadScrambleQuestion(int index) {
        if (scrambleCountDownTimer != null) scrambleCountDownTimer.cancel();
        if (index >= scrambleQuestions.size()) { Toast.makeText(getContext(), "Chúc mừng! Bạn đã hoàn thành!", Toast.LENGTH_SHORT).show(); showMenu(); return; }
        flexboxAnswer.removeAllViews();
        flexboxChoices.removeAllViews();
        btnScrambleCheck.setEnabled(true);
        ScrambleQuestion currentQuestion = scrambleQuestions.get(index);
        tvSentenceVi.setText(currentQuestion.getVietnameseHint());
        String[] words = currentQuestion.getCorrectSentence().split(" ");
        List<String> wordList = new ArrayList<>(java.util.Arrays.asList(words));
        Collections.shuffle(wordList);
        for (String word : wordList) {
            Chip chip = (Chip) getLayoutInflater().inflate(R.layout.st3_item_scramble_chip, flexboxChoices, false);
            chip.setText(word);
            chip.setOnClickListener(this::onChoiceChipClicked);
            flexboxChoices.addView(chip);
        }
        startScrambleTimer();
    }

    private void startScrambleTimer() {
        scrambleCountDownTimer = new CountDownTimer(SCRAMBLE_QUESTION_DURATION, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (isAdded() && tvScrambleTimer != null) tvScrambleTimer.setText(String.valueOf(millisUntilFinished / 1000));
            }
            @Override
            public void onFinish() {
                if (isAdded()) checkScrambleAnswer(false);
            }
        }.start();
    }

    private void onChoiceChipClicked(View view) { Chip chip = (Chip) view; flexboxChoices.removeView(chip); flexboxAnswer.addView(chip); chip.setOnClickListener(this::onAnswerChipClicked); }
    private void onAnswerChipClicked(View view) { Chip chip = (Chip) view; flexboxAnswer.removeView(chip); flexboxChoices.addView(chip); chip.setOnClickListener(this::onChoiceChipClicked); }

    private void checkScrambleAnswer(boolean byButton) {
        if (scrambleCountDownTimer != null) scrambleCountDownTimer.cancel();
        btnScrambleCheck.setEnabled(false);
        String correctAnswer = scrambleQuestions.get(currentScrambleIndex).getCorrectSentence();
        StringBuilder userAnswer = new StringBuilder();
        if (byButton) {
            for (int i = 0; i < flexboxAnswer.getChildCount(); i++) {
                Chip chip = (Chip) flexboxAnswer.getChildAt(i);
                userAnswer.append(chip.getText().toString());
                if (i < flexboxAnswer.getChildCount() - 1) { userAnswer.append(" "); }
            }
        }
        boolean isCorrect = byButton && userAnswer.toString().equalsIgnoreCase(correctAnswer);
        if (isCorrect) {
            if (soundsLoaded) soundPool.play(rightAnswerSoundId, 1, 1, 0, 0, 1);
            lottieScrambleCheckmark.setVisibility(View.VISIBLE);
            lottieScrambleCheckmark.playAnimation();
        } else {
            if (soundsLoaded) soundPool.play(wrongAnswerSoundId, 1, 1, 0, 0, 1);
            lottieScrambleFailed.setVisibility(View.VISIBLE);
            lottieScrambleFailed.playAnimation();
            if (byButton) flexboxAnswer.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake));
        }
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            currentScrambleIndex++;
            loadScrambleQuestion(currentScrambleIndex);
        }, 1500);
    }

    private void setupFeedbackAnimations(LottieAnimationView checkmark, LottieAnimationView failed) {
        if (checkmark != null) {
            checkmark.enableMergePathsForKitKatAndAbove(true);
            checkmark.addAnimatorListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (isAdded()) {
                        checkmark.setVisibility(View.GONE);
                    }
                }
            });
        }
        if (failed != null) {
            failed.enableMergePathsForKitKatAndAbove(true);
            failed.addAnimatorListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (isAdded()) {
                        failed.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    private void startBackgroundMusic(int resId) {
        stopBackgroundMusic();
        backgroundMusicPlayer = MediaPlayer.create(getContext(), resId);
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.setLooping(true);
            backgroundMusicPlayer.start();
        }
    }

    private void stopBackgroundMusic() {
        if (backgroundMusicPlayer != null && backgroundMusicPlayer.isPlaying()) {
            backgroundMusicPlayer.stop();
            backgroundMusicPlayer.release();
            backgroundMusicPlayer = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (quizCountDownTimer != null) quizCountDownTimer.cancel();
        if (matchCountDownTimer != null) matchCountDownTimer.cancel();
        if (scrambleCountDownTimer != null) scrambleCountDownTimer.cancel();
        stopBackgroundMusic();
        if (soundPool != null) { soundPool.release(); soundPool = null; }
    }
}
