package com.example.instalearnenglish;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RecentSearchesAdapter extends RecyclerView.Adapter<RecentSearchesAdapter.ViewHolder> {

    // 1. Listener Interface
    public interface OnItemClickListener {
        void onItemClick(String word);
    }

    private final List<String> recentSearches;
    private OnItemClickListener onItemClickListener;

    public RecentSearchesAdapter(List<String> recentSearches) {
        this.recentSearches = recentSearches;
    }

    // 2. Setter for the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recent_search_item, parent, false);
        return new ViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getRecentWordTextView().setText(recentSearches.get(position));
    }

    @Override
    public int getItemCount() {
        return recentSearches.size();
    }

    // 3. Update ViewHolder to handle clicks
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView recentWordTextView;

        public ViewHolder(View view, final OnItemClickListener listener) {
            super(view);
            recentWordTextView = view.findViewById(R.id.tv_recent_word);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                // Make sure the position is valid and a listener is set
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(recentSearches.get(position));
                }
            });
        }

        public TextView getRecentWordTextView() {
            return recentWordTextView;
        }
    }
}
