package com.example.instalearnenglish.feature.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instalearnenglish.feature.home.R;
import com.example.instalearnenglish.feature.home.model.Tip;

import java.util.List;

public class ArchivedTipAdapter extends RecyclerView.Adapter<ArchivedTipAdapter.TipViewHolder> {

    private final List<Tip> tipList;

    public ArchivedTipAdapter(List<Tip> tipList) {
        this.tipList = tipList;
    }

    @NonNull
    @Override
    public TipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_archived_tip, parent, false);
        return new TipViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TipViewHolder holder, int position) {
        Tip tip = tipList.get(position);
        holder.tvTipTitle.setText(tip.getTitle());
        holder.tvTipContent.setText(tip.getContent());
    }

    @Override
    public int getItemCount() {
        return tipList.size();
    }

    static class TipViewHolder extends RecyclerView.ViewHolder {
        TextView tvTipTitle;
        TextView tvTipContent;

        public TipViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTipTitle = itemView.findViewById(R.id.tv_tip_title);
            tvTipContent = itemView.findViewById(R.id.tv_tip_content);
        }
    }
}
