package com.example.instalearnenglish.feature.station1.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.instalearnenglish.feature.station1.R;
import com.example.instalearnenglish.feature.station1.model.VocabCategory;

import java.util.List;

public class VocabCategoryAdapter extends RecyclerView.Adapter<VocabCategoryAdapter.ViewHolder> {

    private final List<VocabCategory> categories;
    private final VocabItemAdapter.OnItemClickListener itemClickListener;
    private final ViewPager2 parentViewPager; // Reference to the parent ViewPager

    public VocabCategoryAdapter(List<VocabCategory> categories, VocabItemAdapter.OnItemClickListener itemClickListener, ViewPager2 parentViewPager) {
        this.categories = categories;
        this.itemClickListener = itemClickListener;
        this.parentViewPager = parentViewPager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.st1_vocab_category_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(categories.get(position), itemClickListener, parentViewPager);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvCategoryTitle;
        private final RecyclerView rvVocabItems;

        ViewHolder(View view) {
            super(view);
            tvCategoryTitle = view.findViewById(R.id.tv_category_title);
            rvVocabItems = view.findViewById(R.id.rv_vocab_items);
        }

        void bind(VocabCategory category, VocabItemAdapter.OnItemClickListener itemClickListener, final ViewPager2 parentViewPager) {
            tvCategoryTitle.setText(category.getTitle());

            rvVocabItems.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            VocabItemAdapter itemAdapter = new VocabItemAdapter(category.getItems(), itemClickListener);
            rvVocabItems.setAdapter(itemAdapter);

            // Add the conflict resolver listener
            rvVocabItems.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                        parentViewPager.setUserInputEnabled(false);
                    } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        parentViewPager.setUserInputEnabled(true);
                    }
                }
            });
        }
    }
}
