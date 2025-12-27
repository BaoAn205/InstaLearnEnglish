package com.example.instalearnenglish.feature.station1.adapter;

import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instalearnenglish.feature.station1.R;
import com.example.instalearnenglish.feature.station1.model.Tip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ST1_TipsPagerAdapter extends RecyclerView.Adapter<ST1_TipsPagerAdapter.ViewHolder> {

    private final List<Tip> tips;
    private final TextToSpeech tts;

    public ST1_TipsPagerAdapter(List<Tip> tips, TextToSpeech tts) {
        this.tips = tips;
        this.tts = tts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.st1_tip_card_item, parent, false);
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
        private final TextView tvTipVietnameseContent;
        private final ImageButton btnSpeak;
        private final ImageButton btnArchive;

        ViewHolder(View view) {
            super(view);
            ivTipIcon = view.findViewById(R.id.iv_tip_icon);
            tvTipTitle = view.findViewById(R.id.tv_tip_title);
            tvTipContent = view.findViewById(R.id.tv_tip_content);
            tvTipVietnameseContent = view.findViewById(R.id.tv_tip_vietnamese_content);
            btnSpeak = view.findViewById(R.id.btn_speak_tip);
            btnArchive = view.findViewById(R.id.btn_archive_tip);
        }

        void bind(final Tip tip, final TextToSpeech tts) {
            tvTipTitle.setText(tip.getTitle());
            tvTipContent.setText(tip.getContent());
            tvTipVietnameseContent.setText(tip.getVietnameseContent());
            ivTipIcon.setImageResource(tip.getImageResId());

            itemView.setOnClickListener(v -> {
                boolean isVietnameseVisible = tvTipVietnameseContent.getVisibility() == View.VISIBLE;
                tvTipVietnameseContent.setVisibility(isVietnameseVisible ? View.GONE : View.VISIBLE);
            });

            btnSpeak.setOnClickListener(v -> {
                if (tts != null) {
                    String textToSpeak = tip.getTitle() + ". " + tip.getContent();
                    tts.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
                }
            });

            btnArchive.setOnClickListener(v -> {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user == null) {
                    Toast.makeText(itemView.getContext(), "You must be logged in to archive tips.", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Map<String, Object> tipData = new HashMap<>();
                tipData.put("title", tip.getTitle());
                tipData.put("content", tip.getContent());

                db.collection("users").document(user.getUid()).collection("archived_tips")
                        .document(tip.getTitle())
                        .set(tipData)
                        .addOnSuccessListener(aVoid -> Toast.makeText(itemView.getContext(), "Saved to Archive!", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(itemView.getContext(), "Failed to save.", Toast.LENGTH_SHORT).show());
            });
        }
    }
}
