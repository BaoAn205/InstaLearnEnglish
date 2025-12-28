package com.example.instalearnenglish.feature.station23;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instalearnenglish.feature.station23.adapter.St23VocabularyAdapter;
import com.example.instalearnenglish.feature.station23.model.St23VocabItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class St23VocabularyFragment extends Fragment implements St23VocabularyAdapter.OnVocabItemInteractionListener {

    private RecyclerView recyclerView;
    private St23VocabularyAdapter adapter;
    private List<St23VocabItem> vocabList;
    private TextToSpeech tts;
    private AnimatorSet frontAnim, backAnim;
    private boolean isFront = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.st23_fragment_vocabulary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Corrected the ID to match the layout file
        recyclerView = view.findViewById(R.id.st23_rv_flashcards);

        // Initialize TextToSpeech
        tts = new TextToSpeech(getContext(), status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = tts.setLanguage(Locale.US);
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(getContext(), "Language not supported", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "TTS Initialization failed", Toast.LENGTH_SHORT).show();
            }
        });

        // Prepare animations
        float scale = getResources().getDisplayMetrics().density;
        view.setCameraDistance(8000 * scale);
        // TODO: The animator files might need to be created if they don't exist in station23 module
        // frontAnim = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.card_flip_out);
        // backAnim = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.card_flip_in);

        // Setup RecyclerView
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        vocabList = new ArrayList<>();
        vocabList.add(new St23VocabItem("Boarding Pass", "", 0, "Thẻ Lên Máy Bay", ""));
        vocabList.add(new St23VocabItem("Gate", "", 0, "Cổng Chờ", ""));
        vocabList.add(new St23VocabItem("Delay", "", 0, "Trì hoãn", ""));
        vocabList.add(new St23VocabItem("Departure", "", 0, "Sự khởi hành", ""));
        vocabList.add(new St23VocabItem("Arrival", "", 0, "Sự đến nơi", ""));

        adapter = new St23VocabularyAdapter(getContext(), vocabList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSpeakClicked(String textToSpeak) {
        if (tts != null && !tts.isSpeaking()) {
            tts.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    @Override
    public void onCardClicked(View cardFront, View cardBack) {
        // This logic is for the old flashcard UI and might not be used anymore
        if (isFront) {
            if (frontAnim != null) frontAnim.setTarget(cardFront);
            if (backAnim != null) backAnim.setTarget(cardBack);
            if (frontAnim != null) frontAnim.start();
            if (backAnim != null) backAnim.start();
            isFront = false;
        } else {
            if (frontAnim != null) frontAnim.setTarget(cardBack);
            if (backAnim != null) backAnim.setTarget(cardFront);
            if (backAnim != null) backAnim.start();
            if (frontAnim != null) frontAnim.start();
            isFront = true;
        }
    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}
