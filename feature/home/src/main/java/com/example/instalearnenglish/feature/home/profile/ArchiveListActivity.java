package com.example.instalearnenglish.feature.home.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instalearnenglish.feature.home.R;
import com.example.instalearnenglish.feature.home.adapter.SavedItemAdapter;
import com.example.instalearnenglish.feature.home.model.SavedItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ArchiveListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView tvTitle;
    private TextView tvEmpty;
    private ImageButton btnBack;
    private SavedItemAdapter adapter;
    private List<SavedItem> itemList = new ArrayList<>();
    private String type; // "TIP" or "VOCAB"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive_list);

        type = getIntent().getStringExtra("EXTRA_TYPE");
        String title = getIntent().getStringExtra("EXTRA_TITLE");

        recyclerView = findViewById(R.id.rv_archive_items);
        tvTitle = findViewById(R.id.tv_archive_title);
        tvEmpty = findViewById(R.id.tv_empty_archive);
        btnBack = findViewById(R.id.btn_back_archive);

        tvTitle.setText(title);
        btnBack.setOnClickListener(v -> finish());

        setupRecyclerView();
        fetchData();
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SavedItemAdapter(itemList, this::navigateToItem);
        recyclerView.setAdapter(adapter);
    }

    private void fetchData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        FirebaseFirestore.getInstance().collection("users").document(user.getUid())
                .collection("saved_items")
                .whereEqualTo("type", type)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    itemList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        itemList.add(doc.toObject(SavedItem.class));
                    }
                    adapter.notifyDataSetChanged();
                    tvEmpty.setVisibility(itemList.isEmpty() ? View.VISIBLE : View.GONE);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error loading data", Toast.LENGTH_SHORT).show();
                });
    }

    private void navigateToItem(SavedItem item) {
        Intent intent = new Intent();
        String className;
        switch (item.getStationId()) {
            case 1: className = "com.example.instalearnenglish.feature.station1.Station1Activity"; break;
            case 2:
            case 3: className = "com.example.instalearnenglish.feature.station23.Station23Activity"; break;
            case 4: className = "com.example.instalearnenglish.feature.station4.Station4Activity"; break;
            case 5: className = "com.example.instalearnenglish.feature.station5.Station5Activity"; break;
            default: return;
        }
        intent.setClassName(this, className);
        intent.putExtra("LEVEL", item.getStationId());
        intent.putExtra("EXTRA_START_TAB", item.getType().equals("TIP") ? 0 : 1);
        intent.putExtra("EXTRA_ITEM_POSITION", item.getPosition());
        startActivity(intent);
    }
}
