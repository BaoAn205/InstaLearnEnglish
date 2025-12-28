package com.example.instalearnenglish.feature.station23.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instalearnenglish.feature.station23.R;
import com.example.instalearnenglish.feature.station23.model.St23VocabItem;

import java.util.List;

public class St23VocabItemAdapter extends RecyclerView.Adapter<St23VocabItemAdapter.ViewHolder> {

    private final List<St23VocabItem> items;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(St23VocabItem item, ImageView sharedImageView);
    }

    public St23VocabItemAdapter(List<St23VocabItem> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.st2_vocab_item, parent, false);
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
            imageView = view.findViewById(R.id.st2_iv_vocab_image);
            textView = view.findViewById(R.id.st2_tv_vocab_name);
        }

        void bind(final St23VocabItem item, final OnItemClickListener listener) {
            imageView.setImageResource(item.getImageResId());
            textView.setText(item.getName());

            String transitionName = "st2_vocab_image_" + item.getName();
            ViewCompat.setTransitionName(imageView, transitionName);

            itemView.setOnClickListener(v -> listener.onItemClick(item, imageView));
        }
    }
}
