package com.example.instalearnenglish.feature.station1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton; 
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Locale;

public class ST1_LessonSimulationFragment extends Fragment {

    private LinearLayout chatContainer, optionsContainer, recordingContainer;
    private ScrollView scrollView;
    private MaterialButton recordButton;
    private TextView recognitionResultText;
    private FirebaseFirestore db;
    private String lessonId;
    private String currentStepId = "step1";
    private String nextStepIdAfterRecording;

    private TextToSpeech tts;
    private final Handler handler = new Handler(Looper.getMainLooper());

    private final ActivityResultLauncher<Intent> speechResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (!isAdded()) return;
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    ArrayList<String> results = result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (results != null && !results.isEmpty()) {
                        String spokenText = results.get(0);
                        String correctAnswer = (String) recordButton.getTag();
                        displayRecognitionResult(spokenText, correctAnswer);

                        handler.postDelayed(() -> {
                            if (!isAdded()) return;
                            fadeOutView(recordingContainer, () -> {
                                if (!isAdded()) return;
                                recognitionResultText.setVisibility(View.GONE);
                                loadStep(nextStepIdAfterRecording);
                            });
                        }, 2500);
                    }
                }
                recordButton.setText("Tap to Speak");
                recordButton.setEnabled(true);
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            lessonId = getArguments().getString("LESSON_ID");
        }
        tts = new TextToSpeech(getContext(), status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.US);
            } else {
                Log.e("TTS", "Initialization Failed!");
            }
        });
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

        recordButton.setOnClickListener(v -> startSpeechToText());

        if (lessonId != null && !currentStepId.isEmpty()) {
            loadStep(currentStepId);
        }
    }

    private void handleOptionClick(ST1_SimulationOption selectedOption) {
        if (!isAdded()) return;
        fadeOutView(optionsContainer, null);
        addUserMessage(selectedOption.getText());

        final View typingIndicator = addTypingIndicator();

        handler.postDelayed(() -> {
            if (!isAdded()) return;
            if(typingIndicator != null) chatContainer.removeView(typingIndicator);
            addBotMessage(selectedOption.getResponse(), true);
            nextStepIdAfterRecording = selectedOption.getNextStep();
            recordButton.setTag(selectedOption.getText());
            fadeInView(recordingContainer);
        }, 1200);
    }
    
    private View addTypingIndicator() {
        if (!isAdded()) return null;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View typingBubble = inflater.inflate(R.layout.st1_chat_bubble_typing, chatContainer, false);
        chatContainer.addView(typingBubble);
        scrollToBottom();
        return typingBubble;
    }

    private void startSpeechToText() {
        if (!isAdded()) return;
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en-US");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...");
        try {
            speechResultLauncher.launch(intent);
            recordButton.setText("Listening...");
            recordButton.setEnabled(false);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Speech recognition not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadStep(String stepId) {
        if (stepId == null || stepId.isEmpty()) {
            addBotMessage("You\'ve completed the simulation!", false);
            optionsContainer.setVisibility(View.GONE);
            return;
        }
        db.collection("journey_content").document(lessonId)
                .collection("simulation").document(stepId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (!isAdded()) return;
                    if (documentSnapshot.exists()) {
                        ST1_SimulationStep step = documentSnapshot.toObject(ST1_SimulationStep.class);
                        if (step != null) {
                            displayStep(step);
                        }
                    }
                });
    }

    private void displayStep(ST1_SimulationStep step) {
        if (!isAdded()) return;
        addBotMessage(step.getDialogue(), true);
        optionsContainer.removeAllViews();
        if (step.getOptions() != null) {
            for (ST1_SimulationOption option : step.getOptions()) {
                MaterialButton button = new MaterialButton(requireContext());
                button.setText(option.getText());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 8, 0, 8);
                button.setLayoutParams(params);
                button.setOnClickListener(v -> handleOptionClick(option));
                optionsContainer.addView(button);
            }
            fadeInView(optionsContainer);
        }
    }

    private void displayRecognitionResult(String recognized, String correct) {
        if (!isAdded()) return;
        recognitionResultText.setVisibility(View.VISIBLE);
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

    private void addBotMessage(String text, boolean canSpeak) {
        if (!isAdded()) return;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View botBubble = inflater.inflate(R.layout.st1_chat_bubble_bot, chatContainer, false);
        TextView messageText = botBubble.findViewById(R.id.chat_bubble_text);
        ImageButton speakButton = botBubble.findViewById(R.id.btn_speak_bot_message);
        messageText.setText(text);

        if (canSpeak) {
            speakButton.setVisibility(View.VISIBLE);
            speakButton.setOnClickListener(v -> tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null));
        } else {
            speakButton.setVisibility(View.GONE);
        }

        chatContainer.addView(botBubble);
        scrollToBottom();
    }

    private void addUserMessage(String text) {
        if (!isAdded()) return;
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

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
