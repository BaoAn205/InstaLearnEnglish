package com.example.instalearnenglish.feature.station23.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.instalearnenglish.feature.station23.R;
import com.example.instalearnenglish.feature.station23.model.GameMenuItem;

import java.util.List;

public class St3GameMenuAdapter extends RecyclerView.Adapter<St3GameMenuAdapter.GameViewHolder> {

    private final Context context;
    private final List<GameMenuItem> gameList;
    private final OnGameClickListener listener;

    public interface OnGameClickListener {
        void onGameClick(GameMenuItem game);
    }

    public St3GameMenuAdapter(Context context, List<GameMenuItem> gameList, OnGameClickListener listener) {
        this.context = context;
        this.gameList = gameList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.st3_item_game_card, parent, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        GameMenuItem game = gameList.get(position);
        holder.gameTitle.setText(game.getTitle());
        holder.gameIcon.setAnimation(game.getResourceId());
        holder.gameIcon.playAnimation();

        holder.itemView.setOnClickListener(v -> {
            if(listener != null) {
                listener.onGameClick(game);
            }
        });
    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }

    static class GameViewHolder extends RecyclerView.ViewHolder {
        LottieAnimationView gameIcon;
        TextView gameTitle;

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            // Use the new IDs from the updated layout
            gameIcon = itemView.findViewById(R.id.lottie_game_icon);
            gameTitle = itemView.findViewById(R.id.tv_game_title);
        }
    }
}
