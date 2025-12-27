package com.example.instalearnenglish.feature.station4.adapter;

import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instalearnenglish.feature.station4.R;
import com.example.instalearnenglish.feature.station4.model.Tip;

import java.util.List;

public class ST4_TipsPagerAdapter extends RecyclerView.Adapter<ST4_TipsPagerAdapter.ViewHolder> {

    private final List<Tip> tips;
    private final TextToSpeech tts;

    public ST4_TipsPagerAdapter(List<Tip> tips, TextToSpeech tts) {
        this.tips = tips;
        this.tts = tts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.st4_tip_pager_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(tips.get(position), tts);
    }

    @Override
    public int getItemCount() {
        return tips.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivTipIcon;
        private final TextView tvTipTitle;
        private final TextView tvTipContent;
        private final ImageButton btnSpeak;

        ViewHolder(View view) {
            super(view);
            ivTipIcon = view.findViewById(R.id.iv_tip_icon);
            tvTipTitle = view.findViewById(R.id.tv_tip_title);
            tvTipContent = view.findViewById(R.id.tv_tip_content);
            btnSpeak = view.findViewById(R.id.btn_speak_tip);
        }

        void bind(final Tip tip, final TextToSpeech tts) {
            tvTipTitle.setText(tip.getTitle());
            tvTipContent.setText(tip.getContent());
            ivTipIcon.setImageResource(tip.getImageResId());

            btnSpeak.setOnClickListener(v -> {
                if (tts != null) {
                    String textToSpeak = tip.getTitle() + ". " + tip.getContent();
                    tts.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
                }
            });
        }
    }
}
