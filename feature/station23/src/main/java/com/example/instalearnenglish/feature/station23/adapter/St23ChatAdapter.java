package com.example.instalearnenglish.feature.station23.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instalearnenglish.feature.station23.R;
import com.example.instalearnenglish.feature.station23.model.St23ChatMessage;

import java.util.List;

public class St23ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_BOT = 1;
    private static final int VIEW_TYPE_USER = 2;

    private final List<St23ChatMessage> chatMessages;

    public St23ChatAdapter(List<St23ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    @Override
    public int getItemViewType(int position) {
        if (chatMessages.get(position).getSender() == St23ChatMessage.Sender.BOT) {
            return VIEW_TYPE_BOT;
        } else {
            return VIEW_TYPE_USER;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_BOT) {
            View view = inflater.inflate(R.layout.st23_item_chat_bot, parent, false);
            return new BotViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.st23_item_chat_user, parent, false);
            return new UserViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        St23ChatMessage message = chatMessages.get(position);
        if (holder.getItemViewType() == VIEW_TYPE_BOT) {
            ((BotViewHolder) holder).bind(message);
        } else {
            ((UserViewHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    // ViewHolder for Bot messages
    static class BotViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;

        BotViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.st23_tv_bot_message);
        }

        void bind(St23ChatMessage message) {
            messageText.setText(message.getMessage());
        }
    }

    // ViewHolder for User messages
    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;

        UserViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.st23_tv_user_message);
        }

        void bind(St23ChatMessage message) {
            messageText.setText(message.getMessage());
        }
    }
}
