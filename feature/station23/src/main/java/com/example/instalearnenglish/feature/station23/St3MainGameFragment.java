package com.example.instalearnenglish.feature.station23;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.instalearnenglish.feature.station23.adapter.St3MatchAdapter;
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

// Removed St3GameMenuAdapter.OnGameClickListener
public class St3MainGameFragment extends Fragment {

    private View menuContainer, quizGameContainer, matchGameContainer, scrambleGameContainer;

    // --- Menu (Reverted to Buttons) ---
    private Button btnShowQuizGame, btnShowMatchGame, btnShowScrambleGame;

    // --- Quiz Game ---
    private TextView tvQuizQuestion;
    private Button btnQuizAnswerA, btnQuizAnswerB;
    private LottieAnimationView lottieQuizCheckmark, lottieQuizFailed;
    private List<QuizQuestion> quizQuestions;
    private int currentQuestionIndex = 0;

    // --- Match Game ---
    private RecyclerView rvEnglish, rvVietnamese;
    private St3MatchAdapter englishAdapter, vietnameseAdapter;
    private List<St23VocabItem> originalVocabList;
    private List<String> englishWords = new ArrayList<>();
    private List<String> vietnameseWords = new ArrayList<>();
    private String selectedEnglishWord, selectedVietnameseWord;
    private int selectedEnglishPos = -1, selectedVietnamesePos = -1;
    private int correctMatches = 0;
    private LottieAnimationView lottieMatchCheckmark, lottieMatchFailed;

    // --- Scramble Game ---
    private TextView tvSentenceVi;
    private FlexboxLayout flexboxAnswer, flexboxChoices;
    private Button btnScrambleCheck;
    private List<ScrambleQuestion> scrambleQuestions;
    private int currentScrambleIndex = 0;
    private LottieAnimationView lottieScrambleCheckmark, lottieScrambleFailed;


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

        // Reverted to original Button setup
        setupOriginalMenu();
        showMenu();
    }
    
    private void setupOriginalMenu() {
        // This assumes the buttons are inside the menuContainer
        btnShowQuizGame = menuContainer.findViewById(R.id.st3_btn_quiz_game);
        btnShowMatchGame = menuContainer.findViewById(R.id.st3_btn_match_game);
        btnShowScrambleGame = menuContainer.findViewById(R.id.st3_btn_scramble_game);

        btnShowQuizGame.setOnClickListener(v -> showQuizGame());
        btnShowMatchGame.setOnClickListener(v -> showMatchGame());
        btnShowScrambleGame.setOnClickListener(v -> showScrambleGame());
    }

    // Removed onGameClick, setupMenu, loadGameMenuItems

    private void showMenu() {
        menuContainer.setVisibility(View.VISIBLE);
        quizGameContainer.setVisibility(View.GONE);
        matchGameContainer.setVisibility(View.GONE);
        scrambleGameContainer.setVisibility(View.GONE);
    }

    private void showQuizGame() {
        menuContainer.setVisibility(View.GONE);
        matchGameContainer.setVisibility(View.GONE);
        scrambleGameContainer.setVisibility(View.GONE);
        quizGameContainer.setVisibility(View.VISIBLE);

        if (((ViewGroup) quizGameContainer).getChildCount() == 0) {
            getLayoutInflater().inflate(R.layout.st3_fragment_game_quiz, (ViewGroup) quizGameContainer, true);
        }
        setupQuizGame(quizGameContainer);
        currentQuestionIndex = 0;
        loadQuizData();
        displayCurrentQuestion();
    }

    private void showMatchGame() {
        menuContainer.setVisibility(View.GONE);
        quizGameContainer.setVisibility(View.GONE);
        scrambleGameContainer.setVisibility(View.GONE);
        matchGameContainer.setVisibility(View.VISIBLE);

        if (((ViewGroup) matchGameContainer).getChildCount() == 0) {
            getLayoutInflater().inflate(R.layout.st3_fragment_minigame_match, (ViewGroup) matchGameContainer, true);
        }
        setupMatchGame(matchGameContainer);
        loadAndPrepareMatchData();
    }

    private void showScrambleGame() {
        menuContainer.setVisibility(View.GONE);
        quizGameContainer.setVisibility(View.GONE);
        matchGameContainer.setVisibility(View.GONE);
        scrambleGameContainer.setVisibility(View.VISIBLE);

        if (((ViewGroup) scrambleGameContainer).getChildCount() == 0) {
            getLayoutInflater().inflate(R.layout.st3_fragment_game_scramble, (ViewGroup) scrambleGameContainer, true);
        }
        setupScrambleGame(scrambleGameContainer);
        startScrambleGame();
    }

    // --- All Game Logic below is preserved ---

    private void setupQuizGame(View quizView) {
        tvQuizQuestion = quizView.findViewById(R.id.st3_tv_quiz_question);
        btnQuizAnswerA = quizView.findViewById(R.id.st3_btn_answer_a);
        btnQuizAnswerB = quizView.findViewById(R.id.st3_btn_answer_b);
        lottieQuizCheckmark = quizView.findViewById(R.id.st3_quiz_lottie_checkmark);
        lottieQuizFailed = quizView.findViewById(R.id.st3_quiz_lottie_failed);
        setupFeedbackAnimations(lottieQuizCheckmark, lottieQuizFailed);
        setupQuizGameListeners();
    }

    private void loadQuizData() {
        quizQuestions = QuizDataProvider.getStation3QuizQuestions();
    }

    private void displayCurrentQuestion() {
        if (quizQuestions != null && currentQuestionIndex < quizQuestions.size()) {
            QuizQuestion q = quizQuestions.get(currentQuestionIndex);
            tvQuizQuestion.setText(q.getQuestionText());
            btnQuizAnswerA.setText(q.getOptionA());
            btnQuizAnswerB.setText(q.getOptionB());
            btnQuizAnswerA.setEnabled(true);
            btnQuizAnswerB.setEnabled(true);
        } else {
            Toast.makeText(getContext(), "Chúc mừng! Bạn đã hoàn thành!", Toast.LENGTH_SHORT).show();
            showMenu();
        }
    }

    private void setupQuizGameListeners() {
        btnQuizAnswerA.setOnClickListener(v -> handleQuizAnswer(!quizQuestions.get(currentQuestionIndex).isOptionBCorrect()));
        btnQuizAnswerB.setOnClickListener(v -> handleQuizAnswer(quizQuestions.get(currentQuestionIndex).isOptionBCorrect()));
    }

    private void handleQuizAnswer(boolean isCorrect) {
        btnQuizAnswerA.setEnabled(false);
        btnQuizAnswerB.setEnabled(false);
        if (isCorrect) {
            lottieQuizCheckmark.setVisibility(View.VISIBLE);
            lottieQuizCheckmark.playAnimation();
            currentQuestionIndex++;
            new Handler(Looper.getMainLooper()).postDelayed(this::displayCurrentQuestion, 1500);
        } else {
            lottieQuizFailed.setVisibility(View.VISIBLE);
            lottieQuizFailed.playAnimation();
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if (isAdded()) {
                    btnQuizAnswerA.setEnabled(true);
                    btnQuizAnswerB.setEnabled(true);
                }
            }, 1000);
        }
    }

    private void setupMatchGame(View matchView) {
        rvEnglish = matchView.findViewById(R.id.st3_rv_english_words);
        rvVietnamese = matchView.findViewById(R.id.st3_rv_vietnamese_words);
        lottieMatchCheckmark = matchView.findViewById(R.id.st3_match_lottie_checkmark);
        lottieMatchFailed = matchView.findViewById(R.id.st3_match_lottie_failed);
        setupFeedbackAnimations(lottieMatchCheckmark, lottieMatchFailed);

        englishAdapter = new St3MatchAdapter(getContext(), englishWords, (word, position) -> {
            selectedEnglishWord = word;
            selectedEnglishPos = position;
            checkMatch();
        });
        vietnameseAdapter = new St3MatchAdapter(getContext(), vietnameseWords, (word, position) -> {
            selectedVietnameseWord = word;
            selectedVietnamesePos = position;
            checkMatch();
        });

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
            englishWords.add(item.getEnglishWord());
            vietnameseWords.add(item.getVietnameseMeaning());
        }
        Collections.shuffle(vietnameseWords);
        correctMatches = 0;
        if(englishAdapter!=null) englishAdapter.notifyDataSetChanged();
        if(vietnameseAdapter!=null) vietnameseAdapter.notifyDataSetChanged();
    }

    private void checkMatch() {
        if (selectedEnglishWord == null || selectedVietnameseWord == null) return;
        boolean isMatch = false;
        for (St23VocabItem item : originalVocabList) {
            if (item.getEnglishWord().equals(selectedEnglishWord) && item.getVietnameseMeaning().equals(selectedVietnameseWord)) {
                isMatch = true;
                break;
            }
        }

        if (isMatch) {
            handleCorrectMatch();
        } else {
            lottieMatchFailed.setVisibility(View.VISIBLE);
            lottieMatchFailed.playAnimation();
        }

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            selectedEnglishWord = null;
            selectedVietnameseWord = null;
            if (englishAdapter != null) englishAdapter.clearSelection();
            if (vietnameseAdapter != null) vietnameseAdapter.clearSelection();
        }, 300);
    }

    private void handleCorrectMatch() {
        lottieMatchCheckmark.setVisibility(View.VISIBLE);
        lottieMatchCheckmark.playAnimation();
        correctMatches++;

        englishAdapter.removeItem(selectedEnglishPos);
        vietnameseAdapter.removeItem(selectedVietnamesePos);

        if (englishWords.isEmpty()) {
            Toast.makeText(getContext(), "Chúc mừng! Bạn đã hoàn thành!", Toast.LENGTH_LONG).show();
            new Handler(Looper.getMainLooper()).postDelayed(this::showMenu, 1500);
        }
    }

    private void setupScrambleGame(View scrambleView) {
        tvSentenceVi = scrambleView.findViewById(R.id.st3_scramble_tv_sentence_vi);
        flexboxAnswer = scrambleView.findViewById(R.id.st3_scramble_flexbox_answer);
        flexboxChoices = scrambleView.findViewById(R.id.st3_scramble_flexbox_choices);
        btnScrambleCheck = scrambleView.findViewById(R.id.st3_scramble_btn_check);
        lottieScrambleCheckmark = scrambleView.findViewById(R.id.st3_scramble_lottie_checkmark);
        lottieScrambleFailed = scrambleView.findViewById(R.id.st3_scramble_lottie_failed);
        setupFeedbackAnimations(lottieScrambleCheckmark, lottieScrambleFailed);

        btnScrambleCheck.setOnClickListener(v -> checkScrambleAnswer());
    }

    private void startScrambleGame() {
        scrambleQuestions = ScrambleQuestionProvider.getQuestions();
        currentScrambleIndex = 0;
        // TODO: Load the first question
    }

    private void checkScrambleAnswer() {
        // TODO: Implement scramble check logic
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
}
