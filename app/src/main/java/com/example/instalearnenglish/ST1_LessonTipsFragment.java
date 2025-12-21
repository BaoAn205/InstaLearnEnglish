package com.example.instalearnenglish;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.transition.TransitionManager;
import androidx.transition.AutoTransition;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.List;
import java.util.Map;

public class ST1_LessonTipsFragment extends Fragment {

    private LinearLayout tipsContainer;
    private FirebaseFirestore db;
    private String lessonId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            lessonId = getArguments().getString("LESSON_ID");
        }
        return inflater.inflate(R.layout.st1_fragment_lesson_tips, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tipsContainer = view.findViewById(R.id.tips_container);
        db = FirebaseFirestore.getInstance();

        if (lessonId != null && !lessonId.isEmpty()) {
            loadTipsFromFirestore();
        }
    }

    private void loadTipsFromFirestore() {
        db.collection("journey_content").document(lessonId)
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult() != null) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<Map<String, Object>> tipsList = (List<Map<String, Object>>) document.get("tips");
                        if (tipsList != null) {
                            for (Map<String, Object> tipMap : tipsList) {
                                String title = (String) tipMap.get("title");
                                String content = (String) tipMap.get("content");
                                if (title != null && content != null) {
                                    ST1_Tip ST1_Tip = new ST1_Tip(title, content);
                                    addTipCardToView(ST1_Tip);
                                }
                            }
                        }
                    }
                } else {
                    // Handle the error
                }
            });
    }

    private void addTipCardToView(ST1_Tip ST1_Tip) {
        if (getContext() == null) return;

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View cardView = inflater.inflate(R.layout.st1_tip_card_item, tipsContainer, false);

        TextView titleView = cardView.findViewById(R.id.tip_title);
        TextView detailView = cardView.findViewById(R.id.tip_detail);

        titleView.setText(ST1_Tip.getTitle());
        detailView.setText(ST1_Tip.getContent());

        cardView.setOnClickListener(v -> {
            TransitionManager.beginDelayedTransition(tipsContainer, new AutoTransition());
            if (detailView.getVisibility() == View.GONE) {
                detailView.setVisibility(View.VISIBLE);
            } else {
                detailView.setVisibility(View.GONE);
            }
        });

        tipsContainer.addView(cardView);
    }
}
