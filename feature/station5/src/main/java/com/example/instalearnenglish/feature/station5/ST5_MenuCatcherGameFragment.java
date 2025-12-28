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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instalearnenglish.feature.station5.adapter.ST5_MenuGameGridAdapter;
import com.example.instalearnenglish.feature.station5.model.VocabItem;
import com.example.instalearnenglish.feature.station5.utils.ST5_GameHistoryManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class ST5_MenuCatcherGameFragment extends Fragment {

    private ProgressBar progressBar;
    private TextView tvScore;
    private ImageButton btnListen;
    private Button btnSubmit;
    private RecyclerView rvItemsGrid;

    private TextToSpeech tts;
    private boolean isTtsReady = false;
    private List<VocabItem> allItems;
    private List<VocabItem> targetItems = new ArrayList<>();
    private final Set<Integer> selectedPositions = new HashSet<>();
    
    private int currentRound = 0;
    private final int totalRounds = 5;
    private int totalCorrectRounds = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        tts = new TextToSpeech(getContext(), status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = tts.setLanguage(Locale.US);
                if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                    isTtsReady = true;
                }
            }
        });
        return inflater.inflate(R.layout.st5_fragment_menu_catcher_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        progressBar = view.findViewById(R.id.progress_bar);
        tvScore = view.findViewById(R.id.tv_score);
        btnListen = view.findViewById(R.id.btn_listen);
        btnSubmit = view.findViewById(R.id.btn_submit);
        rvItemsGrid = view.findViewById(R.id.rv_items_grid);

        rvItemsGrid.setLayoutManager(new GridLayoutManager(getContext(), 3));
        
        loadAllItems();
        startNewRound();

        btnListen.setOnClickListener(v -> speakRecommendation());
        btnSubmit.setOnClickListener(v -> checkAnswer());
    }

    private void loadAllItems() {
        allItems = new ArrayList<>();
        allItems.add(new VocabItem("Menu", "", R.drawable.st5_menu, "", ""));
        allItems.add(new VocabItem("Appetizer", "", R.drawable.st5_appetizer, "", ""));
        allItems.add(new VocabItem("Main Course", "", R.drawable.st5_main_course, "", ""));
        allItems.add(new VocabItem("Dessert", "", R.drawable.st5_dessert, "", ""));
        allItems.add(new VocabItem("Seafood", "", R.drawable.st5_seafood, "", ""));
        allItems.add(new VocabItem("Beverage", "", R.drawable.st5_beverage, "", ""));
        allItems.add(new VocabItem("Spice", "", R.drawable.st5_spice, "", ""));
        allItems.add(new VocabItem("Bakery", "", R.drawable.st5_bakery, "", ""));
        allItems.add(new VocabItem("Vegetarian", "", R.drawable.st5_vegetarian, "", ""));
    }

    private void startNewRound() {
        if (currentRound >= totalRounds) {
            finishGame();
            return;
        }

        progressBar.setProgress(currentRound);
        tvScore.setText("Round: " + (currentRound + 1) + "/" + totalRounds);
        
        Collections.shuffle(allItems);
        targetItems.clear();
        targetItems.add(allItems.get(0));
        targetItems.add(allItems.get(1));

        List<VocabItem> gridItems = new ArrayList<>(allItems);
        Collections.shuffle(gridItems);
        
        selectedPositions.clear();
        ST5_MenuGameGridAdapter adapter = new ST5_MenuGameGridAdapter(gridItems, selectedPositions);
        rvItemsGrid.setAdapter(adapter);

        new Handler(Looper.getMainLooper()).postDelayed(this::speakRecommendation, 1200);
    }

    private void speakRecommendation() {
        if (!isTtsReady) {
            Toast.makeText(getContext(), "Voice engine is loading...", Toast.LENGTH_SHORT).show();
            return;
        }
        if (targetItems.size() < 2) return;
        String text = "Today, I recommend the " + targetItems.get(0).getName() + " and the " + targetItems.get(1).getName() + ".";
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    private void checkAnswer() {
        ST5_MenuGameGridAdapter adapter = (ST5_MenuGameGridAdapter) rvItemsGrid.getAdapter();
        if (adapter == null) return;

        List<VocabItem> selectedItems = new ArrayList<>();
        for (int pos : selectedPositions) {
            selectedItems.add(adapter.getItemAt(pos));
        }

        boolean isCorrect = selectedItems.size() == targetItems.size();
        for (VocabItem target : targetItems) {
            boolean found = false;
            for (VocabItem selected : selectedItems) {
                if (selected.getName().equalsIgnoreCase(target.getName())) {
                    found = true;
                    break;
                }
            }
            if (!found) isCorrect = false;
        }

        if (isCorrect) {
            totalCorrectRounds++;
            Toast.makeText(getContext(), "Perfect!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Incorrect. Listen again!", Toast.LENGTH_SHORT).show();
        }

        currentRound++;
        new Handler(Looper.getMainLooper()).postDelayed(this::startNewRound, 1500);
    }

    private void finishGame() {
        String finalScore = totalCorrectRounds + "/" + totalRounds;
        ST5_GameHistoryManager.saveResult(getContext(), "The Menu Catcher", "Score: " + finalScore);
        Toast.makeText(getContext(), "Finished! Result: " + finalScore, Toast.LENGTH_LONG).show();
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
