package com.example.instalearnenglish.feature.station23;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.instalearnenglish.feature.station23.adapter.St3VocabCategoryAdapter;
import com.example.instalearnenglish.feature.station23.adapter.St3VocabItemAdapter;
import com.example.instalearnenglish.feature.station23.model.St23VocabCategory;
import com.example.instalearnenglish.feature.station23.model.St23VocabItem;
import com.example.instalearnenglish.feature.station23.model.St23VocabProvider;

import java.util.List;

public class St3VocabularyFragment extends Fragment implements St3VocabItemAdapter.OnItemClickListener {

    private RecyclerView rvCategories;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.st3_fragment_vocabulary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvCategories = view.findViewById(R.id.st3_rv_categories);
        rvCategories.setLayoutManager(new LinearLayoutManager(getContext()));

        // Find the parent ViewPager that manages the tabs
        ViewPager2 parentViewPager = requireActivity().findViewById(R.id.viewPager);

        // Load categorized data for Station 3
        List<St23VocabCategory> categoryList = St23VocabProvider.getStation3CategorizedVocabulary();

        // Create and set the category adapter
        St3VocabCategoryAdapter categoryAdapter = new St3VocabCategoryAdapter(categoryList, this, parentViewPager);
        rvCategories.setAdapter(categoryAdapter);
    }

    @Override
    public void onItemClick(St23VocabItem item, ImageView sharedImageView) {
        // For now, just show a Toast. In the future, we can open a detail Activity.
        Toast.makeText(getContext(), item.getName(), Toast.LENGTH_SHORT).show();
    }
}
