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
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.instalearnenglish.feature.station23.adapter.ScannerAdapter;
import com.example.instalearnenglish.feature.station23.adapter.St3GameMenuAdapter;
import com.example.instalearnenglish.feature.station23.model.AnnouncementQuestion;
import com.example.instalearnenglish.feature.station23.model.AnnouncementQuestionProvider;
import com.example.instalearnenglish.feature.station23.model.BoardingRushQuestion;
import com.example.instalearnenglish.feature.station23.model.BoardingRushQuestionProvider;
import com.example.instalearnenglish.feature.station23.model.GameMenuItem;
import com.example.instalearnenglish.feature.station23.model.ScannerItem;
import com.example.instalearnenglish.feature.station23.model.ScannerItemProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class St23MiniGameFragment extends Fragment implements St3GameMenuAdapter.OnGameClickListener {

    // Main Containers
    private View menuContainer, scannerGameContainer, announcementGameContainer, boardingRushGameContainer;
    private RecyclerView rvGameMenu;

    // --- Vocabulary Scanner Game ---
    private View scannerMemorizeContainer, scannerQuestionContainer;
    private RecyclerView rvScannerItems;
    private TextView tvScannerMemorizeTimer;
    private Button btnScannerAnswerA, btnScannerAnswerB, btnScannerAnswerC;
    private LottieAnimationView lottieScannerCheckmark, lottieScannerFailed;
    private ScannerAdapter scannerAdapter;
    private List<ScannerItem> currentScannerItems;
    private String correctScannerAnswer;
    private CountDownTimer scannerMemorizeTimer;
    private int scannerScore = 0;
    private int scannerLives = 3;

    // --- Announcement Game ---
    private LottieAnimationView lottiePlayAudio, lottieAnnouncementCheckmark, lottieAnnouncementFailed;
    private TextView tvAnnouncementQuestion;
    private Button btnAnnounceAnswerA, btnAnnounceAnswerB, btnAnnounceAnswerC;
    private List<AnnouncementQuestion> announcementQuestions;
    private int currentAnnounceIndex = 0;
    private MediaPlayer announcementMediaPlayer;

    // --- Boarding Rush Game ---
    private View brGamePlayContainer, brResultContainer;
    private LottieAnimationView brLottiePassenger, brLottieResult;
    private TextView brTvDistance, brTvLives, brTvQuestion, brTvResultMessage;
    private Button brBtnAnswerA, brBtnAnswerB, brBtnAnswerC, brBtnPlayAgain;
    private List<BoardingRushQuestion> brQuestions;
    private int brCurrentQuestionIndex = 0;
    private int brDistance = 100;
    private int brLives = 5;

    // --- Sound related ---
    private MediaPlayer backgroundMusicPlayer;
    private SoundPool soundPool;
    private int rightAnswerSoundId, wrongAnswerSoundId, finishGameSoundId;
    private boolean soundsLoaded = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.st23_fragment_mini_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        menuContainer = view.findViewById(R.id.st2_game_menu_container);
        scannerGameContainer = view.findViewById(R.id.st23_security_game_container);
        announcementGameContainer = view.findViewById(R.id.st2_announcement_game_container);
        boardingRushGameContainer = view.findViewById(R.id.st2_boarding_rush_game_container);
        rvGameMenu = menuContainer.findViewById(R.id.st2_rv_game_menu);

        setupNewGameMenu();
        setupSounds();
        showMenu();
    }

    private void setupNewGameMenu() {
        rvGameMenu.setLayoutManager(new GridLayoutManager(getContext(), 2));
        List<GameMenuItem> gameList = new ArrayList<>();
        // Using available animations as placeholders to fix build error
        gameList.add(new GameMenuItem("Soi Từ Vựng", R.raw.animation_quiz, null)); // Placeholder for animation_scanner
        gameList.add(new GameMenuItem("Lắng nghe Thông báo", R.raw.animation_word_match, null)); // Placeholder for animation_announcement
        gameList.add(new GameMenuItem("Đến kịp giờ bay", R.raw.animation_boarding_rush, null));

        St3GameMenuAdapter adapter = new St3GameMenuAdapter(getContext(), gameList, this);
        rvGameMenu.setAdapter(adapter);
    }

    @Override
    public void onGameClick(GameMenuItem game) {
        if (game.getTitle().contains("Soi Từ Vựng")) {
            showScannerGame();
        } else if (game.getTitle().contains("Lắng nghe")) {
            showAnnouncementGame();
        } else if (game.getTitle().contains("kịp giờ bay")) {
            showBoardingRushGame();
        }
    }

    // --- ALL ORIGINAL CODE BELOW IS PRESERVED ---

    private void setupSounds() {
        AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build();
        soundPool = new SoundPool.Builder().setMaxStreams(3).setAudioAttributes(audioAttributes).build();
        soundPool.setOnLoadCompleteListener((pool, sampleId, status) -> soundsLoaded = (status == 0));
        rightAnswerSoundId = soundPool.load(getContext(), R.raw.right_answer, 1);
        wrongAnswerSoundId = soundPool.load(getContext(), R.raw.wrong_answer, 1);
        finishGameSoundId = soundPool.load(getContext(), R.raw.finishminigame3_2, 1);
    }

    private void showMenu() {
        menuContainer.setVisibility(View.VISIBLE);
        scannerGameContainer.setVisibility(View.GONE);
        announcementGameContainer.setVisibility(View.GONE);
        boardingRushGameContainer.setVisibility(View.GONE);
        stopAllGames();
    }

    private void stopAllGames() {
        stopBackgroundMusic();
        if (scannerMemorizeTimer != null) scannerMemorizeTimer.cancel();
        if (announcementMediaPlayer != null) {
            if (announcementMediaPlayer.isPlaying()) announcementMediaPlayer.stop();
            announcementMediaPlayer.release();
            announcementMediaPlayer = null;
        }
    }

    private void showScannerGame() {
        stopAllGames();
        menuContainer.setVisibility(View.GONE);
        announcementGameContainer.setVisibility(View.GONE);
        boardingRushGameContainer.setVisibility(View.GONE);
        scannerGameContainer.setVisibility(View.VISIBLE);

        if (((ViewGroup) scannerGameContainer).getChildCount() == 0) {
            getLayoutInflater().inflate(R.layout.st2_game_luggage_scanner, (ViewGroup) scannerGameContainer, true);
        }
        setupScannerGame(scannerGameContainer);
        startScannerGame();
    }

    private void showAnnouncementGame() {
        stopAllGames();
        menuContainer.setVisibility(View.GONE);
        scannerGameContainer.setVisibility(View.GONE);
        boardingRushGameContainer.setVisibility(View.GONE);
        announcementGameContainer.setVisibility(View.VISIBLE);

        if (((ViewGroup) announcementGameContainer).getChildCount() == 0) {
            getLayoutInflater().inflate(R.layout.st2_game_announcement_layout, (ViewGroup) announcementGameContainer, true);
        }
        setupAnnouncementGameViews(announcementGameContainer);
        startAnnouncementGame();
    }

    private void showBoardingRushGame() {
        stopAllGames();
        menuContainer.setVisibility(View.GONE);
        scannerGameContainer.setVisibility(View.GONE);
        announcementGameContainer.setVisibility(View.GONE);
        boardingRushGameContainer.setVisibility(View.VISIBLE);

        if (((ViewGroup) boardingRushGameContainer).getChildCount() == 0) {
            getLayoutInflater().inflate(R.layout.st2_fragment_game_boarding_rush, (ViewGroup) boardingRushGameContainer, true);
        }
        setupBoardingRushGame(boardingRushGameContainer);
        startBoardingRushGame();
    }

    private void setupScannerGame(View gameView) {
        scannerMemorizeContainer = gameView.findViewById(R.id.st2_scanner_memorize_container);
        scannerQuestionContainer = gameView.findViewById(R.id.st2_scanner_question_container);
        rvScannerItems = gameView.findViewById(R.id.st2_scanner_rv_items);
        tvScannerMemorizeTimer = gameView.findViewById(R.id.st2_scanner_tv_memorize_timer);
        btnScannerAnswerA = gameView.findViewById(R.id.st2_scanner_answer_A);
        btnScannerAnswerB = gameView.findViewById(R.id.st2_scanner_answer_B);
        btnScannerAnswerC = gameView.findViewById(R.id.st2_scanner_answer_C);
        lottieScannerCheckmark = gameView.findViewById(R.id.st2_scanner_lottie_checkmark);
        lottieScannerFailed = gameView.findViewById(R.id.st2_scanner_lottie_failed);

        rvScannerItems.setLayoutManager(new GridLayoutManager(getContext(), 3));
        setupFeedbackAnimations(lottieScannerCheckmark, lottieScannerFailed);
    }

    private void startScannerGame() {
        startBackgroundMusic(R.raw.gameplay3_4);
        scannerScore = 0;
        scannerLives = 3;
        loadScannerRound();
    }

    private void loadScannerRound() {
        scannerMemorizeContainer.setVisibility(View.VISIBLE);
        scannerQuestionContainer.setVisibility(View.GONE);

        currentScannerItems = ScannerItemProvider.generateRoundItems(6);
        scannerAdapter = new ScannerAdapter(getContext(), currentScannerItems);
        rvScannerItems.setAdapter(scannerAdapter);

        for (ScannerItem item : currentScannerItems) {
            if (item.isForbidden()) {
                correctScannerAnswer = item.getItemName();
                break;
            }
        }
        startMemorizeTimer();
    }

    private void startMemorizeTimer() {
        scannerMemorizeTimer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(isAdded() && tvScannerMemorizeTimer != null) {
                    tvScannerMemorizeTimer.setText(String.valueOf(millisUntilFinished / 1000));
                }
            }

            @Override
            public void onFinish() {
                if(isAdded()) showScannerQuestionScreen();
            }
        }.start();
    }

    private void showScannerQuestionScreen() {
        scannerMemorizeContainer.setVisibility(View.GONE);
        scannerQuestionContainer.setVisibility(View.VISIBLE);

        List<String> options = new ArrayList<>();
        options.add(correctScannerAnswer);

        List<ScannerItem> safeItems = new ArrayList<>();
        for(ScannerItem item : currentScannerItems) {
            if(!item.isForbidden()) safeItems.add(item);
        }
        Collections.shuffle(safeItems);
        options.add(safeItems.get(0).getItemName());
        options.add(safeItems.get(1).getItemName());

        Collections.shuffle(options);

        btnScannerAnswerA.setText(options.get(0));
        btnScannerAnswerB.setText(options.get(1));
        btnScannerAnswerC.setText(options.get(2));

        btnScannerAnswerA.setOnClickListener(v -> checkScannerAnswer(options.get(0)));
        btnScannerAnswerB.setOnClickListener(v -> checkScannerAnswer(options.get(1)));
        btnScannerAnswerC.setOnClickListener(v -> checkScannerAnswer(options.get(2)));
    }

    private void checkScannerAnswer(String selectedAnswer) {
        boolean isCorrect = selectedAnswer.equals(correctScannerAnswer);
        if (isCorrect) {
            scannerScore++;
            if (soundsLoaded) soundPool.play(rightAnswerSoundId, 1, 1, 0, 0, 1);
            lottieScannerCheckmark.setVisibility(View.VISIBLE);
            lottieScannerCheckmark.playAnimation();
            new Handler(Looper.getMainLooper()).postDelayed(this::loadScannerRound, 1500);
        } else {
            scannerLives--;
            if (soundsLoaded) soundPool.play(wrongAnswerSoundId, 1, 1, 0, 0, 1);
            lottieScannerFailed.setVisibility(View.VISIBLE);
            lottieScannerFailed.playAnimation();
            if (scannerLives <= 0) {
                Toast.makeText(getContext(), "Game Over! Score: " + scannerScore, Toast.LENGTH_LONG).show();
                new Handler(Looper.getMainLooper()).postDelayed(this::showMenu, 2000);
            } else {
                Toast.makeText(getContext(), "Sai rồi! Bạn còn " + scannerLives + " mạng.", Toast.LENGTH_SHORT).show();
                new Handler(Looper.getMainLooper()).postDelayed(this::loadScannerRound, 1500);
            }
        }
    }

    private void setupAnnouncementGameViews(View announcementGameView) {
        lottiePlayAudio = announcementGameView.findViewById(R.id.st2_lottie_play_audio);
        tvAnnouncementQuestion = announcementGameView.findViewById(R.id.st2_tv_announcement_question);
        btnAnnounceAnswerA = announcementGameView.findViewById(R.id.st2_btn_announcement_answer_a);
        btnAnnounceAnswerB = announcementGameView.findViewById(R.id.st2_btn_announcement_answer_b);
        btnAnnounceAnswerC = announcementGameView.findViewById(R.id.st2_btn_announcement_answer_c);
        lottieAnnouncementCheckmark = announcementGameView.findViewById(R.id.st2_lottie_announcement_checkmark);
        lottieAnnouncementFailed = announcementGameView.findViewById(R.id.st2_lottie_announcement_failed);
        setupFeedbackAnimations(lottieAnnouncementCheckmark, lottieAnnouncementFailed);
    }

    private void startAnnouncementGame() {
        announcementQuestions = AnnouncementQuestionProvider.getQuestions();
        currentAnnounceIndex = 0;
        loadAnnouncementQuestion(currentAnnounceIndex);
    }

    private void loadAnnouncementQuestion(int index) {
        if (index >= announcementQuestions.size()) { Toast.makeText(getContext(), "Chúc mừng! Bạn đã hoàn thành!", Toast.LENGTH_SHORT).show(); showMenu(); return; }
        AnnouncementQuestion currentQuestion = announcementQuestions.get(index);
        tvAnnouncementQuestion.setText(currentQuestion.getQuestion());
        List<String> answers = currentQuestion.getAnswers();
        btnAnnounceAnswerA.setText(answers.get(0));
        btnAnnounceAnswerB.setText(answers.get(1));
        btnAnnounceAnswerC.setText(answers.get(2));
        lottiePlayAudio.setClickable(true);
        lottiePlayAudio.setOnClickListener(v -> playAudio(currentQuestion.getAudioFileResId()));
        btnAnnounceAnswerA.setOnClickListener(v -> checkAnnouncementAnswer(0, currentQuestion.getCorrectAnswerIndex()));
        btnAnnounceAnswerB.setOnClickListener(v -> checkAnnouncementAnswer(1, currentQuestion.getCorrectAnswerIndex()));
        btnAnnounceAnswerC.setOnClickListener(v -> checkAnnouncementAnswer(2, currentQuestion.getCorrectAnswerIndex()));
    }

    private void playAudio(int audioResId) {
        if (announcementMediaPlayer != null && announcementMediaPlayer.isPlaying()) return;
        if (announcementMediaPlayer != null) announcementMediaPlayer.release();
        announcementMediaPlayer = MediaPlayer.create(getContext(), audioResId);
        if (announcementMediaPlayer == null) { Toast.makeText(getContext(), "Lỗi: Không tìm thấy file âm thanh.", Toast.LENGTH_SHORT).show(); return; }
        lottiePlayAudio.setClickable(false);
        lottiePlayAudio.playAnimation();
        announcementMediaPlayer.setOnCompletionListener(mp -> { if (isAdded()) { lottiePlayAudio.setProgress(0f); lottiePlayAudio.setClickable(true); } });
        announcementMediaPlayer.start();
    }

    private void checkAnnouncementAnswer(int selectedIndex, int correctIndex) {
        if (selectedIndex == correctIndex) {
            if (soundsLoaded) soundPool.play(rightAnswerSoundId, 1, 1, 0, 0, 1);
            lottieAnnouncementCheckmark.setVisibility(View.VISIBLE);
            lottieAnnouncementCheckmark.playAnimation();
            currentAnnounceIndex++;
            new Handler(Looper.getMainLooper()).postDelayed(() -> loadAnnouncementQuestion(currentAnnounceIndex), 1500);
        } else {
            if (soundsLoaded) soundPool.play(wrongAnswerSoundId, 1, 1, 0, 0, 1);
            lottieAnnouncementFailed.setVisibility(View.VISIBLE);
            lottieAnnouncementFailed.playAnimation();
        }
    }

    private void setupBoardingRushGame(View gameView) {
        brGamePlayContainer = gameView.findViewById(R.id.st2_br_game_play_container);
        brResultContainer = gameView.findViewById(R.id.st2_br_result_container);
        brLottiePassenger = gameView.findViewById(R.id.st2_br_lottie_passenger);
        brLottieResult = gameView.findViewById(R.id.st2_br_lottie_result);
        brTvDistance = gameView.findViewById(R.id.st2_br_tv_distance);
        brTvLives = gameView.findViewById(R.id.st2_br_tv_lives);
        brTvQuestion = gameView.findViewById(R.id.st2_br_tv_question);
        brTvResultMessage = gameView.findViewById(R.id.st2_br_tv_result_message);
        brBtnAnswerA = gameView.findViewById(R.id.st2_br_btn_answer_a);
        brBtnAnswerB = gameView.findViewById(R.id.st2_br_btn_answer_b);
        brBtnAnswerC = gameView.findViewById(R.id.st2_br_btn_answer_c);
        brBtnPlayAgain = gameView.findViewById(R.id.st2_br_btn_play_again);

        brBtnAnswerA.setOnClickListener(v -> checkBoardingRushAnswer(0));
        brBtnAnswerB.setOnClickListener(v -> checkBoardingRushAnswer(1));
        brBtnAnswerC.setOnClickListener(v -> checkBoardingRushAnswer(2));
        brBtnPlayAgain.setOnClickListener(v -> startBoardingRushGame());
    }

    private void startBoardingRushGame() {
        brGamePlayContainer.setVisibility(View.VISIBLE);
        brResultContainer.setVisibility(View.GONE);
        brDistance = 100;
        brLives = 5;
        brCurrentQuestionIndex = 0;
        brQuestions = BoardingRushQuestionProvider.getQuestions();
        startBackgroundMusic(R.raw.gameplay3_2);
        loadBoardingRushQuestion();
    }

    private void loadBoardingRushQuestion() {
        updateBoardingRushUI();
        if (brCurrentQuestionIndex >= brQuestions.size()) return;
        BoardingRushQuestion currentQuestion = brQuestions.get(brCurrentQuestionIndex);
        brTvQuestion.setText(currentQuestion.getQuestion());
        List<String> answers = currentQuestion.getAnswers();
        brBtnAnswerA.setText(answers.get(0));
        brBtnAnswerB.setText(answers.get(1));
        brBtnAnswerC.setText(answers.get(2));
    }

    private void updateBoardingRushUI() {
        brTvDistance.setText("Còn lại: " + brDistance + "m");
        brTvLives.setText("Mạng: " + brLives);
    }

    private void checkBoardingRushAnswer(int selectedAnswerIndex) {
        if (!soundsLoaded) return;
        BoardingRushQuestion currentQuestion = brQuestions.get(brCurrentQuestionIndex);
        boolean isCorrect = selectedAnswerIndex == currentQuestion.getCorrectAnswerIndex();
        if (isCorrect) {
            soundPool.play(rightAnswerSoundId, 1, 1, 0, 0, 1);
            brDistance -= 10;
            if (brDistance <= 0) {
                handleBoardingRushWin();
                return;
            }
        } else {
            soundPool.play(wrongAnswerSoundId, 1, 1, 0, 0, 1);
            brLives--;
            if(brLottiePassenger != null) brLottiePassenger.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake));
            if (brLives <= 0) {
                handleBoardingRushLoss();
                return;
            }
        }
        brCurrentQuestionIndex++;
        if (brCurrentQuestionIndex < brQuestions.size()) {
            loadBoardingRushQuestion();
        } else {
            handleBoardingRushWin();
        }
    }

    private void handleBoardingRushWin() {
        stopBackgroundMusic();
        if (soundsLoaded) soundPool.play(finishGameSoundId, 1, 1, 0, 0, 1);
        brGamePlayContainer.setVisibility(View.GONE);
        brResultContainer.setVisibility(View.VISIBLE);
        brLottieResult.setAnimation(R.raw.plane_takeoff);
        brLottieResult.playAnimation();
        brTvResultMessage.setText("Bạn đã kịp chuyến bay!");
    }

    private void handleBoardingRushLoss() {
        stopBackgroundMusic();
        if (soundsLoaded) soundPool.play(finishGameSoundId, 1, 1, 0, 0, 1);
        brGamePlayContainer.setVisibility(View.GONE);
        brResultContainer.setVisibility(View.VISIBLE);
        brLottieResult.setAnimation(R.raw.trip_failed);
        brLottieResult.playAnimation();
        brTvResultMessage.setText("Bạn đã trễ chuyến bay!");
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
        if (backgroundMusicPlayer != null) {
            if (backgroundMusicPlayer.isPlaying()) {
                backgroundMusicPlayer.stop();
            }
            backgroundMusicPlayer.release();
            backgroundMusicPlayer = null;
        }
    }

    private void setupFeedbackAnimations(LottieAnimationView checkmark, LottieAnimationView failed) {
        if (checkmark != null) {
            checkmark.enableMergePathsForKitKatAndAbove(true);
            checkmark.addAnimatorListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (isAdded()) checkmark.setVisibility(View.GONE);
                }
            });
        }
        if (failed != null) {
            failed.enableMergePathsForKitKatAndAbove(true);
            failed.addAnimatorListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (isAdded()) failed.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopAllGames();
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }
}
