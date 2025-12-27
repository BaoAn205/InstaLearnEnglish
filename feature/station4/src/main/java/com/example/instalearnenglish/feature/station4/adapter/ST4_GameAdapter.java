package com.example.instalearnenglish.feature.station4.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieAnimationView;
import com.example.instalearnenglish.feature.station4.R;
import com.example.instalearnenglish.feature.station4.ST4_LessonGameFragment.ST4_Game;

import java.util.List;

public class ST4_GameAdapter extends RecyclerView.Adapter<ST4_GameAdapter.ViewHolder> {

    private final List<ST4_Game> games;
    private final OnGameClickListener listener;

    public interface OnGameClickListener {
        void onGameClick(ST4_Game game);
    }

    public ST4_GameAdapter(List<ST4_Game> games, OnGameClickListener listener) {
        this.games = games;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.st4_game_grid_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(games.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final LottieAnimationView animationView;
        private final TextView tvGameTitle;

        ViewHolder(View view) {
            super(view);
            animationView = view.findViewById(R.id.iv_game_icon);
            tvGameTitle = view.findViewById(R.id.tv_game_title);
        }

        void bind(final ST4_Game game, final OnGameClickListener listener) {
            tvGameTitle.setText(game.getTitle());
            // Use the correct method to get the resource ID
            animationView.setAnimation(game.getIconResId());
            itemView.setOnClickListener(v -> listener.onGameClick(game));
        }
    }
}
