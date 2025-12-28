package com.example.instalearnenglish.feature.station23;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.instalearnenglish.feature.station23.adapter.St23VocabCategoryAdapter;
import com.example.instalearnenglish.feature.station23.adapter.St23VocabItemAdapter;
import com.example.instalearnenglish.feature.station23.model.St23VocabCategory;
import com.example.instalearnenglish.feature.station23.model.St23VocabItem;
import com.example.instalearnenglish.feature.station23.model.St23VocabProvider;

import java.util.List;

public class St2VocabularyFragment extends Fragment implements St23VocabItemAdapter.OnItemClickListener {

    private RecyclerView rvCategories;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Use the new layout with a vertical RecyclerView
        return inflater.inflate(R.layout.st2_fragment_vocabulary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvCategories = view.findViewById(R.id.st2_rv_categories);
        rvCategories.setLayoutManager(new LinearLayoutManager(getContext()));

        // Find the parent ViewPager that manages the tabs
        ViewPager2 parentViewPager = requireActivity().findViewById(R.id.viewPager);

        // Load categorized data
        List<St23VocabCategory> categoryList = St23VocabProvider.getStation2CategorizedVocabulary();

        // Create and set the category adapter
        St23VocabCategoryAdapter categoryAdapter = new St23VocabCategoryAdapter(categoryList, this, parentViewPager);
        rvCategories.setAdapter(categoryAdapter);
    }

    @Override
    public void onItemClick(St23VocabItem item, ImageView sharedImageView) {
        // For now, just show a Toast. In the future, we can open a detail Activity like Station 1.
        Toast.makeText(getContext(), item.getName(), Toast.LENGTH_SHORT).show();
        
        // Example of opening detail activity (commented out for now)
        /*
        Intent intent = new Intent(getActivity(), St2VocabDetailActivity.class);
        intent.putExtra("EXTRA_NAME", item.getName());
        // ... pass other data

        String transitionName = ViewCompat.getTransitionName(sharedImageView);
        intent.putExtra("EXTRA_TRANSITION_NAME", transitionName);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            requireActivity(),
            sharedImageView,
            transitionName
        );
        startActivity(intent, options.toBundle());
        */
    }
}
