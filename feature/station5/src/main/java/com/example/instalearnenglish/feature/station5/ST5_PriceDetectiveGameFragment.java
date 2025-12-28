package com.example.instalearnenglish.feature.station5;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
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
import androidx.fragment.app.Fragment;

import com.example.instalearnenglish.feature.station5.utils.ST5_GameHistoryManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class ST5_PriceDetectiveGameFragment extends Fragment {

    private ProgressBar progressBar;
    private TextView tvScore;
    private ImageButton btnListen;
    private Button btnOption1, btnOption2, btnOption3, btnOption4;

    private TextToSpeech tts;
    private int currentRound = 0;
    private final int totalRounds = 5;
    private int totalCorrectRounds = 0;
    
    private int correctPrice = 0;
    private String speechText = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        tts = new TextToSpeech(getContext(), status -> {
            if (status == TextToSpeech.SUCCESS) tts.setLanguage(Locale.US);
        });
        return inflater.inflate(R.layout.st5_fragment_price_detective_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        progressBar = view.findViewById(R.id.progress_bar);
        tvScore = view.findViewById(R.id.tv_score);
        btnListen = view.findViewById(R.id.btn_listen_price);
        btnOption1 = view.findViewById(R.id.btn_option1);
        btnOption2 = view.findViewById(R.id.btn_option2);
        btnOption3 = view.findViewById(R.id.btn_option3);
        btnOption4 = view.findViewById(R.id.btn_option4);

        startNewRound();

        btnListen.setOnClickListener(v -> speakPrice());
        
        View.OnClickListener optionListener = v -> {
            Button b = (Button) v;
            String selected = b.getText().toString().replace("$", "");
            checkAnswer(Integer.parseInt(selected));
        };

        btnOption1.setOnClickListener(optionListener);
        btnOption2.setOnClickListener(optionListener);
        btnOption3.setOnClickListener(optionListener);
        btnOption4.setOnClickListener(optionListener);
    }

    private void startNewRound() {
        if (currentRound >= totalRounds) {
            finishGame();
            return;
        }

        progressBar.setProgress(currentRound);
        tvScore.setText("Round: " + (currentRound + 1) + "/" + totalRounds);

        Random r = new Random();
        int basePrice = (r.nextInt(10) + 1) * 10; // 10, 20, ..., 100
        int discountPercent = (r.nextInt(5) + 1) * 10; // 10%, 20%, ..., 50%
        
        correctPrice = basePrice - (basePrice * discountPercent / 100);
        speechText = "This item costs " + basePrice + " dollars, but today you have a " + discountPercent + " percent discount.";

        // Generate options
        List<Integer> options = new ArrayList<>();
        options.add(correctPrice);
        while (options.size() < 4) {
            int wrong = (r.nextInt(10) + 1) * 10;
            if (!options.contains(wrong)) options.add(wrong);
        }
        Collections.shuffle(options);

        btnOption1.setText("$" + options.get(0));
        btnOption2.setText("$" + options.get(1));
        btnOption3.setText("$" + options.get(2));
        btnOption4.setText("$" + options.get(3));

        new Handler(Looper.getMainLooper()).postDelayed(this::speakPrice, 1000);
    }

    private void speakPrice() {
        if (!speechText.isEmpty()) {
            tts.speak(speechText, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    private void checkAnswer(int selectedPrice) {
        if (selectedPrice == correctPrice) {
            totalCorrectRounds++;
            Toast.makeText(getContext(), "Excellent calculation!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Incorrect. The final price was $" + correctPrice, Toast.LENGTH_SHORT).show();
        }

        currentRound++;
        new Handler(Looper.getMainLooper()).postDelayed(this::startNewRound, 1500);
    }

    private void finishGame() {
        String finalScore = totalCorrectRounds + "/" + totalRounds;
        ST5_GameHistoryManager.saveResult(getContext(), "Price Detective", "Score: " + finalScore);
        Toast.makeText(getContext(), "Shopping Trip Finished! Result: " + finalScore, Toast.LENGTH_LONG).show();
        if (getActivity() != null) getActivity().finish();
    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}
