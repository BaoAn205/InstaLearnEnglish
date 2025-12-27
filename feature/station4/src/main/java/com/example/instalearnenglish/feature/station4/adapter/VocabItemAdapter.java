package com.example.instalearnenglish.feature.station4.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instalearnenglish.feature.station4.R;
import com.example.instalearnenglish.feature.station4.model.VocabItem;

import java.util.List;

public class VocabItemAdapter extends RecyclerView.Adapter<VocabItemAdapter.ViewHolder> {

    private final List<VocabItem> items;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(VocabItem item, ImageView sharedImageView); // Pass the ImageView for transition
    }

    public VocabItemAdapter(List<VocabItem> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.st4_vocab_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(items.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView textView;

        ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.iv_vocab_image);
            textView = view.findViewById(R.id.tv_vocab_name);
        }

        void bind(final VocabItem item, final OnItemClickListener listener) {
            imageView.setImageResource(item.getImageResId());
            textView.setText(item.getName());

            // Set a unique transition name for each item
            String transitionName = "vocab_image_transition_" + item.getName();
            ViewCompat.setTransitionName(imageView, transitionName);

            itemView.setOnClickListener(v -> listener.onItemClick(item, imageView));
        }
    }
}
