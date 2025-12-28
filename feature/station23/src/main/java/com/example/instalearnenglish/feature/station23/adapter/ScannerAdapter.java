package com.example.instalearnenglish.feature.station23.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instalearnenglish.feature.station23.R;
import com.example.instalearnenglish.feature.station23.model.ScannerItem;

import java.util.List;

public class ScannerAdapter extends RecyclerView.Adapter<ScannerAdapter.ScannerViewHolder> {

    private final Context context;
    private final List<ScannerItem> items;

    public ScannerAdapter(Context context, List<ScannerItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ScannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.st2_item_scanner, parent, false);
        return new ScannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScannerViewHolder holder, int position) {
        ScannerItem item = items.get(position);
        holder.icon.setImageResource(item.getDrawableResId());
        holder.name.setText(item.getItemName());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ScannerViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView name;

        public ScannerViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.st2_iv_scanner_item_icon);
            name = itemView.findViewById(R.id.st2_tv_scanner_item_name);
        }
    }
}
