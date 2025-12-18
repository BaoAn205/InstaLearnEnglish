package com.example.instalearnenglish.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SearchHistoryManager {

    private static final String PREF_NAME = "SearchHistoryPref";
    private static final String KEY_SEARCH_HISTORY = "search_history";

    private final SharedPreferences sharedPreferences;
    private final Gson gson;

    public SearchHistoryManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void saveSearchHistory(List<String> searchHistory) {
        String json = gson.toJson(searchHistory);
        sharedPreferences.edit().putString(KEY_SEARCH_HISTORY, json).apply();
    }

    public List<String> loadSearchHistory() {
        String json = sharedPreferences.getString(KEY_SEARCH_HISTORY, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        List<String> searchHistory = gson.fromJson(json, type);

        if (searchHistory == null) {
            return new ArrayList<>();
        }
        return searchHistory;
    }
}
