package com.example.instalearnenglish.feature.station23;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

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

public class St23VocabularyFragment extends Fragment implements St23VocabularyAdapter.OnVocabItemInteractionListener, TextToSpeech.OnInitListener {

    private RecyclerView recyclerView;
    private St23VocabularyAdapter adapter;
    private List<St23VocabItem> vocabList;
    private TextToSpeech tts;
    private Context mContext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tts = new TextToSpeech(mContext, this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.st23_fragment_vocabulary, container, false);
        recyclerView = view.findViewById(R.id.st23_rv_flashcards);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        loadVocabData();
        
        // Pass the themed context from the fragment to the adapter
        adapter = new St23VocabularyAdapter(mContext, vocabList, this);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void loadVocabData() {
        vocabList = new ArrayList<>();
        vocabList.add(new St23VocabItem("Boarding Pass", "Thẻ Lên Máy Bay"));
        vocabList.add(new St23VocabItem("Gate", "Cổng Chờ"));
        vocabList.add(new St23VocabItem("Delay", "Trì hoãn"));
        vocabList.add(new St23VocabItem("Departure", "Sự khởi hành"));
        vocabList.add(new St23VocabItem("Arrival", "Sự đến nơi"));
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The Language specified is not supported!");
            }
        } else {
            Log.e("TTS", "Initialization Failed!");
        }
    }

    @Override
    public void onSpeakClicked(String textToSpeak) {
        tts.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    @Override
    public void onCardClicked(final View cardFront, final View cardBack) {
        final boolean isShowingFront = cardFront.getAlpha() > 0;
        final View viewToFlip = isShowingFront ? cardFront : cardBack;
        final View newView = isShowingFront ? cardBack : cardFront;

        ObjectAnimator flipOut = ObjectAnimator.ofFloat(viewToFlip, "rotationY", 0f, 90f);
        flipOut.setDuration(250);
        flipOut.setInterpolator(new AccelerateDecelerateInterpolator());

        final ObjectAnimator flipIn = ObjectAnimator.ofFloat(newView, "rotationY", -90f, 0f);
        flipIn.setDuration(250);
        flipIn.setInterpolator(new DecelerateInterpolator());

        flipOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                viewToFlip.setAlpha(0f);
                newView.setAlpha(1f);
                flipIn.start();
            }
        });
        flipOut.start();
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
