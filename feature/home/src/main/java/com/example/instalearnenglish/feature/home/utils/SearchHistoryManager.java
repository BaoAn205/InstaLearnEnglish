package com.example.instalearnenglish.feature.home.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SearchHistoryManager {
    private static final String PREFS_NAME = "SearchHistoryPrefs";
    private static final String HISTORY_KEY = "search_history";
    private static final int MAX_HISTORY_SIZE = 10;

    public static void addSearchTerm(Context context, String term) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String jsonHistory = prefs.getString(HISTORY_KEY, "[]");

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        ArrayList<String> history = gson.fromJson(jsonHistory, type);

        // Remove term if it already exists to move it to the top
        history.remove(term);

        // Add the new term to the top of the list
        history.add(0, term);

        // Ensure the list doesn't exceed the max size
        while (history.size() > MAX_HISTORY_SIZE) {
            history.remove(history.size() - 1);
        }

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(HISTORY_KEY, gson.toJson(history));
        editor.apply();
    }

    public static List<String> getSearchHistory(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String jsonHistory = prefs.getString(HISTORY_KEY, "[]");

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(jsonHistory, type);
    }
}
