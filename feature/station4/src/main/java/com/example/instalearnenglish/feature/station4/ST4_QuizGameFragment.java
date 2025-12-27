package com.example.instalearnenglish.feature.station4;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
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
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ST4_QuizGameFragment extends Fragment implements View.OnClickListener {

    private TextView tvProgress, tvQuestion;
    private Button btnOption1, btnOption2, btnOption3, btnOption4;
    private ImageButton btnSpeak, btnBack;
    private ProgressBar progressTimer;
    private List<Button> optionButtons;

    private List<Question> questionList;
    private int currentQuestionIndex = 0;
    private int score = 0;

    private TextToSpeech tts;
    private CountDownTimer countDownTimer;
    private static final long QUESTION_TIME_LIMIT = 20000; // 20 seconds

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        tts = new TextToSpeech(getContext(), status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = tts.setLanguage(Locale.US);
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "The Language specified is not supported!");
                }
            } else {
                Log.e("TTS", "Initialization Failed!");
            }
        });
        return inflater.inflate(R.layout.st4_fragment_quiz_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        
        tvProgress = view.findViewById(R.id.tv_progress);
        tvQuestion = view.findViewById(R.id.tv_question);
        btnOption1 = view.findViewById(R.id.btn_option1);
        btnOption2 = view.findViewById(R.id.btn_option2);
        btnOption3 = view.findViewById(R.id.btn_option3);
        btnOption4 = view.findViewById(R.id.btn_option4);
        btnSpeak = view.findViewById(R.id.btn_speak);
        btnBack = view.findViewById(R.id.btn_back);
        progressTimer = view.findViewById(R.id.progress_timer);

        optionButtons = new ArrayList<>();
        optionButtons.add(btnOption1);
        optionButtons.add(btnOption2);
        optionButtons.add(btnOption3);
        optionButtons.add(btnOption4);

        for (Button btn : optionButtons) {
            btn.setOnClickListener(this);
        }

        btnSpeak.setOnClickListener(v -> speakQuestion());
        btnBack.setOnClickListener(v -> requireActivity().finish());

        startGame();
    }

    private void startGame() {
        currentQuestionIndex = 0;
        score = 0;
        loadQuestions();
        displayQuestion();
    }

    private void displayQuestion() {
        if (!isAdded()) return;
        if (currentQuestionIndex < questionList.size()) {
            resetButtonColors();
            Question currentQuestion = questionList.get(currentQuestionIndex);
            tvProgress.setText("Question: " + (currentQuestionIndex + 1) + "/" + questionList.size());
            tvQuestion.setText(currentQuestion.getQuestionText());

            List<String> options = currentQuestion.getShuffledOptions();
            for (int i = 0; i < optionButtons.size(); i++) {
                optionButtons.get(i).setText(options.get(i));
                optionButtons.get(i).setEnabled(true);
            }
            startTimer();
        } else {
            updateGameProgress();
        }
    }

    private void startTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        progressTimer.setProgress(progressTimer.getMax());
        countDownTimer = new CountDownTimer(QUESTION_TIME_LIMIT, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(isAdded()) progressTimer.setProgress((int) (millisUntilFinished / 100));
            }

            @Override
            public void onFinish() {
                if(isAdded()) handleTimeout();
            }
        }.start();
    }

    private void handleTimeout() {
        playSoundAndShowToast(false, "Time's up!");
        for (Button btn : optionButtons) {
            btn.setEnabled(false);
            if (btn.getText().toString().equals(questionList.get(currentQuestionIndex).getCorrectAnswer())) {
                btn.setBackgroundColor(Color.GREEN);
            }
        }
        moveToNextQuestionWithDelay();
    }

    @Override
    public void onClick(View v) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        Button clickedButton = (Button) v;
        String selectedAnswer = clickedButton.getText().toString();
        Question currentQuestion = questionList.get(currentQuestionIndex);

        for (Button btn : optionButtons) {
            btn.setEnabled(false);
        }

        boolean isCorrect = selectedAnswer.equals(currentQuestion.getCorrectAnswer());
        
        if (isCorrect) {
            score++;
            clickedButton.setBackgroundColor(Color.GREEN);
            playSoundAndShowToast(true, "Correct!");
        } else {
            clickedButton.setBackgroundColor(Color.RED);
            for (Button btn : optionButtons) {
                if (btn.getText().toString().equals(currentQuestion.getCorrectAnswer())) {
                    btn.setBackgroundColor(Color.GREEN);
                    break;
                }
            }
            playSoundAndShowToast(false, "Wrong!");
        }
        moveToNextQuestionWithDelay();
    }

    private void moveToNextQuestionWithDelay() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (!isAdded()) return;
            currentQuestionIndex++;
            displayQuestion();
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

        userDocRef.update("station1_completed_games", FieldValue.arrayUnion("QUIZ_PROHIBITED_ITEMS"))
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
        String message = "Your score: " + score + "/" + questionList.size();
        if (justUnlocked) {
            message += "\n\nCongratulations! You have unlocked Station 2!";
        }

        new AlertDialog.Builder(requireContext())
                .setTitle("Quiz Completed!")
                .setMessage(message)
                .setPositiveButton("Play Again", (dialog, which) -> startGame())
                .setNegativeButton("Exit", (dialog, which) -> requireActivity().finish())
                .setCancelable(false)
                .show();
    }

    private void resetButtonColors() {
        for (Button btn : optionButtons) {
            btn.setBackgroundColor(Color.parseColor("#6200EE")); // Default button color
        }
    }

    private void speakQuestion() {
        if (!isAdded() || tts == null) return;
        String text = tvQuestion.getText().toString();
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void loadQuestions() {
        questionList = new ArrayList<>();
        questionList.add(new Question("Where should you pack your power bank?", "In carry-on luggage", "In checked luggage", "Either is fine", "It is not allowed"));
        questionList.add(new Question("What is the maximum volume for liquids in carry-on luggage?", "100ml per container", "200ml per container", "500ml total", "No limit"));
        questionList.add(new Question("To save space, it is best to...", "Roll your clothes", "Fold your clothes flat", "Stuff them in randomly", "Use large suitcases"));
        questionList.add(new Question("Which item is a good alternative to liquid toiletries?", "Solid shampoo bars", "Larger bottles", "Many small bottles", "None of the above"));
        questionList.add(new Question("What should you do with your important documents like passports?", "Scan and save a digital copy", "Only carry the physical copy", "Leave them at home", "Give them to a friend"));
        questionList.add(new Question("An empty water bottle is useful because...", "You can refill it after security", "It's a souvenir", "It's good for decoration", "You can sell it"));
        questionList.add(new Question("Which of these should be in a first-aid kit?", "Pain relievers and band-aids", "Only prescription medication", "Snacks and drinks", "A map"));
        questionList.add(new Question("What is the main benefit of a universal travel adapter?", "It works in most countries", "It charges faster", "It is cheaper", "It is more colorful"));
        questionList.add(new Question("Why is it a good idea to pack some snacks?", "For long waits or flights", "To save money on all meals", "To share with everyone", "To feed pigeons"));
        questionList.add(new Question("Lithium batteries are NOT allowed in...", "Checked luggage", "Carry-on luggage", "Your pocket", "Any luggage"));
        Collections.shuffle(questionList);
    }

    private static class Question {
        private String questionText;
        private String correctAnswer;
        private String[] incorrectAnswers;

        public Question(String questionText, String correctAnswer, String... incorrectAnswers) {
            this.questionText = questionText;
            this.correctAnswer = correctAnswer;
            this.incorrectAnswers = incorrectAnswers;
        }

        public String getQuestionText() { return questionText; }
        public String getCorrectAnswer() { return correctAnswer; }

        public List<String> getShuffledOptions() {
            List<String> options = new ArrayList<>();
            options.add(correctAnswer);
            Collections.addAll(options, incorrectAnswers);
            Collections.shuffle(options);
            return options;
        }
    }
}
