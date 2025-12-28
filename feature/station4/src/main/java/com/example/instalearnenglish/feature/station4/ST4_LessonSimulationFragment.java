package com.example.instalearnenglish.feature.station4;

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

public class ST4_LessonSimulationFragment extends Fragment {

    private LinearLayout chatContainer, optionsContainer, recordingContainer, scenarioSelectionContainer;
    private View chatMainLayout;
    private ScrollView scrollView;
    private MaterialButton recordButton;
    private TextView recognitionResultText;
    private TextView tvTargetSentence;
    private String nextStepIdAfterRecording;

    private TextToSpeech tts;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Map<String, ST4_SimulationStep> mockData;

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
        return inflater.inflate(R.layout.st4_fragment_lesson_simulation, container, false);
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

        // --- SCENARIO 1: THE SMOOTH CHECK-IN (COMPLETED) ---
        
        // STEP 1: Welcome
        ST4_SimulationOption sc1_1_1 = new ST4_SimulationOption("Hello, I have a reservation under the name Alex.", true, "Welcome Alex! Let me check our system for you.", "sc1_step2");
        ST4_SimulationOption sc1_1_2 = new ST4_SimulationOption("Hi, I'd like to check in, but I don't have a reservation.", true, "No problem! Let me see what rooms we have available.", "sc1_walkin_rooms");
        mockData.put("sc1_step1", new ST4_SimulationStep("Welcome to Grand Hotel! How can I assist you today?", Arrays.asList(sc1_1_1, sc1_1_2)));

        // Branch 1: Has Reservation - Verify ID
        ST4_SimulationOption sc1_2_1 = new ST4_SimulationOption("Here is my passport. Does the room have a city view?", true, "Yes, it does! Your room is on the 12th floor with a beautiful view.", "sc1_step3");
        mockData.put("sc1_step2", new ST4_SimulationStep("I found your booking for 2 nights. May I see your ID or passport, please?", Arrays.asList(sc1_2_1)));

        // Branch 2: Walk-in - Room Choice
        ST4_SimulationOption sc1_wi_1 = new ST4_SimulationOption("I'll take the Single Room, please.", true, "Great choice. That will be $100 per night. May I have your ID?", "sc1_step2");
        ST4_SimulationOption sc1_wi_2 = new ST4_SimulationOption("I prefer the Double Room for more space.", true, "Excellent. The Double Room is $150 per night. May I see your ID?", "sc1_step2");
        mockData.put("sc1_walkin_rooms", new ST4_SimulationStep("We have a Single Room and a Double Room available. Which one would you prefer?", Arrays.asList(sc1_wi_1, sc1_wi_2)));

        // STEP 3: Key handover & extras
        ST4_SimulationOption sc1_3_1 = new ST4_SimulationOption("What time is breakfast served in the morning?", true, "Breakfast is from 6:30 to 10:00 AM at the restaurant on the 1st floor.", "sc1_final");
        ST4_SimulationOption sc1_3_2 = new ST4_SimulationOption("Could you tell me how to get to the gym?", true, "The gym is located on the 2nd floor, just past the elevators.", "sc1_final");
        mockData.put("sc1_step3", new ST4_SimulationStep("Here is your key card. You are in Room 1205. Do you need help with your luggage?", Arrays.asList(sc1_3_1, sc1_3_2)));

        // STEP 4: Final greeting
        ST4_SimulationOption sc1_f_1 = new ST4_SimulationOption("Thank you very much for your help!", true, "You're very welcome! If you need anything else, just dial 0.", "end");
        mockData.put("sc1_final", new ST4_SimulationStep("Is there anything else I can assist you with before you head up?", Arrays.asList(sc1_f_1)));


        // --- SCENARIO 2: ROOM SERVICE REQUEST ---
        ST4_SimulationOption sc2_opt1_1 = new ST4_SimulationOption("Hello, I'm in room 1205. I need some fresh towels, please.", true, "Of course, sir. How many towels would you like?", "sc2_step2");
        ST4_SimulationOption sc2_opt1_2 = new ST4_SimulationOption("Hi, I would like to order some food to my room.", true, "Certainly! What would you like to order from our menu?", "sc2_step2_food");
        mockData.put("sc2_step1", new ST4_SimulationStep("Front desk, how may I help you today?", Arrays.asList(sc2_opt1_1, sc2_opt1_2)));

        ST4_SimulationOption sc2_opt2_1 = new ST4_SimulationOption("Could you bring up three fresh towels?", true, "Certainly. A housekeeper will bring them to your room shortly.", "end");
        mockData.put("sc2_step2", new ST4_SimulationStep("We can definitely do that. How many do you need?", Arrays.asList(sc2_opt2_1)));

        ST4_SimulationOption sc2_opt2_food_1 = new ST4_SimulationOption("I'll have a club sandwich and an orange juice.", true, "Excellent choice. That will be delivered in about 20 minutes.", "end");
        mockData.put("sc2_step2_food", new ST4_SimulationStep("Our kitchen is open. What can I get for you?", Arrays.asList(sc2_opt2_food_1)));

        mockData.put("end", new ST4_SimulationStep("Enjoy your experience! Is there anything else?", null));
    }

    private void loadStep(String stepId) {
        if (stepId == null || stepId.isEmpty() || stepId.equals("end")) {
            addBotMessage("Simulation completed! You've successfully practiced this scenario.", false);
            optionsContainer.setVisibility(View.GONE);
            recordingContainer.setVisibility(View.GONE);
            
            // Show return button
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

        ST4_SimulationStep step = mockData.get(stepId);
        if (step != null) {
            displayStep(step);
        }
    }

    private void displayStep(ST4_SimulationStep step) {
        if (!isAdded()) return;
        addBotMessage(step.getDialogue(), true);
        optionsContainer.removeAllViews();
        if (step.getOptions() != null) {
            for (ST4_SimulationOption option : step.getOptions()) {
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

    private void handleOptionClick(ST4_SimulationOption selectedOption) {
        if (!isAdded()) return;
        fadeOutView(optionsContainer, null);
        addUserMessage(selectedOption.getText());

        final View typingIndicator = addTypingIndicator();

        handler.postDelayed(() -> {
            if (!isAdded()) return;
            if(typingIndicator != null) chatContainer.removeView(typingIndicator);
            addBotMessage(selectedOption.getResponse(), true);
            nextStepIdAfterRecording = selectedOption.getNextStep();
            
            // Set the sentence user needs to repeat
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
        View typingBubble = inflater.inflate(R.layout.st4_chat_bubble_typing, chatContainer, false);
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
        View botBubble = inflater.inflate(R.layout.st4_chat_bubble_bot, chatContainer, false);
        TextView messageText = botBubble.findViewById(R.id.chat_bubble_text);
        ImageButton speakButton = botBubble.findViewById(R.id.btn_speak_bot_message);
        ImageView avatar = botBubble.findViewById(R.id.iv_avatar_bot);
        
        // Set receptionist avatar
        avatar.setImageResource(R.drawable.st4_receptionist);
        
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
        View userBubble = inflater.inflate(R.layout.st4_chat_bubble_user, chatContainer, false);
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
