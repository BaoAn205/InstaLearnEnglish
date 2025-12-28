package com.example.instalearnenglish.feature.station4;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instalearnenglish.feature.station4.adapter.ST4_GameHistoryAdapter;
import com.example.instalearnenglish.feature.station4.utils.ST4_GameHistoryManager;

import org.json.JSONArray;
import org.json.JSONException;

public class ST4_GameHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.st4_activity_game_history);

        Toolbar toolbar = findViewById(R.id.toolbar_history);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        RecyclerView rvHistory = findViewById(R.id.rv_game_history);
        rvHistory.setLayoutManager(new LinearLayoutManager(this));

        String historyData = ST4_GameHistoryManager.getHistory(this);
        try {
            JSONArray jsonArray = new JSONArray(historyData);
            ST4_GameHistoryAdapter adapter = new ST4_GameHistoryAdapter(jsonArray);
            rvHistory.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
