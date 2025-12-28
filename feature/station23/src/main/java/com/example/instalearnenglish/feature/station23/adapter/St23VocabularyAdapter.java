package com.example.instalearnenglish.feature.station23.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instalearnenglish.feature.station23.R;
import com.example.instalearnenglish.feature.station23.model.St23VocabItem;

import java.util.List;

public class St23VocabularyAdapter extends RecyclerView.Adapter<St23VocabularyAdapter.VocabViewHolder> {

    private final List<St23VocabItem> vocabList;
    private final OnVocabItemInteractionListener listener;
    private final Context context; 

    public interface OnVocabItemInteractionListener {
        void onSpeakClicked(String textToSpeak);
        void onCardClicked(View cardFront, View cardBack);
    }

    public St23VocabularyAdapter(Context context, List<St23VocabItem> vocabList, OnVocabItemInteractionListener listener) {
        this.context = context;
        this.vocabList = vocabList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VocabViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.st23_item_flashcard, parent, false);
        return new VocabViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VocabViewHolder holder, int position) {
        St23VocabItem currentItem = vocabList.get(position);
        holder.tvEnglish.setText(currentItem.getName());
        holder.tvVietnamese.setText(currentItem.getVietnameseMeaning());

        // Use the ImageButton for the speak action
        holder.ibSpeak.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSpeakClicked(currentItem.getName());
            }
        });

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCardClicked(holder.cardFront, holder.cardBack);
            }
        });
    }

    @Override
    public int getItemCount() {
        return vocabList.size();
    }

    static class VocabViewHolder extends RecyclerView.ViewHolder {
        TextView tvEnglish, tvVietnamese;
        ImageButton ibSpeak; // This is now ImageButton
        View cardFront, cardBack;

        public VocabViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEnglish = itemView.findViewById(R.id.st23_tv_english);
            tvVietnamese = itemView.findViewById(R.id.st23_tv_vietnamese);
            ibSpeak = itemView.findViewById(R.id.st23_ib_speak); // This now correctly finds the ImageButton
            cardFront = itemView.findViewById(R.id.st23_card_front);
            cardBack = itemView.findViewById(R.id.st23_card_back);
        }
    }
}
