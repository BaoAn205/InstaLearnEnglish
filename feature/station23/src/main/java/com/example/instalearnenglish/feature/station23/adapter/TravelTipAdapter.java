package com.example.instalearnenglish.feature.station23.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instalearnenglish.feature.home.utils.ArchiveManager;
import com.example.instalearnenglish.feature.station23.R;
import com.example.instalearnenglish.feature.station23.model.TravelTip;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TravelTipAdapter extends RecyclerView.Adapter<TravelTipAdapter.TipViewHolder> {

    public interface OnTipInteractionListener {
        void onPlayAudio(String textToSpeak);
    }

    private final Context context;
    private final List<TravelTip> tips;
    private final OnTipInteractionListener listener;
    private final Set<Integer> expandedPositions = new HashSet<>();
    private int stationId = 2; // Default to Station 2

    public TravelTipAdapter(Context context, List<TravelTip> tips, OnTipInteractionListener listener) {
        this.context = context;
        this.tips = tips;
        this.listener = listener;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
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
        holder.vietnamese.setText(tip.getVietnameseMeaning());

        if (expandedPositions.contains(position)) {
            holder.vietnamese.setVisibility(View.VISIBLE);
            holder.divider.setVisibility(View.VISIBLE);
            holder.btnShowTranslation.setText("Ẩn bản dịch");
        } else {
            holder.vietnamese.setVisibility(View.GONE);
            holder.divider.setVisibility(View.GONE);
            holder.btnShowTranslation.setText("Xem bản dịch");
        }

        holder.playButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPlayAudio(tip.getTitle() + ". " + tip.getContent());
            }
        });

        holder.btnShowTranslation.setOnClickListener(v -> {
            if (expandedPositions.contains(position)) {
                expandedPositions.remove(position);
            } else {
                expandedPositions.add(position);
            }
            notifyItemChanged(position);
        });

        holder.btnSave.setOnClickListener(v -> {
            String id = "ST" + stationId + "_TIP_" + position;
            ArchiveManager.saveItem(context, id, tip.getTitle(), "TIP", stationId, position);
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
        TextView vietnamese;
        ImageView playButton;
        Button btnShowTranslation;
        View divider;
        ImageView btnSave;

        public TipViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.st2_tip_card_icon);
            title = itemView.findViewById(R.id.st2_tip_card_title);
            content = itemView.findViewById(R.id.st2_tip_card_content);
            vietnamese = itemView.findViewById(R.id.st2_tip_card_vietnamese);
            playButton = itemView.findViewById(R.id.st2_tip_card_play_button);
            btnShowTranslation = itemView.findViewById(R.id.btn_show_translation);
            divider = itemView.findViewById(R.id.divider);
            btnSave = itemView.findViewById(R.id.st2_tip_card_save_button);
        }
    }
}
