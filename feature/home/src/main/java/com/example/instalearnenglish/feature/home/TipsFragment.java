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
import com.example.instalearnenglish.feature.home.model.Tip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class TipsFragment extends Fragment {

    private RecyclerView rvTips;
    private ArchivedTipAdapter adapter;
    private List<Tip> tipList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tips, container, false);

        rvTips = view.findViewById(R.id.rv_tips);
        rvTips.setLayoutManager(new LinearLayoutManager(getContext()));

        tipList = new ArrayList<>();
        adapter = new ArchivedTipAdapter(tipList);
        rvTips.setAdapter(adapter);

        loadArchivedTips();

        return view;
    }

    private void loadArchivedTips() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(getContext(), "Please log in to see your archived tips.", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(user.getUid()).collection("archived_tips")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        tipList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Tip tip = document.toObject(Tip.class);
                            tipList.add(tip);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "Failed to load archived tips.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
