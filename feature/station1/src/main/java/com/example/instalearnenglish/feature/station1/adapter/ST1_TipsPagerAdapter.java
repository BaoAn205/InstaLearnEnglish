package com.example.instalearnenglish.feature.station1.adapter;

import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instalearnenglish.feature.station1.R;
import com.example.instalearnenglish.feature.station1.model.Tip;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ST1_TipsPagerAdapter extends RecyclerView.Adapter<ST1_TipsPagerAdapter.ViewHolder> {

    private final List<Tip> tips;
    private final TextToSpeech tts;
    private final Set<Integer> expandedPositions = new HashSet<>();

    public ST1_TipsPagerAdapter(List<Tip> tips, TextToSpeech tts) {
        this.tips = tips;
        this.tts = tts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.st1_tip_pager_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(tips.get(position), tts, position);
    }

    @Override
    public int getItemCount() {
        return tips.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivTipIcon;
        private final TextView tvTipTitle;
        private final TextView tvTipContent;
        private final TextView tvTipVietnamese;
        private final ImageButton btnSpeak;
        private final Button btnShowTranslation;

        ViewHolder(View view) {
            super(view);
            ivTipIcon = view.findViewById(R.id.iv_tip_icon);
            tvTipTitle = view.findViewById(R.id.tv_tip_title);
            tvTipContent = view.findViewById(R.id.tv_tip_content);
            tvTipVietnamese = view.findViewById(R.id.tv_tip_vietnamese);
            btnSpeak = view.findViewById(R.id.btn_speak_tip);
            btnShowTranslation = view.findViewById(R.id.btn_show_translation);
        }

        void bind(final Tip tip, final TextToSpeech tts, int position) {
            tvTipTitle.setText(tip.getTitle());
            tvTipContent.setText(tip.getContent());
            tvTipVietnamese.setText(tip.getVietnameseMeaning());
            ivTipIcon.setImageResource(tip.getImageResId());

            if (expandedPositions.contains(position)) {
                tvTipVietnamese.setVisibility(View.VISIBLE);
                btnShowTranslation.setText("Ẩn bản dịch");
            } else {
                tvTipVietnamese.setVisibility(View.GONE);
                btnShowTranslation.setText("Xem bản dịch");
            }

            btnSpeak.setOnClickListener(v -> {
                if (tts != null) {
                    String textToSpeak = tip.getTitle() + ". " + tip.getContent();
                    tts.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
                }
            });

            btnShowTranslation.setOnClickListener(v -> {
                if (tvTipVietnamese.getVisibility() == View.GONE) {
                    tvTipVietnamese.setVisibility(View.VISIBLE);
                    btnShowTranslation.setText("Ẩn bản dịch");
                    expandedPositions.add(position);
                } else {
                    tvTipVietnamese.setVisibility(View.GONE);
                    btnShowTranslation.setText("Xem bản dịch");
                    expandedPositions.remove(position);
                }
            });
        }
    }
}
