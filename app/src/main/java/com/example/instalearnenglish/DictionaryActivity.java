package com.example.instalearnenglish;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instalearnenglish.model.Definition;
import com.example.instalearnenglish.model.Meaning;
import com.example.instalearnenglish.model.Phonetic;
import com.example.instalearnenglish.model.Word;
import com.example.instalearnenglish.remote.DictionaryApiService;
import com.example.instalearnenglish.remote.RetrofitClient;
import com.example.instalearnenglish.util.SearchHistoryManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DictionaryActivity extends AppCompatActivity implements RecentSearchesAdapter.OnItemClickListener {

    private static final String TAG = "DictionaryActivity";

    // Main and Result Views
    private NestedScrollView mainContentView;
    private ScrollView wordResultView;

    // Search
    private EditText etSearch;

    // Recent Searches
    private RecyclerView rvRecentSearches;
    private RecentSearchesAdapter recentSearchesAdapter;
    private List<String> recentSearchesList;
    private SearchHistoryManager searchHistoryManager;

    // Result View Components
    private ImageButton btnBackToSearch, resultBtnPlayAudio;
    private TextView resultTvWord, resultTvPhonetic;
    private LinearLayout resultLlMeaningsContainer;
    private String audioUrl;

    private DictionaryApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        initializeViews();
        initializeServices();

        setupBottomNavigation();
        setupRecentSearches();
        setupSearchListener();
        setupResultViewListeners();
    }

    private void initializeViews() {
        mainContentView = findViewById(R.id.main_content_view);
        wordResultView = findViewById(R.id.word_result_view);
        etSearch = findViewById(R.id.et_search);
        rvRecentSearches = findViewById(R.id.rv_recent_searches);

        // Result view components
        btnBackToSearch = findViewById(R.id.btn_back_to_search);
        resultBtnPlayAudio = findViewById(R.id.result_btn_play_audio);
        resultTvWord = findViewById(R.id.result_tv_word);
        resultTvPhonetic = findViewById(R.id.result_tv_phonetic);
        resultLlMeaningsContainer = findViewById(R.id.result_ll_meanings_container);
    }

    private void initializeServices() {
        apiService = RetrofitClient.getRetrofitInstance().create(DictionaryApiService.class);
        searchHistoryManager = new SearchHistoryManager(this);
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_dictionary);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_dictionary) {
                return true;
            } else if (itemId == R.id.nav_flashcards) {
                startActivity(new Intent(getApplicationContext(), FlashcardsActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });
    }

    private void setupRecentSearches() {
        recentSearchesList = searchHistoryManager.loadSearchHistory();
        recentSearchesAdapter = new RecentSearchesAdapter(recentSearchesList);
        recentSearchesAdapter.setOnItemClickListener(this);
        rvRecentSearches.setLayoutManager(new LinearLayoutManager(this));
        rvRecentSearches.setAdapter(recentSearchesAdapter);
    }

    private void setupSearchListener() {
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String wordToSearch = etSearch.getText().toString().trim();
                if (!wordToSearch.isEmpty()) {
                    searchWord(wordToSearch);
                }
                return true;
            }
            return false;
        });
    }

    private void setupResultViewListeners() {
        btnBackToSearch.setOnClickListener(v -> showMainContent());
        resultBtnPlayAudio.setOnClickListener(v -> playAudio());
    }

    private void searchWord(String word) {
        apiService.getWordDefinition(word).enqueue(new Callback<List<Word>>() {
            @Override
            public void onResponse(Call<List<Word>> call, Response<List<Word>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    updateRecentSearches(word);
                    displayWordResult(response.body().get(0));
                } else {
                    Toast.makeText(DictionaryActivity.this, "Word not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Word>> call, Throwable t) {
                Toast.makeText(DictionaryActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayWordResult(Word word) {
        showResultView();
        resultTvWord.setText(word.getWord());

        resultLlMeaningsContainer.removeAllViews();
        audioUrl = null;
        String phoneticText = "";

        if (word.getPhonetics() != null && !word.getPhonetics().isEmpty()) {
            for (Phonetic p : word.getPhonetics()) {
                if (p.getText() != null && !p.getText().isEmpty()) {
                    phoneticText = p.getText();
                }
                if (p.getAudio() != null && !p.getAudio().isEmpty()) {
                    audioUrl = p.getAudio();
                }
                if (!phoneticText.isEmpty() && audioUrl != null && !audioUrl.isEmpty()) {
                    break;
                }
            }
        }

        resultTvPhonetic.setText(phoneticText);
        resultBtnPlayAudio.setVisibility(audioUrl != null && !audioUrl.isEmpty() ? View.VISIBLE : View.GONE);

        if (word.getMeanings() != null) {
            for (Meaning meaning : word.getMeanings()) {
                TextView tvPartOfSpeech = new TextView(this);
                tvPartOfSpeech.setText(meaning.getPartOfSpeech());
                tvPartOfSpeech.setTextSize(20f);
                tvPartOfSpeech.setTypeface(null, android.graphics.Typeface.BOLD);
                resultLlMeaningsContainer.addView(tvPartOfSpeech);

                if (meaning.getDefinitions() != null) {
                    for (Definition def : meaning.getDefinitions()) {
                        TextView tvDefinition = new TextView(this);
                        tvDefinition.setText("\t• " + def.getDefinition());
                        tvDefinition.setTextSize(16f);
                        resultLlMeaningsContainer.addView(tvDefinition);

                        if (def.getExample() != null && !def.getExample().isEmpty()) {
                            TextView tvExample = new TextView(this);
                            tvExample.setText("\t\t\tExample: \"" + def.getExample() + "\"");
                            tvExample.setTextSize(14f);
                            tvExample.setTypeface(null, android.graphics.Typeface.ITALIC);
                            resultLlMeaningsContainer.addView(tvExample);
                        }
                    }
                }
            }
        }      // TODO: Add logic to fetch and display Vietnamese meaning
    }

    private void playAudio() {
        if (audioUrl == null || audioUrl.isEmpty()) {
            Toast.makeText(this, "No audio available", Toast.LENGTH_SHORT).show();
            return;
        }
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(MediaPlayer::start);
            mediaPlayer.setOnCompletionListener(MediaPlayer::release);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Could not play audio", Toast.LENGTH_SHORT).show();
        }
    }

    private void showMainContent() {
        mainContentView.setVisibility(View.VISIBLE);
        wordResultView.setVisibility(View.GONE);
    }

    private void showResultView() {
        mainContentView.setVisibility(View.GONE);
        wordResultView.setVisibility(View.VISIBLE);
    }

    private void updateRecentSearches(String word) {
        recentSearchesList.remove(word);
        recentSearchesList.add(0, word);
        recentSearchesAdapter.notifyDataSetChanged();
        searchHistoryManager.saveSearchHistory(recentSearchesList);
    }

    @Override
    public void onItemClick(String word) {
        etSearch.setText(word);
        searchWord(word);
    }
}
