package com.example.instalearnenglish;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.material.R.attr;

import java.util.ArrayList;

public class ST1_LessonSimulationFragment extends Fragment {

    private LinearLayout chatContainer, optionsContainer, recordingContainer;
    private ScrollView scrollView;
    private MaterialButton recordButton;
    private TextView recognitionResultText;
    private FirebaseFirestore db;
    private String lessonId;
    private String currentStepId = "step1";
    private String nextStepIdAfterRecording; // To store the next step
    private SpeechRecognizer speechRecognizer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            lessonId = getArguments().getString("LESSON_ID");
        }
        return inflater.inflate(R.layout.st1_fragment_lesson_simulation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        chatContainer = view.findViewById(R.id.chat_container);
        optionsContainer = view.findViewById(R.id.options_container);
        scrollView = view.findViewById(R.id.chat_scroll_view);
        recordingContainer = view.findViewById(R.id.recording_container);
        recordButton = view.findViewById(R.id.record_button);
        recognitionResultText = view.findViewById(R.id.recognition_result_text);
        db = FirebaseFirestore.getInstance();
        setupSpeechRecognizer();
        if (lessonId != null && !currentStepId.isEmpty()) {
            loadStep(currentStepId);
        }
    }

    private void setupSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext());
        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onResults(Bundle results) {
                recordButton.setText("Tap to Speak");
                recordButton.setEnabled(true);
                ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (data != null && !data.isEmpty()) {
                    String recognizedText = data.get(0);
                    String correctAnswer = (String) recordButton.getTag();
                    displayRecognitionResult(recognizedText, correctAnswer);

                    // After showing the result, wait and then proceed to the next step.
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        fadeOutView(recordingContainer, () -> {
                            recognitionResultText.setVisibility(View.GONE); // Hide the result text
                            loadStep(nextStepIdAfterRecording); // Load the next logical step
                        });
                    }, 2500); // Wait 2.5s for the user to see the result
                }
            }

            @Override
            public void onReadyForSpeech(Bundle params) { recordButton.setText("Listening..."); recordButton.setEnabled(false); }
            @Override
            public void onError(int error) { recordButton.setText("Tap to Speak"); recordButton.setEnabled(true); }
            @Override
            public void onBeginningOfSpeech() {}
            @Override
            public void onEndOfSpeech() {}
            @Override
            public void onPartialResults(Bundle partialResults) {}
            @Override
            public void onEvent(int eventType, Bundle params) {}
            @Override
            public void onBufferReceived(byte[] buffer) {}
            @Override
            public void onRmsChanged(float rmsdB) {}
        });

        recordButton.setOnClickListener(v -> speechRecognizer.startListening(speechRecognizerIntent));
    }

    private void loadStep(String stepId) {
        if (stepId == null || stepId.isEmpty() || getContext() == null) {
            addBotMessage("You\'ve completed the simulation!");
            optionsContainer.setVisibility(View.GONE);
            return;
        }
        db.collection("journey_content").document(lessonId)
            .collection("simulation").document(stepId)
            .get()
            .addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    ST1_SimulationStep step = documentSnapshot.toObject(ST1_SimulationStep.class);
                    if (step != null) {
                        displayStep(step);
                    }
                }
            });
    }

    private void displayStep(ST1_SimulationStep step) {
        addBotMessage(step.getDialogue());
        optionsContainer.removeAllViews();
        if (step.getOptions() != null) {
            for (ST1_SimulationOption option : step.getOptions()) {
                MaterialButton button = new MaterialButton(requireContext(), null, attr.materialButtonOutlinedStyle);
                button.setText(option.getText());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 16, 0, 0);
                button.setLayoutParams(params);
                button.setOnClickListener(v -> handleOptionClick(option));
                optionsContainer.addView(button);
            }
            fadeInView(optionsContainer);
        }
    }

    private void handleOptionClick(ST1_SimulationOption selectedOption) {
        fadeOutView(optionsContainer, null);
        addUserMessage(selectedOption.getText());

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            addBotMessage(selectedOption.getResponse());

            if (selectedOption.isCorrect()) {
                // If correct, SAVE the next step ID and SHOW the recording button.
                // The flow now STOPS and waits for the user to speak.
                nextStepIdAfterRecording = selectedOption.getNextStep();
                recordButton.setTag(selectedOption.getText());
                fadeInView(recordingContainer);
            } else {
                // If wrong, proceed to the next step immediately after a delay.
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    currentStepId = selectedOption.getNextStep();
                    loadStep(currentStepId);
                }, 1200);
            }
        }, 600);
    }

    private void displayRecognitionResult(String recognized, String correct) {
        fadeInView(recognitionResultText);
        SpannableString spannable = new SpannableString(recognized);
        String[] recognizedWords = recognized.toLowerCase().split("\\s+");
        String[] correctWords = correct.toLowerCase().split("\\s+");

        int lastIndex = 0;
        for (int i = 0; i < recognizedWords.length; i++) {
            int color = ContextCompat.getColor(requireContext(), R.color.red_error);
            if (i < correctWords.length && recognizedWords[i].equalsIgnoreCase(correctWords[i])) {
                color = ContextCompat.getColor(requireContext(), R.color.green_correct);
            }
            int start = recognized.toLowerCase().indexOf(recognizedWords[i], lastIndex);
            if (start != -1) {
                int end = start + recognizedWords[i].length();
                spannable.setSpan(new ForegroundColorSpan(color), start, end, 0);
                lastIndex = end;
            }
        }
        recognitionResultText.setText(spannable);
    }

    private void addBotMessage(String text) {
        if (getContext() == null) return;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View botBubble = inflater.inflate(R.layout.st1_chat_bubble_bot, chatContainer, false);
        TextView messageText = botBubble.findViewById(R.id.chat_bubble_text);
        messageText.setText(text);
        chatContainer.addView(botBubble);
        scrollToBottom();
    }

    private void addUserMessage(String text) {
        if (getContext() == null) return;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View userBubble = inflater.inflate(R.layout.st1_chat_bubble_user, chatContainer, false);
        TextView messageText = userBubble.findViewById(R.id.chat_bubble_text);
        messageText.setText(text);
        chatContainer.addView(userBubble);
        scrollToBottom();
    }

    private void scrollToBottom() {
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
    }

    private void fadeInView(View view) {
        view.setAlpha(0f);
        view.setVisibility(View.VISIBLE);
        view.animate().alpha(1f).setDuration(300).start();
    }

    private void fadeOutView(View view, Runnable onEnd) {
        view.animate().alpha(0f).setDuration(300).withEndAction(() -> {
            view.setVisibility(View.GONE);
            if (onEnd != null) {
                onEnd.run();
            }
        }).start();
    }
}
