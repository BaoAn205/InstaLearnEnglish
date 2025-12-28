package com.example.instalearnenglish.feature.station23.adapter;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instalearnenglish.feature.station23.R;
import com.example.instalearnenglish.feature.station23.model.St23Vocabulary; // Assuming this model will be created

import java.util.List;

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
        private boolean isFront = true;

        public VocabViewHolder(@NonNull View itemView) {
            super(itemView);
            cardFlipperContainer = itemView.findViewById(R.id.st2_card_flipper_container);
            flashcardFront = itemView.findViewById(R.id.st2_flashcard_front);
            flashcardBack = itemView.findViewById(R.id.st2_flashcard_back);
            
            // Views in front and back layouts
            word = flashcardFront.findViewById(R.id.st2_flashcard_word);
            pronunciation = flashcardBack.findViewById(R.id.st2_flashcard_pronunciation);
            meaning = flashcardBack.findViewById(R.id.st2_flashcard_meaning);
            image = flashcardBack.findViewById(R.id.st2_flashcard_image);
        }

        void bind(St23Vocabulary vocab) {
            word.setText(vocab.getWord());
            pronunciation.setText(vocab.getPronunciation());
            meaning.setText(vocab.getMeaning());
            // TODO: Load image from URL using Glide/Picasso for 'image' ImageView

            cardFlipperContainer.setOnClickListener(v -> flipCard());
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
