package com.example.instalearnenglish.feature.station4.adapter;

import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instalearnenglish.feature.station4.R;
import com.example.instalearnenglish.feature.station4.model.Tip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ST4_TipsPagerAdapter extends RecyclerView.Adapter<ST4_TipsPagerAdapter.ViewHolder> {

    private final List<Tip> tips;
    private final TextToSpeech tts;
    // Lưu giữ trạng thái những trang nào đang mở bản dịch để không bị mất khi vuốt
    private final Set<Integer> expandedPositions = new HashSet<>();

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
        private final TextView tvTipTranslation;
        private final ImageButton btnSpeak;
        private final ImageButton btnArchive;
        private final Button btnShowTranslation;

        ViewHolder(View view) {
            super(view);
            ivTipIcon = view.findViewById(R.id.iv_tip_icon);
            tvTipTitle = view.findViewById(R.id.tv_tip_title);
            tvTipContent = view.findViewById(R.id.tv_tip_content);
            tvTipTranslation = view.findViewById(R.id.tv_tip_translation);
            btnSpeak = view.findViewById(R.id.btn_speak_tip);
            btnArchive = view.findViewById(R.id.btn_archive_tip);
            btnShowTranslation = view.findViewById(R.id.btn_show_translation);
        }

        void bind(final Tip tip, final TextToSpeech tts, int position) {
            tvTipTitle.setText(tip.getTitle());
            tvTipContent.setText(tip.getContent());
            tvTipTranslation.setText(tip.getVietnameseMeaning());
            ivTipIcon.setImageResource(tip.getImageResId());

            // Kiểm tra xem vị trí này có đang được mở bản dịch không
            if (expandedPositions.contains(position)) {
                tvTipTranslation.setVisibility(View.VISIBLE);
                btnShowTranslation.setText("Ẩn bản dịch");
            } else {
                tvTipTranslation.setVisibility(View.GONE);
                btnShowTranslation.setText("Xem bản dịch");
            }

            btnSpeak.setOnClickListener(v -> {
                if (tts != null) {
                    String textToSpeak = tip.getTitle() + ". " + tip.getContent();
                    tts.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
                }
            });

            btnShowTranslation.setOnClickListener(v -> {
                if (tvTipTranslation.getVisibility() == View.GONE) {
                    tvTipTranslation.setVisibility(View.VISIBLE);
                    btnShowTranslation.setText("Ẩn bản dịch");
                    expandedPositions.add(position);
                } else {
                    tvTipTranslation.setVisibility(View.GONE);
                    btnShowTranslation.setText("Xem bản dịch");
                    expandedPositions.remove(position);
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
                tipData.put("vietnamese", tip.getVietnameseMeaning());
                tipData.put("station", 4);

                db.collection("users").document(user.getUid()).collection("archived_tips")
                        .document(tip.getTitle())
                        .set(tipData)
                        .addOnSuccessListener(aVoid -> Toast.makeText(itemView.getContext(), "Saved to Archive!", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(itemView.getContext(), "Failed to save.", Toast.LENGTH_SHORT).show());
            });
        }
    }
}
