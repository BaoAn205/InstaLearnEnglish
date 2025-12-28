package com.example.instalearnenglish.feature.station2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instalearnenglish.feature.station2.R;
import com.example.instalearnenglish.feature.station2.model.TravelTip;

import java.util.List;

public class TravelTipAdapter extends RecyclerView.Adapter<TravelTipAdapter.TipViewHolder> {

    public interface OnTipInteractionListener {
        void onPlayAudio(String textToSpeak);
    }

    private final Context context;
    private final List<TravelTip> tips;
    private final OnTipInteractionListener listener;

    public TravelTipAdapter(Context context, List<TravelTip> tips, OnTipInteractionListener listener) {
        this.context = context;
        this.tips = tips;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.st2_item_tip_card, parent, false);
        return new TipViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TipViewHolder holder, int position) {
        TravelTip tip = tips.get(position);
        holder.icon.setImageResource(tip.getIconResId());
        holder.title.setText(tip.getTitle());
        holder.content.setText(tip.getContent());

        holder.playButton.setOnClickListener(v -> {
            if (listener != null) {
                // Speak the full content
                listener.onPlayAudio(tip.getTitle() + ". " + tip.getContent());
            }
        });
    }

    @Override
    public int getItemCount() {
        return tips.size();
    }

    static class TipViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView title;
        TextView content;
        ImageView playButton;

        public TipViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.st2_tip_card_icon);
            title = itemView.findViewById(R.id.st2_tip_card_title);
            content = itemView.findViewById(R.id.st2_tip_card_content);
            playButton = itemView.findViewById(R.id.st2_tip_card_play_button);
        }
    }
}
