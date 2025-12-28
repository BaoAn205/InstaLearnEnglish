package com.example.instalearnenglish.feature.station1;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;

import com.example.instalearnenglish.feature.home.tools.DictionaryDialogFragment;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ST1_VocabDetailActivity extends AppCompatActivity {

    public static final String EXTRA_NAME = "EXTRA_NAME";
    public static final String EXTRA_PHONETIC = "EXTRA_PHONETIC";
    public static final String EXTRA_IMAGE_RES_ID = "EXTRA_IMAGE_RES_ID";
    public static final String EXTRA_VIETNAMESE = "EXTRA_VIETNAMESE";
    public static final String EXTRA_EXAMPLE = "EXTRA_EXAMPLE";

    private static final String NOTES_PREFS = "MyNotes";
    private TextToSpeech tts;
    private EditText etMyNotes;
    private String vocabName;
    private String phonetic;
    private String vietnamese;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.st1_activity_vocab_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        ImageView ivDetailImage = findViewById(R.id.iv_detail_image);
        TextView tvDetailPhonetic = findViewById(R.id.tv_detail_phonetic);
        TextView tvDetailVietnamese = findViewById(R.id.tv_detail_vietnamese);
        TextView tvDetailExample = findViewById(R.id.tv_detail_example);
        ImageButton btnSpeak = findViewById(R.id.btn_detail_speak);
        ImageButton btnDictionary = findViewById(R.id.btn_dictionary);
        ImageButton btnArchive = findViewById(R.id.btn_archive);
        etMyNotes = findViewById(R.id.et_my_notes);

        vocabName = getIntent().getStringExtra(EXTRA_NAME);
        phonetic = getIntent().getStringExtra(EXTRA_PHONETIC);
        vietnamese = getIntent().getStringExtra(EXTRA_VIETNAMESE);
        String example = getIntent().getStringExtra(EXTRA_EXAMPLE);
        int imageResId = getIntent().getIntExtra(EXTRA_IMAGE_RES_ID, 0);
        String transitionName = getIntent().getStringExtra("EXTRA_TRANSITION_NAME");

        collapsingToolbar.setTitle(vocabName);
        tvDetailPhonetic.setText(phonetic);
        tvDetailVietnamese.setText(vietnamese);
        tvDetailExample.setText("\"" + example + "\"");

        if (imageResId != 0) {
            ivDetailImage.setImageResource(imageResId);
        }

        if (transitionName != null) {
            ViewCompat.setTransitionName(ivDetailImage, transitionName);
        }

        loadNote();

        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.US);
            } else {
                Log.e("TTS", "Initialization Failed!");
            }
        });

        btnSpeak.setOnClickListener(v -> {
            if (vocabName != null) {
                tts.speak(vocabName, TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });

        btnDictionary.setOnClickListener(v -> {
            DictionaryDialogFragment dialogFragment = new DictionaryDialogFragment();
            dialogFragment.show(getSupportFragmentManager(), "DictionaryDialog");
        });

        btnArchive.setOnClickListener(v -> archiveVocab());
    }

    private void archiveVocab() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "You must be logged in to archive words.", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> vocabData = new HashMap<>();
        vocabData.put("name", vocabName);
        vocabData.put("phonetic", phonetic);
        vocabData.put("vietnamese", vietnamese);

        db.collection("users").document(user.getUid()).collection("archived_vocabs")
                .document(vocabName)
                .set(vocabData)
                .addOnSuccessListener(aVoid -> Toast.makeText(ST1_VocabDetailActivity.this, "Saved to Archive!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(ST1_VocabDetailActivity.this, "Failed to save.", Toast.LENGTH_SHORT).show());
    }

    private void loadNote() {
        if (vocabName == null || vocabName.isEmpty()) return;
        SharedPreferences prefs = getSharedPreferences(NOTES_PREFS, Context.MODE_PRIVATE);
        String noteKey = "note_" + vocabName;
        String savedNote = prefs.getString(noteKey, "");
        etMyNotes.setText(savedNote);
    }

    private void saveNote() {
        if (vocabName == null || vocabName.isEmpty()) return;
        SharedPreferences.Editor editor = getSharedPreferences(NOTES_PREFS, Context.MODE_PRIVATE).edit();
        String noteKey = "note_" + vocabName;
        String currentNote = etMyNotes.getText().toString();
        editor.putString(noteKey, currentNote);
        editor.apply();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveNote(); // Save notes when the user leaves the screen
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}
