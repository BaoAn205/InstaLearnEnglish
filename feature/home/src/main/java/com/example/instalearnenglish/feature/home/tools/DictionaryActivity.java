package com.example.instalearnenglish.feature.home.tools;

import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.instalearnenglish.feature.home.HomeActivity;
import com.example.instalearnenglish.feature.home.R;
import com.example.instalearnenglish.feature.home.adapter.RecentSearchesAdapter;
import com.example.instalearnenglish.feature.home.databinding.FeatureHomeDictionaryBinding;
import com.example.instalearnenglish.feature.home.model.Definition;
import com.example.instalearnenglish.feature.home.model.Meaning;
import com.example.instalearnenglish.feature.home.model.MyMemoryResponse;
import com.example.instalearnenglish.feature.home.model.Phonetic;
import com.example.instalearnenglish.feature.home.model.Word;
import com.example.instalearnenglish.feature.home.profile.ProfileActivity;
import com.example.instalearnenglish.feature.home.remote.DictionaryApiService;
import com.example.instalearnenglish.feature.home.remote.RetrofitClient;
import com.example.instalearnenglish.feature.home.remote.TranslationApiService;
import com.example.instalearnenglish.feature.home.utils.MusicManager;
import com.example.instalearnenglish.feature.home.utils.SearchHistoryManager;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DictionaryActivity extends AppCompatActivity {

    private FeatureHomeDictionaryBinding binding;
    private MediaPlayer mediaPlayer;
    private RecentSearchesAdapter recentSearchesAdapter;
    private static final int OVERLAY_PERMISSION_REQ_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FeatureHomeDictionaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupSearchListener();
        setupBackButton();
        setupPopularWords();
        setupBottomNavigation();
        setupFloatingSwitch();
    }

    private void setupFloatingSwitch() {
        binding.switchFloatingDictionary.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
                    binding.switchFloatingDictionary.setChecked(false);
                } else {
                    startService(new Intent(this, FloatingDictionaryService.class));
                }
            } else {
                stopService(new Intent(this, FloatingDictionaryService.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(this)) {
                binding.switchFloatingDictionary.setChecked(true);
                startService(new Intent(this, FloatingDictionaryService.class));
            } else {
                Toast.makeText(this, "Permission denied. Mini Mode cannot be enabled.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MusicManager.isNavigationToMusicActivity = false;
        MusicManager.start(this);
        setupRecentSearches();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MusicManager.pause();
    }

    private void setupRecentSearches() {
        List<String> history = SearchHistoryManager.getSearchHistory(this);
        binding.rvRecentSearches.setLayoutManager(new LinearLayoutManager(this));
        recentSearchesAdapter = new RecentSearchesAdapter(history, term -> {
            binding.etSearch.setText(term);
            searchWord(term);
        });
        binding.rvRecentSearches.setAdapter(recentSearchesAdapter);
    }

    private void setupPopularWords() {
        List<String> popularWords = Arrays.asList("hello", "world", "android", "english", "language", "learning");
        LayoutInflater inflater = LayoutInflater.from(this);
        for (String word : popularWords) {
            TextView popularWordView = (TextView) inflater.inflate(R.layout.popular_word_item, binding.popularWordsContainer, false);
            popularWordView.setText(word);
            popularWordView.setOnClickListener(v -> {
                binding.etSearch.setText(word);
                searchWord(word);
            });
            binding.popularWordsContainer.addView(popularWordView);
        }
    }

    private void setupSearchListener() {
        binding.etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String word = binding.etSearch.getText().toString().trim();
                if (!word.isEmpty()) {
                    searchWord(word);
                    hideKeyboard();
                }
                return true;
            }
            return false;
        });
    }

    private void setupBackButton() {
        binding.btnBackToSearch.setOnClickListener(v -> {
            binding.wordResultView.setVisibility(View.GONE);
            binding.mainContentView.setVisibility(View.VISIBLE);
            binding.etSearch.setText("");
            setupRecentSearches();
        });
    }

    private void setupBottomNavigation() {
        binding.bottomNavigation.setSelectedItemId(R.id.nav_dictionary);
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                MusicManager.isNavigationToMusicActivity = true;
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_dictionary) {
                return true; 
            } else if (itemId == R.id.nav_profile) {
                MusicManager.isNavigationToMusicActivity = true;
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }

    private void searchWord(String word) {
        DictionaryApiService apiService = RetrofitClient.getClient().create(DictionaryApiService.class);
        Call<List<Word>> call = apiService.getWordDefinition(word);

        call.enqueue(new Callback<List<Word>>() {
            @Override
            public void onResponse(Call<List<Word>> call, Response<List<Word>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    SearchHistoryManager.addSearchTerm(DictionaryActivity.this, word);
                    displayResults(response.body().get(0));
                } else {
                    Toast.makeText(DictionaryActivity.this, "Word not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Word>> call, Throwable t) {
                Toast.makeText(DictionaryActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayResults(Word word) {
        binding.mainContentView.setVisibility(View.GONE);
        binding.wordResultView.setVisibility(View.VISIBLE);
        binding.resultTvWord.setText(word.getWord());

        String phoneticText = "";
        String audioUrl = null;
        if (word.getPhonetics() != null) {
            for (Phonetic phonetic : word.getPhonetics()) {
                if (phonetic.getText() != null && !phonetic.getText().isEmpty()) {
                    phoneticText = phonetic.getText();
                    if (phonetic.getAudio() != null && !phonetic.getAudio().isEmpty()) {
                        audioUrl = phonetic.getAudio();
                    }
                    break;
                }
            }
        }
        binding.resultTvPhonetic.setText(phoneticText);
        setupAudioButton(audioUrl);

        translateWordToVietnamese(word.getWord());

        binding.resultLlMeaningsContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);
        if (word.getMeanings() != null) {
            for (Meaning meaning : word.getMeanings()) {
                TextView partOfSpeechView = (TextView) inflater.inflate(R.layout.template_part_of_speech, binding.resultLlMeaningsContainer, false);
                partOfSpeechView.setText(meaning.getPartOfSpeech());
                binding.resultLlMeaningsContainer.addView(partOfSpeechView);

                if (meaning.getDefinitions() != null) {
                    for (Definition definition : meaning.getDefinitions()) {
                        View definitionView = inflater.inflate(R.layout.template_definition, binding.resultLlMeaningsContainer, false);
                        TextView tvDefinition = definitionView.findViewById(R.id.tv_definition);
                        TextView tvExample = definitionView.findViewById(R.id.tv_example);

                        tvDefinition.setText("- " + definition.getDefinition());
                        if (definition.getExample() != null && !definition.getExample().isEmpty()) {
                            tvExample.setText("Eg: \"" + definition.getExample() + "\"");
                            tvExample.setVisibility(View.VISIBLE);
                        } else {
                            tvExample.setVisibility(View.GONE);
                        }
                        binding.resultLlMeaningsContainer.addView(definitionView);
                    }
                }
            }
        }
    }

    private void translateWordToVietnamese(String word) {
        Retrofit translationRetrofit = RetrofitClient.getClient("https://api.mymemory.translated.net/");
        TranslationApiService translationService = translationRetrofit.create(TranslationApiService.class);

        Call<MyMemoryResponse> call = translationService.getTranslation(word, "en|vi");
        call.enqueue(new Callback<MyMemoryResponse>() {
            @Override
            public void onResponse(Call<MyMemoryResponse> call, Response<MyMemoryResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getResponseData() != null) {
                    String translatedText = response.body().getResponseData().getTranslatedText();
                    binding.tvVietnameseMeaning.setText(translatedText);
                } else {
                    binding.tvVietnameseMeaning.setText("Translation not available.");
                }
            }

            @Override
            public void onFailure(Call<MyMemoryResponse> call, Throwable t) {
                binding.tvVietnameseMeaning.setText("Translation failed.");
            }
        });
    }

    private void setupAudioButton(String url) {
        if (url == null || url.isEmpty()) {
            binding.resultBtnPlayAudio.setVisibility(View.GONE);
            return;
        }
        binding.resultBtnPlayAudio.setVisibility(View.VISIBLE);
        binding.resultBtnPlayAudio.setOnClickListener(v -> {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioAttributes(new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).setUsage(AudioAttributes.USAGE_MEDIA).build());
            try {
                mediaPlayer.setDataSource(url);
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(MediaPlayer::start);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Could not play audio", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
