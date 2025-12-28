package com.example.instalearnenglish.feature.station4.utils;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ST4_GameHistoryManager {
    // Đổi tên PREF_NAME để đảm bảo chỉ lưu dữ liệu riêng cho Station 4
    private static final String PREF_NAME = "InstaLearn_Station4_History";
    private static final String KEY_HISTORY = "st4_game_results";

    public static void saveResult(Context context, String gameName, String score) {
        if (context == null) return;
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String historyJson = prefs.getString(KEY_HISTORY, "[]");

        try {
            JSONArray jsonArray = new JSONArray(historyJson);
            JSONObject newResult = new JSONObject();
            
            String date = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
            
            newResult.put("game", gameName);
            newResult.put("score", score);
            newResult.put("date", date);

            // Giữ lại 20 kết quả gần nhất của Station 4
            if (jsonArray.length() >= 20) {
                jsonArray.remove(0);
            }
            jsonArray.put(newResult);

            prefs.edit().putString(KEY_HISTORY, jsonArray.toString()).apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String getHistory(Context context) {
        if (context == null) return "[]";
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_HISTORY, "[]");
    }
}
