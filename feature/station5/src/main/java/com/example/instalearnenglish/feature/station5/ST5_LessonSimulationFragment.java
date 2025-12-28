package com.example.instalearnenglish.feature.station5;

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
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ST5_LessonSimulationFragment extends Fragment {

    private LinearLayout chatContainer, optionsContainer, recordingContainer, scenarioSelectionContainer;
    private View chatMainLayout;
    private ScrollView scrollView;
    private MaterialButton recordButton;
    private TextView recognitionResultText, tvTargetSentence;
    private String nextStepIdAfterRecording;

    private TextToSpeech tts;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Map<String, ST5_SimulationStep> mockData;

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
        tts = new TextToSpeech(getContext(), status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.US);
            }
        });
        return inflater.inflate(R.layout.st5_fragment_lesson_simulation, container, false);
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
        tvTargetSentence = view.findViewById(R.id.tv_target_sentence);
        
        scenarioSelectionContainer = view.findViewById(R.id.scenario_selection_container);
        chatMainLayout = view.findViewById(R.id.chat_main_layout);

        initMockData();
        setupScenarioSelection(view);

        recordButton.setOnClickListener(v -> startSpeechToText());
    }

    private void setupScenarioSelection(View view) {
        view.findViewById(R.id.card_scenario_1).setOnClickListener(v -> startScenario("sc1_step1"));
        view.findViewById(R.id.card_scenario_2).setOnClickListener(v -> startScenario("sc2_step1"));
    }

    private void startScenario(String startStepId) {
        scenarioSelectionContainer.setVisibility(View.GONE);
        chatMainLayout.setVisibility(View.VISIBLE);
        chatContainer.removeAllViews();
        loadStep(startStepId);
    }

    private void initMockData() {
        mockData = new HashMap<>();

        // --- SCENARIO 1: DINING AT A RESTAURANT ---
        ST5_SimulationOption sc1_1_1 = new ST5_SimulationOption("Hello, I would like a table for two, please.", true, "Certainly! Right this way. Here are your menus.", "sc1_step2");
        ST5_SimulationOption sc1_1_2 = new ST5_SimulationOption("Hi, do you have any free tables for one person?", true, "Yes, we do. Please follow me to your table.", "sc1_step2");
        mockData.put("sc1_step1", new ST5_SimulationStep("Welcome! Do you have a reservation?", Arrays.asList(sc1_1_1, sc1_1_2)));

        ST5_SimulationOption sc1_2_1 = new ST5_SimulationOption("I'll have the grilled chicken and a glass of water.", true, "Excellent choice. Would you like any dessert later?", "sc1_step3");
        ST5_SimulationOption sc1_2_2 = new ST5_SimulationOption("Could I just have a salad and an orange juice, please?", true, "Of course. I'll bring your drink first.", "sc1_step3");
        mockData.put("sc1_step2", new ST5_SimulationStep("Are you ready to order, or do you need a few more minutes?", Arrays.asList(sc1_2_1, sc1_2_2)));

        ST5_SimulationOption sc1_3_1 = new ST5_SimulationOption("No, thank you. Could I have the bill, please?", true, "Certainly. I'll be right back with the bill.", "end");
        mockData.put("sc1_step3", new ST5_SimulationStep("How was your meal? Would you like to see the dessert menu?", Arrays.asList(sc1_3_1)));

        // --- SCENARIO 2: SHOPPING FOR SOUVENIRS ---
        ST5_SimulationOption sc2_1_1 = new ST5_SimulationOption("Hi, how much is this beautiful wooden statue?", true, "This one is $25. It's handmade by local artists.", "sc2_step2");
        ST5_SimulationOption sc2_1_2 = new ST5_SimulationOption("Excuse me, do you have this t-shirt in a larger size?", true, "Let me check... Yes, we have it in Large and XL.", "sc2_step2");
        mockData.put("sc2_step1", new ST5_SimulationStep("Hello! Feel free to look around. Let me know if you need anything.", Arrays.asList(sc2_1_1, sc2_1_2)));

        ST5_SimulationOption sc2_2_1 = new ST5_SimulationOption("That's a bit expensive. Can you give me a discount?", true, "Since you are buying two, I can give them to you for $40.", "sc2_step3");
        ST5_SimulationOption sc2_2_2 = new ST5_SimulationOption("Okay, I'll take it. Do you accept credit cards?", true, "Yes, we accept all major credit cards and cash.", "sc2_step3");
        mockData.put("sc2_step2", new ST5_SimulationStep("It's one of our best-sellers! Are you interested in buying it?", Arrays.asList(sc2_2_1, sc2_2_2)));

        ST5_SimulationOption sc2_3_1 = new ST5_SimulationOption("Perfect, here is my card. Thank you!", true, "Thank you! Here is your receipt. Have a great day!", "end");
        mockData.put("sc2_step3", new ST5_SimulationStep("Great! I'll wrap this up for you. Anything else today?", Arrays.asList(sc2_3_1)));

        mockData.put("end", new ST5_SimulationStep("Thank you for visiting! Hope to see you again.", null));
    }

    private void loadStep(String stepId) {
        if (stepId == null || stepId.isEmpty() || stepId.equals("end")) {
            addBotMessage("Well done! You've completed the simulation.", false);
            optionsContainer.setVisibility(View.GONE);
            recordingContainer.setVisibility(View.GONE);
            
            MaterialButton btnBack = new MaterialButton(requireContext());
            btnBack.setText("Return to Scenarios");
            btnBack.setOnClickListener(v -> {
                chatMainLayout.setVisibility(View.GONE);
                scenarioSelectionContainer.setVisibility(View.VISIBLE);
            });
            optionsContainer.removeAllViews();
            optionsContainer.addView(btnBack);
            optionsContainer.setVisibility(View.VISIBLE);
            return;
        }

        ST5_SimulationStep step = mockData.get(stepId);
        if (step != null) {
            displayStep(step);
        }
    }

    private void displayStep(ST5_SimulationStep step) {
        if (!isAdded()) return;
        addBotMessage(step.getDialogue(), true);
        optionsContainer.removeAllViews();
        if (step.getOptions() != null) {
            for (ST5_SimulationOption option : step.getOptions()) {
                MaterialButton button = new MaterialButton(requireContext());
                button.setText(option.getText());
                button.setAllCaps(false);
                button.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 8, 0, 8);
                button.setLayoutParams(params);
                button.setOnClickListener(v -> handleOptionClick(option));
                optionsContainer.addView(button);
            }
            fadeInView(optionsContainer);
        }
    }

    private void handleOptionClick(ST5_SimulationOption selectedOption) {
        if (!isAdded()) return;
        fadeOutView(optionsContainer, null);
        addUserMessage(selectedOption.getText());

        final View typingIndicator = addTypingIndicator();

        handler.postDelayed(() -> {
            if (!isAdded()) return;
            if(typingIndicator != null) chatContainer.removeView(typingIndicator);
            addBotMessage(selectedOption.getResponse(), true);
            nextStepIdAfterRecording = selectedOption.getNextStep();
            
            if (tvTargetSentence != null) {
                tvTargetSentence.setText(selectedOption.getText());
            }
            recordButton.setTag(selectedOption.getText());
            
            fadeInView(recordingContainer);
        }, 1200);
    }

    private View addTypingIndicator() {
        if (!isAdded()) return null;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View typingBubble = inflater.inflate(R.layout.st5_chat_bubble_typing, chatContainer, false);
        chatContainer.addView(typingBubble);
        scrollToBottom();
        return typingBubble;
    }

    private void startSpeechToText() {
        if (!isAdded()) return;
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
        String targetText = tvTargetSentence != null ? tvTargetSentence.getText().toString() : "";
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Repeat: " + targetText);
        try {
            speechResultLauncher.launch(intent);
            recordButton.setText("Listening...");
            recordButton.setEnabled(false);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Speech recognition not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayRecognitionResult(String recognized, String correct) {
        if (!isAdded()) return;
        recognitionResultText.setVisibility(View.VISIBLE);
        SpannableString spannable = new SpannableString(recognized);
        String[] recognizedWords = recognized.toLowerCase().split("\\s+");
        String cleanCorrect = correct.replaceAll("[^a-zA-Z ]", "").toLowerCase();
        String[] correctWords = cleanCorrect.split("\\s+");

        int lastIndex = 0;
        for (int i = 0; i < recognizedWords.length; i++) {
            int color = ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark);
            if (i < correctWords.length && recognizedWords[i].equalsIgnoreCase(correctWords[i])) {
                color = ContextCompat.getColor(requireContext(), android.R.color.holo_green_dark);
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
        View botBubble = inflater.inflate(R.layout.st5_chat_bubble_bot, chatContainer, false);
        TextView messageText = botBubble.findViewById(R.id.chat_bubble_text);
        ImageButton speakButton = botBubble.findViewById(R.id.btn_speak_bot_message);
        ImageView avatar = botBubble.findViewById(R.id.iv_avatar_bot);
        
        avatar.setImageResource(R.drawable.st5_waiter);
        
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
        View userBubble = inflater.inflate(R.layout.st5_chat_bubble_user, chatContainer, false);
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
