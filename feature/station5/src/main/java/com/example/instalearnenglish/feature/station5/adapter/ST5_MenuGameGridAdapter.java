package com.example.instalearnenglish.feature.station5.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instalearnenglish.feature.station5.R;
import com.example.instalearnenglish.feature.station5.model.VocabItem;

import java.util.List;
import java.util.Set;

public class ST5_MenuGameGridAdapter extends RecyclerView.Adapter<ST5_MenuGameGridAdapter.ViewHolder> {

    private final List<VocabItem> items;
    private final Set<Integer> selectedPositions;

    public ST5_MenuGameGridAdapter(List<VocabItem> items, Set<Integer> selectedPositions) {
        this.items = items;
        this.selectedPositions = selectedPositions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.st5_item_menu_game_grid, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VocabItem item = items.get(position);
        holder.ivItem.setImageResource(item.getImageResId());
        holder.tvName.setText(item.getName());

        if (selectedPositions.contains(position)) {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#E1BEE7"));
            holder.cardView.setStrokeWidth(4);
            holder.cardView.setStrokeColor(Color.parseColor("#9C27B0"));
        } else {
            holder.cardView.setCardBackgroundColor(Color.WHITE);
            holder.cardView.setStrokeWidth(0);
        }

        holder.itemView.setOnClickListener(v -> {
            if (selectedPositions.contains(position)) {
                selectedPositions.remove(position);
            } else {
                selectedPositions.add(position);
            }
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public VocabItem getItemAt(int position) {
        return items.get(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        com.google.android.material.card.MaterialCardView cardView;
        ImageView ivItem;
        TextView tvName;

        ViewHolder(View view) {
            super(view);
            cardView = (com.google.android.material.card.MaterialCardView) view;
            ivItem = view.findViewById(R.id.iv_game_item);
            tvName = view.findViewById(R.id.tv_game_item_name);
        }
    }
}
