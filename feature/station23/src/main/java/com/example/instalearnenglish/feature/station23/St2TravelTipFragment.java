package com.example.instalearnenglish.feature.station23;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.instalearnenglish.feature.station23.adapter.TravelTipAdapter;
import com.example.instalearnenglish.feature.station23.model.TravelTip;
import com.example.instalearnenglish.feature.station23.model.TravelTipProvider;

import java.util.List;
import java.util.Locale;

public class St2TravelTipFragment extends Fragment implements TravelTipAdapter.OnTipInteractionListener {

    private RecyclerView recyclerView;
    private TextView pageIndicator;
    private TravelTipAdapter adapter;
    private List<TravelTip> tips;
    private TextToSpeech tts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.st2_fragment_travel_tip, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.st2_tip_recycler_view);
        pageIndicator = view.findViewById(R.id.st2_tip_page_indicator);

        initializeTextToSpeech();
        setupRecyclerView();
    }

    private void initializeTextToSpeech() {
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
    }

    private void setupRecyclerView() {
        tips = TravelTipProvider.getStation2Tips();
        adapter = new TravelTipAdapter(getContext(), tips, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                View centerView = snapHelper.findSnapView(layoutManager);
                if (centerView != null) {
                    int pos = layoutManager.getPosition(centerView);
                    String indicatorText = (pos + 1) + " / " + tips.size();
                    pageIndicator.setText(indicatorText);
                }
            }
        });

        if (!tips.isEmpty()) {
            pageIndicator.setText("1 / " + tips.size());
        }
    }

    @Override
    public void onPlayAudio(String textToSpeak) {
        if (tts != null && !tts.isSpeaking()) {
            tts.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
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
