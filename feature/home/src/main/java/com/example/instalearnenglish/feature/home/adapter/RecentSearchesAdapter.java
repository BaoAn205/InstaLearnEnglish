package com.example.instalearnenglish.feature.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.instalearnenglish.feature.home.R;
import java.util.List;

public class RecentSearchesAdapter extends RecyclerView.Adapter<RecentSearchesAdapter.ViewHolder> {

    private final List<String> searchTerms;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String term);
    }

    public RecentSearchesAdapter(List<String> searchTerms, OnItemClickListener listener) {
        this.searchTerms = searchTerms;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_search_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(searchTerms.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return searchTerms.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView) itemView;
        }

        public void bind(final String term, final OnItemClickListener listener) {
            textView.setText(term);
            itemView.setOnClickListener(v -> listener.onItemClick(term));
        }
    }
}
