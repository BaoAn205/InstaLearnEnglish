package com.example.instalearnenglish.feature.station1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

// Explicitly import the class to help the IDE
import com.example.instalearnenglish.feature.station1.ST1_Vocabulary;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ST1_LessonVocabFragment extends Fragment {

    private ViewPager2 vocabViewPager;
    private FirebaseFirestore db;
    private String lessonId;
    private final List<ST1_Vocabulary> vocabList = new ArrayList<>();
    private ST1_VocabCardAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            lessonId = getArguments().getString("LESSON_ID");
        }
        return inflater.inflate(R.layout.st1_fragment_lesson_vocab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        vocabViewPager = view.findViewById(R.id.vocab_view_pager);
        adapter = new ST1_VocabCardAdapter(vocabList);
        vocabViewPager.setAdapter(adapter);

        // Get the parent ViewPager (for tabs) - Use the correct ID from the Activity's layout
        ViewPager2 parentViewPager = requireActivity().findViewById(R.id.viewPager);

        // Add a listener to the child ViewPager (for flashcards)
        vocabViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrollStateChanged(int state) {
                // When the user is dragging the flashcard pager, disable the parent pager
                if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
                    parentViewPager.setUserInputEnabled(false);
                } else if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    // When the drag is finished, re-enable the parent pager
                    parentViewPager.setUserInputEnabled(true);
                }
            }
        });

        db = FirebaseFirestore.getInstance();

        if (lessonId != null && !lessonId.isEmpty()) {
            loadVocabFromFirestore();
        }
    }

    private void loadVocabFromFirestore() {
        db.collection("journey_content").document(lessonId)
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult() != null) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<Map<String, Object>> vocabData = (List<Map<String, Object>>) document.get("vocabulary");
                        if (vocabData != null) {
                            vocabList.clear();
                            for (Map<String, Object> vocabMap : vocabData) {
                                String word = (String) vocabMap.get("word");
                                String pronunciation = (String) vocabMap.get("pronunciation");
                                String meaning = (String) vocabMap.get("meaning");
                                String imageUrl = (String) vocabMap.get("imageUrl");
                                if (word != null) {
                                    vocabList.add(new ST1_Vocabulary(word, pronunciation, meaning, imageUrl));
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    // Handle the error
                }
            });
    }
}
