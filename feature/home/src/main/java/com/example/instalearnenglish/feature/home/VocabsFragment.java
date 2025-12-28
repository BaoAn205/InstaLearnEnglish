package com.example.instalearnenglish.feature.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instalearnenglish.feature.home.R;
import com.example.instalearnenglish.feature.home.model.Vocab;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class VocabsFragment extends Fragment {

    private RecyclerView rvVocabs;
    private ArchivedVocabAdapter adapter;
    private List<Vocab> vocabList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vocabs, container, false);

        rvVocabs = view.findViewById(R.id.rv_vocabs);
        rvVocabs.setLayoutManager(new LinearLayoutManager(getContext()));

        vocabList = new ArrayList<>();
        adapter = new ArchivedVocabAdapter(vocabList);
        rvVocabs.setAdapter(adapter);

        loadArchivedVocabs();

        return view;
    }

    private void loadArchivedVocabs() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(getContext(), "Please log in to see your archived words.", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(user.getUid()).collection("archived_vocabs")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        vocabList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Vocab vocab = document.toObject(Vocab.class);
                            vocabList.add(vocab);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "Failed to load archived words.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
