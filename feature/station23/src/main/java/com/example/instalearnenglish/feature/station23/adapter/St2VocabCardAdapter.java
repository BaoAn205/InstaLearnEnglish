package com.example.instalearnenglish.feature.station23.adapter;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instalearnenglish.feature.station23.R;
import com.example.instalearnenglish.feature.station23.model.St23Vocabulary;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class St2VocabCardAdapter extends RecyclerView.Adapter<St2VocabCardAdapter.VocabViewHolder> {

    private final List<St23Vocabulary> vocabList;

    public St2VocabCardAdapter(List<St23Vocabulary> vocabList) {
        this.vocabList = vocabList;
    }

    @NonNull
    @Override
    public VocabViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.st2_flashcard_container, parent, false);
        return new VocabViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VocabViewHolder holder, int position) {
        holder.bind(vocabList.get(position));
    }

    @Override
    public int getItemCount() {
        return vocabList.size();
    }

    static class VocabViewHolder extends RecyclerView.ViewHolder {
        private final View flashcardFront, flashcardBack;
        private final FrameLayout cardFlipperContainer;
        private final TextView word, pronunciation, meaning;
        private final ImageView image;
        private final ImageButton btnArchive;
        private boolean isFront = true;

        public VocabViewHolder(@NonNull View itemView) {
            super(itemView);
            cardFlipperContainer = itemView.findViewById(R.id.st2_card_flipper_container);
            flashcardFront = itemView.findViewById(R.id.st2_flashcard_front);
            flashcardBack = itemView.findViewById(R.id.st2_flashcard_back);
            
            word = flashcardFront.findViewById(R.id.st2_flashcard_word);
            pronunciation = flashcardBack.findViewById(R.id.st2_flashcard_pronunciation);
            meaning = flashcardBack.findViewById(R.id.st2_flashcard_meaning);
            image = flashcardBack.findViewById(R.id.st2_flashcard_image);
            btnArchive = flashcardBack.findViewById(R.id.btn_archive_vocab);
        }

        void bind(St23Vocabulary vocab) {
            word.setText(vocab.getWord());
            pronunciation.setText(vocab.getPronunciation());
            meaning.setText(vocab.getMeaning());

            cardFlipperContainer.setOnClickListener(v -> flipCard());

            btnArchive.setOnClickListener(v -> {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user == null) {
                    Toast.makeText(itemView.getContext(), "You must be logged in to archive words.", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Map<String, Object> vocabData = new HashMap<>();
                vocabData.put("name", vocab.getWord());
                vocabData.put("phonetic", vocab.getPronunciation());
                vocabData.put("vietnamese", vocab.getMeaning());
                vocabData.put("station", 2);

                db.collection("users").document(user.getUid()).collection("archived_vocabs")
                        .document(vocab.getWord())
                        .set(vocabData)
                        .addOnSuccessListener(aVoid -> Toast.makeText(itemView.getContext(), "Saved to Archive!", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(itemView.getContext(), "Failed to save.", Toast.LENGTH_SHORT).show());
            });
        }

        private void flipCard() {
            AnimatorSet out = (AnimatorSet) AnimatorInflater.loadAnimator(itemView.getContext(), R.animator.card_flip_out);
            AnimatorSet in = (AnimatorSet) AnimatorInflater.loadAnimator(itemView.getContext(), R.animator.card_flip_in);

            View targetFront = isFront ? flashcardFront : flashcardBack;
            View targetBack = isFront ? flashcardBack : flashcardFront;

            out.setTarget(targetFront);
            in.setTarget(targetBack);

            final float scale = itemView.getContext().getResources().getDisplayMetrics().density;
            targetFront.setCameraDistance(8000 * scale);
            targetBack.setCameraDistance(8000 * scale);

            out.start();
            in.start();

            isFront = !isFront;
        }
    }
}
