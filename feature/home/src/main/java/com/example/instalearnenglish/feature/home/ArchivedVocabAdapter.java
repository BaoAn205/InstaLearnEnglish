package com.example.instalearnenglish.feature.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instalearnenglish.feature.home.R;
import com.example.instalearnenglish.feature.home.model.Vocab;

import java.util.List;

public class ArchivedVocabAdapter extends RecyclerView.Adapter<ArchivedVocabAdapter.VocabViewHolder> {

    private final List<Vocab> vocabList;

    public ArchivedVocabAdapter(List<Vocab> vocabList) {
        this.vocabList = vocabList;
    }

    @NonNull
    @Override
    public VocabViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_archived_vocab, parent, false);
        return new VocabViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VocabViewHolder holder, int position) {
        Vocab vocab = vocabList.get(position);
        holder.tvVocabName.setText(vocab.getName());
        holder.tvVocabPhonetic.setText(vocab.getPhonetic());
        holder.tvVocabVietnamese.setText(vocab.getVietnamese());
    }

    @Override
    public int getItemCount() {
        return vocabList.size();
    }

    static class VocabViewHolder extends RecyclerView.ViewHolder {
        TextView tvVocabName;
        TextView tvVocabPhonetic;
        TextView tvVocabVietnamese;

        public VocabViewHolder(@NonNull View itemView) {
            super(itemView);
            tvVocabName = itemView.findViewById(R.id.tv_vocab_name);
            tvVocabPhonetic = itemView.findViewById(R.id.tv_vocab_phonetic);
            tvVocabVietnamese = itemView.findViewById(R.id.tv_vocab_vietnamese);
        }
    }
}
