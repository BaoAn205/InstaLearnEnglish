package com.example.instalearnenglish.feature.home.tools;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.instalearnenglish.feature.home.R;
import com.example.instalearnenglish.feature.home.adapter.RecentSearchesAdapter;
import com.example.instalearnenglish.feature.home.databinding.DialogDictionaryBinding;
import com.example.instalearnenglish.feature.home.model.Definition;
import com.example.instalearnenglish.feature.home.model.Meaning;
import com.example.instalearnenglish.feature.home.model.MyMemoryResponse;
import com.example.instalearnenglish.feature.home.model.Phonetic;
import com.example.instalearnenglish.feature.home.model.Word;
import com.example.instalearnenglish.feature.home.remote.DictionaryApiService;
import com.example.instalearnenglish.feature.home.remote.RetrofitClient;
import com.example.instalearnenglish.feature.home.remote.TranslationApiService;
import com.example.instalearnenglish.feature.home.utils.SearchHistoryManager;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DictionaryDialogFragment extends DialogFragment {

    private DialogDictionaryBinding binding;
    private MediaPlayer mediaPlayer;
    private RecentSearchesAdapter recentSearchesAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogDictionaryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupSearchListener();
        setupBackButton();
        setupPopularWords();
        setupRecentSearches();
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private void setupRecentSearches() {
        List<String> history = SearchHistoryManager.getSearchHistory(getContext());
        binding.rvRecentSearches.setLayoutManager(new LinearLayoutManager(getContext()));
        recentSearchesAdapter = new RecentSearchesAdapter(history, term -> {
            binding.etSearch.setText(term);
            searchWord(term);
        });
        binding.rvRecentSearches.setAdapter(recentSearchesAdapter);
    }

    private void setupPopularWords() {
        List<String> popularWords = Arrays.asList("hello", "world", "android", "english", "language", "learning");
        LayoutInflater inflater = LayoutInflater.from(getContext());
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

    private void searchWord(String word) {
        DictionaryApiService apiService = RetrofitClient.getClient().create(DictionaryApiService.class);
        Call<List<Word>> call = apiService.getWordDefinition(word);

        call.enqueue(new Callback<List<Word>>() {
            @Override
            public void onResponse(Call<List<Word>> call, Response<List<Word>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    SearchHistoryManager.addSearchTerm(getContext(), word);
                    displayResults(response.body().get(0));
                } else {
                    Toast.makeText(getContext(), "Word not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Word>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
        LayoutInflater inflater = LayoutInflater.from(getContext());
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
                Toast.makeText(getContext(), "Could not play audio", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hideKeyboard() {
        View view = getView();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
