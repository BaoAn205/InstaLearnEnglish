package com.example.instalearnenglish.feature.station4;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ST4_VocabCardAdapter extends RecyclerView.Adapter<ST4_VocabCardAdapter.VocabViewHolder> {

    private final List<ST4_Vocabulary> vocabList;

    public ST4_VocabCardAdapter(List<ST4_Vocabulary> vocabList) {
        this.vocabList = vocabList;
    }

    @NonNull
    @Override
    public VocabViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.st4_flashcard_container, parent, false);
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
            cardFlipperContainer = itemView.findViewById(R.id.card_flipper_container);
            flashcardFront = itemView.findViewById(R.id.flashcard_front);
            flashcardBack = itemView.findViewById(R.id.flashcard_back);
            word = flashcardFront.findViewById(R.id.flashcard_word);
            pronunciation = flashcardBack.findViewById(R.id.flashcard_pronunciation);
            meaning = flashcardBack.findViewById(R.id.flashcard_meaning);
            image = flashcardBack.findViewById(R.id.flashcard_image);
            btnArchive = flashcardBack.findViewById(R.id.btn_archive_vocab);
        }

        void bind(ST4_Vocabulary vocab) {
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
                vocabData.put("station", 4);

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

            if (isFront) {
                out.setTarget(flashcardFront);
                in.setTarget(flashcardBack);
                flashcardFront.setCameraDistance(8000 * itemView.getContext().getResources().getDisplayMetrics().density);
                flashcardBack.setCameraDistance(8000 * itemView.getContext().getResources().getDisplayMetrics().density);
            } else {
                out.setTarget(flashcardBack);
                in.setTarget(flashcardFront);
                flashcardFront.setCameraDistance(8000 * itemView.getContext().getResources().getDisplayMetrics().density);
                flashcardBack.setCameraDistance(8000 * itemView.getContext().getResources().getDisplayMetrics().density);
            }

            out.start();
            in.start();

            isFront = !isFront;
        }
    }
}
