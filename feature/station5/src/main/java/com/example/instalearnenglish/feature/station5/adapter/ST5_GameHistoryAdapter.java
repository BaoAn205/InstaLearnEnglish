package com.example.instalearnenglish.feature.station5.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instalearnenglish.feature.station5.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ST5_GameHistoryAdapter extends RecyclerView.Adapter<ST5_GameHistoryAdapter.ViewHolder> {

    private final JSONArray historyArray;

    public ST5_GameHistoryAdapter(JSONArray historyArray) {
        this.historyArray = historyArray;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.st5_item_game_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            // Display newest first
            JSONObject item = historyArray.getJSONObject(historyArray.length() - 1 - position);
            holder.tvGameName.setText(item.getString("game"));
            holder.tvScore.setText(item.getString("score"));
            holder.tvDate.setText(item.getString("date"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return historyArray.length();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvGameName, tvScore, tvDate;

        ViewHolder(View view) {
            super(view);
            tvGameName = view.findViewById(R.id.tv_history_game_name);
            tvScore = view.findViewById(R.id.tv_history_score);
            tvDate = view.findViewById(R.id.tv_history_date);
        }
    }
}
