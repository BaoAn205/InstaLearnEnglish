package com.example.instalearnenglish.feature.station23.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instalearnenglish.feature.station23.R;

import java.util.List;

public class St3MatchAdapter extends RecyclerView.Adapter<St3MatchAdapter.WordViewHolder> {

    private final Context context;
    private final List<String> wordList;
    private final OnWordClickListener listener;
    private int selectedPosition = -1;

    public interface OnWordClickListener {
        void onWordClick(String word, int position);
    }

    public St3MatchAdapter(Context context, List<String> wordList, OnWordClickListener listener) {
        this.context = context;
        this.wordList = wordList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.st3_item_match_word, parent, false);
        return new WordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        holder.wordText.setText(wordList.get(position));

        if (selectedPosition == position) {
            holder.cardView.setCardBackgroundColor(Color.LTGRAY);
        } else {
            holder.cardView.setCardBackgroundColor(Color.WHITE);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                notifyItemChanged(selectedPosition);
                selectedPosition = holder.getAdapterPosition();
                notifyItemChanged(selectedPosition);
                listener.onWordClick(wordList.get(selectedPosition), selectedPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return wordList.size();
    }
    
    public void removeItem(int position) {
        if (position >= 0 && position < wordList.size()) {
            wordList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clearSelection() {
        int oldPosition = selectedPosition;
        selectedPosition = -1;
        if (oldPosition != -1) {
            notifyItemChanged(oldPosition);
        }
    }

    static class WordViewHolder extends RecyclerView.ViewHolder {
        TextView wordText;
        CardView cardView;

        public WordViewHolder(@NonNull View itemView) {
            super(itemView);
            wordText = itemView.findViewById(R.id.st3_tv_match_word);
            cardView = itemView.findViewById(R.id.st3_card_match_word);
        }
    }
}
