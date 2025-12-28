package com.example.instalearnenglish.feature.home.tools;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.instalearnenglish.feature.home.R;
import com.example.instalearnenglish.feature.home.model.Definition;
import com.example.instalearnenglish.feature.home.model.Meaning;
import com.example.instalearnenglish.feature.home.model.MyMemoryResponse;
import com.example.instalearnenglish.feature.home.model.Word;
import com.example.instalearnenglish.feature.home.remote.DictionaryApiService;
import com.example.instalearnenglish.feature.home.remote.RetrofitClient;
import com.example.instalearnenglish.feature.home.remote.TranslationApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FloatingDictionaryService extends Service {

    private WindowManager windowManager;
    private View floatingView;
    private WindowManager.LayoutParams params;

    private View collapsedView;
    private View expandedView;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (floatingView == null) {
            setupFloatingView();
        } else {
            collapsedView.setVisibility(View.VISIBLE);
            expandedView.setVisibility(View.GONE);
        }
        return START_STICKY;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupFloatingView() {
        try {
            // Sử dụng Theme cục bộ của project để tránh lỗi không tìm thấy symbol từ thư viện
            Context themedContext = new ContextThemeWrapper(this, R.style.Theme_InstaLearnEnglish);
            floatingView = LayoutInflater.from(themedContext).inflate(R.layout.layout_floating_dictionary, null);

            int layoutType;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                layoutType = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                layoutType = WindowManager.LayoutParams.TYPE_PHONE;
            }

            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    layoutType,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);

            params.gravity = Gravity.TOP | Gravity.START;
            params.x = 100;
            params.y = 200;

            windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            windowManager.addView(floatingView, params);

            collapsedView = floatingView.findViewById(R.id.collapsed_view);
            expandedView = floatingView.findViewById(R.id.expanded_view);

            floatingView.findViewById(R.id.btn_close_expanded).setOnClickListener(v -> {
                collapsedView.setVisibility(View.VISIBLE);
                expandedView.setVisibility(View.GONE);
                params.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                windowManager.updateViewLayout(floatingView, params);
            });

            EditText etSearch = floatingView.findViewById(R.id.et_search_floating);
            etSearch.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String word = etSearch.getText().toString().trim();
                    if (!word.isEmpty()) {
                        searchWord(word);
                    }
                    return true;
                }
                return false;
            });

            floatingView.findViewById(R.id.dictionary_bubble_icon).setOnTouchListener(new View.OnTouchListener() {
                private int initialX;
                private int initialY;
                private float initialTouchX;
                private float initialTouchY;
                private long lastTouchTime;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            initialX = params.x;
                            initialY = params.y;
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
                            lastTouchTime = System.currentTimeMillis();
                            return true;

                        case MotionEvent.ACTION_UP:
                            long clickDuration = System.currentTimeMillis() - lastTouchTime;
                            if (clickDuration < 200) {
                                collapsedView.setVisibility(View.GONE);
                                expandedView.setVisibility(View.VISIBLE);
                                params.flags &= ~WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                                windowManager.updateViewLayout(floatingView, params);
                            }
                            return true;

                        case MotionEvent.ACTION_MOVE:
                            params.x = initialX + (int) (event.getRawX() - initialTouchX);
                            params.y = initialY + (int) (event.getRawY() - initialTouchY);
                            windowManager.updateViewLayout(floatingView, params);
                            return true;
                    }
                    return false;
                }
            });
        } catch (Exception e) {
            Log.e("FloatingService", "Error: " + e.getMessage());
        }
    }

    private void searchWord(String word) {
        DictionaryApiService apiService = RetrofitClient.getClient().create(DictionaryApiService.class);
        Call<List<Word>> call = apiService.getWordDefinition(word);

        call.enqueue(new Callback<List<Word>>() {
            @Override
            public void onResponse(Call<List<Word>> call, Response<List<Word>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    displayResults(response.body().get(0));
                } else {
                    Toast.makeText(FloatingDictionaryService.this, "Word not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Word>> call, Throwable t) {
                Toast.makeText(FloatingDictionaryService.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayResults(Word word) {
        TextView tvWord = floatingView.findViewById(R.id.floating_tv_word);
        TextView tvPhonetic = floatingView.findViewById(R.id.floating_tv_phonetic);
        LinearLayout container = floatingView.findViewById(R.id.floating_meanings_container);

        tvWord.setText(word.getWord());
        tvPhonetic.setText(word.getPhonetics() != null && !word.getPhonetics().isEmpty() ? word.getPhonetics().get(0).getText() : "");

        translateWord(word.getWord());

        container.removeAllViews();
        if (word.getMeanings() != null) {
            for (Meaning meaning : word.getMeanings()) {
                TextView pos = new TextView(this);
                pos.setText(meaning.getPartOfSpeech());
                pos.setPadding(0, 16, 0, 4);
                pos.setTextColor(ContextCompat.getColor(this, android.R.color.black));
                pos.setTypeface(null, Typeface.BOLD);
                container.addView(pos);

                if (meaning.getDefinitions() != null) {
                    for (Definition def : meaning.getDefinitions()) {
                        TextView d = new TextView(this);
                        d.setText("- " + def.getDefinition());
                        d.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        d.setPadding(0, 4, 0, 4);
                        container.addView(d);
                    }
                }
            }
        }
    }

    private void translateWord(String word) {
        Retrofit translationRetrofit = RetrofitClient.getClient("https://api.mymemory.translated.net/");
        TranslationApiService translationService = translationRetrofit.create(TranslationApiService.class);

        Call<MyMemoryResponse> call = translationService.getTranslation(word, "en|vi");
        call.enqueue(new Callback<MyMemoryResponse>() {
            @Override
            public void onResponse(Call<MyMemoryResponse> call, Response<MyMemoryResponse> response) {
                TextView tvTranslation = floatingView.findViewById(R.id.floating_tv_translation);
                if (response.isSuccessful() && response.body() != null && response.body().getResponseData() != null) {
                    tvTranslation.setText(response.body().getResponseData().getTranslatedText());
                } else {
                    tvTranslation.setText("Translation N/A");
                }
            }

            @Override
            public void onFailure(Call<MyMemoryResponse> call, Throwable t) {
                TextView tvTranslation = floatingView.findViewById(R.id.floating_tv_translation);
                tvTranslation.setText("Error");
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (floatingView != null) {
            try {
                windowManager.removeView(floatingView);
            } catch (Exception e) {
                Log.e("FloatingService", "Error removing view: " + e.getMessage());
            }
        }
    }
}
