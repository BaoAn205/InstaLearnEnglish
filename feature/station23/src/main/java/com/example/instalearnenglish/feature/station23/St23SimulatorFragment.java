package com.example.instalearnenglish.feature.station23;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.instalearnenglish.feature.station23.R;
import com.example.instalearnenglish.feature.station23.adapter.St23ChatAdapter;
import com.example.instalearnenglish.feature.station23.model.St23ChatMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class St23SimulatorFragment extends Fragment implements TextToSpeech.OnInitListener {

    private enum ScenarioState { CHOOSING_LIQUIDS, CHOOSING_LAPTOP, FINISHED }
    private enum PracticeMode { READING, SPEAKING }

    private RecyclerView rvChatHistory;
    private St23ChatAdapter chatAdapter;
    private List<St23ChatMessage> chatMessages;

    private View modeSelectionContainer, conversationContainer;
    private LinearLayout llChoiceButtons;
    private Button btnChoiceA, btnChoiceB, btnReadingMode, btnSpeakingMode;
    private FrameLayout flMicContainer;
    private LottieAnimationView lottieMicAnimation, lottieFeedbackCheckmark, lottieFeedbackFailed;
    private TextView tvPartialResults;

    private TextToSpeech tts;
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;
    private ScenarioState currentState;
    private PracticeMode currentMode;
    private String pendingCorrectAnswer;

    private final ActivityResultLauncher<String> requestPermissionLauncher = 
        registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) startSpeechRecognition();
            else Toast.makeText(getContext(), "Không có quyền ghi âm.", Toast.LENGTH_SHORT).show();
        });

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tts = new TextToSpeech(getContext(), this);
        setupSpeechRecognizer();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.st23_fragment_simulator, container, false);

        modeSelectionContainer = view.findViewById(R.id.st23_mode_selection_container);
        conversationContainer = view.findViewById(R.id.st23_conversation_container);
        rvChatHistory = view.findViewById(R.id.st23_rv_chat_history);
        llChoiceButtons = view.findViewById(R.id.st23_ll_choice_buttons);
        btnChoiceA = view.findViewById(R.id.st23_btn_choice_a);
        btnChoiceB = view.findViewById(R.id.st23_btn_choice_b);
        btnReadingMode = view.findViewById(R.id.st23_btn_reading_mode);
        btnSpeakingMode = view.findViewById(R.id.st23_btn_speaking_mode);
        flMicContainer = view.findViewById(R.id.st23_fl_mic_container);
        lottieMicAnimation = view.findViewById(R.id.st23_lottie_mic_animation);
        lottieFeedbackCheckmark = view.findViewById(R.id.st23_lottie_feedback_checkmark);
        lottieFeedbackFailed = view.findViewById(R.id.st23_lottie_feedback_failed);
        tvPartialResults = view.findViewById(R.id.st23_tv_partial_results);

        setupRecyclerView();
        setupFeedbackAnimations();
        setupModeSelection();

        return view;
    }

    private void setupModeSelection() {
        modeSelectionContainer.setVisibility(View.VISIBLE);
        conversationContainer.setVisibility(View.GONE);
        btnReadingMode.setOnClickListener(v -> startScenario(PracticeMode.READING));
        btnSpeakingMode.setOnClickListener(v -> startScenario(PracticeMode.SPEAKING));
    }

    private void startScenario(PracticeMode mode) {
        this.currentMode = mode;
        modeSelectionContainer.setVisibility(View.GONE);
        conversationContainer.setVisibility(View.VISIBLE);
        chatMessages.clear();
        chatAdapter.notifyDataSetChanged();
        currentState = ScenarioState.CHOOSING_LIQUIDS;
        advanceScenario();
    }

    private void advanceScenario() {
        llChoiceButtons.setVisibility(View.GONE);
        flMicContainer.setVisibility(View.GONE);

        switch (currentState) {
            case CHOOSING_LIQUIDS:
                addBotMessage("Do you have any liquids in your bag?");
                setupChoices("No, just my laptop.", "Yes, a 2-liter bottle of water.");
                break;
            case CHOOSING_LAPTOP:
                addBotMessage("Okay, please take your laptop out of the bag.");
                setupChoices("Here you go.", "I'll keep it inside.");
                break;
            case FINISHED:
                addBotMessage("Great! Have a nice flight!");
                break;
        }
    }

    private void setupChoices(String choiceAText, String choiceBText) {
        btnChoiceA.setText(choiceAText);
        btnChoiceB.setText(choiceBText);
        llChoiceButtons.setVisibility(View.VISIBLE);
        btnChoiceA.setOnClickListener(v -> handleCorrectChoice(choiceAText));
        btnChoiceB.setOnClickListener(v -> handleIncorrectChoice());
    }

    private void handleCorrectChoice(String correctText) {
        addUserMessage(correctText);
        this.pendingCorrectAnswer = correctText;

        if (currentMode == PracticeMode.SPEAKING) {
            llChoiceButtons.setVisibility(View.GONE);
            flMicContainer.setVisibility(View.VISIBLE);
            flMicContainer.setOnClickListener(v -> handleRecordButtonClick());
        } else {
            if(currentState == ScenarioState.CHOOSING_LIQUIDS) currentState = ScenarioState.CHOOSING_LAPTOP;
            else if(currentState == ScenarioState.CHOOSING_LAPTOP) currentState = ScenarioState.FINISHED;
            
            new Handler(Looper.getMainLooper()).postDelayed(this::advanceScenario, 1200);
        }
    }

    private void handleIncorrectChoice() {
        addBotMessage("I'm sorry, that's not the correct procedure. Please try again.");
    }

    private void checkAnswer(String spokenText) {
        String cleanCorrectAnswer = pendingCorrectAnswer.replaceAll("[^a-zA-Z ]", "").toLowerCase();
        String cleanSpokenText = spokenText.replaceAll("[^a-zA-Z ]", "").toLowerCase();

        if (cleanSpokenText.contains(cleanCorrectAnswer)) {
            lottieFeedbackCheckmark.setVisibility(View.VISIBLE);
            lottieFeedbackCheckmark.playAnimation();
            if(currentState == ScenarioState.CHOOSING_LIQUIDS) currentState = ScenarioState.CHOOSING_LAPTOP;
            else if(currentState == ScenarioState.CHOOSING_LAPTOP) currentState = ScenarioState.FINISHED;
            advanceScenario();
        } else {
            lottieFeedbackFailed.setVisibility(View.VISIBLE);
            lottieFeedbackFailed.playAnimation();
            Toast.makeText(getContext(), "Chưa đúng, hãy thử lại!", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void setupSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext());
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                lottieMicAnimation.playAnimation();
                tvPartialResults.setText("Đang nghe...");
                tvPartialResults.setVisibility(View.VISIBLE);
            }
            
            @Override
            public void onPartialResults(Bundle partialResults) {
                ArrayList<String> matches = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty()) {
                    tvPartialResults.setText(matches.get(0));
                }
            }

            @Override
            public void onResults(Bundle results) {
                tvPartialResults.setVisibility(View.GONE);
                lottieMicAnimation.cancelAnimation();
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty()) {
                    checkAnswer(matches.get(0));
                }
            }

            @Override
            public void onError(int error) {
                tvPartialResults.setVisibility(View.GONE);
                lottieMicAnimation.cancelAnimation();
                Log.e("SpeechRecognizer", "Error: " + error);
                Toast.makeText(getContext(), "Lỗi ghi âm, hãy thử lại.", Toast.LENGTH_SHORT).show();
            }
            
            @Override public void onBeginningOfSpeech() {}
            @Override public void onEndOfSpeech() {}
            @Override public void onRmsChanged(float rmsdB) {}
            @Override public void onBufferReceived(byte[] buffer) {}
            @Override public void onEvent(int eventType, Bundle params) {}
        });
    }

    private void startSpeechRecognition() {
        speechRecognizer.startListening(speechRecognizerIntent);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Language not supported");
            }
        } else {
            Log.e("TTS", "Initialization failed");
        }
    }

    private void addBotMessage(String message) {
        if (getContext() == null) return;
        chatMessages.add(new St23ChatMessage(message, St23ChatMessage.Sender.BOT));
        chatAdapter.notifyItemInserted(chatMessages.size() - 1);
        rvChatHistory.scrollToPosition(chatMessages.size() - 1);
        tts.speak(message, TextToSpeech.QUEUE_ADD, null, null);
    }

    private void addUserMessage(String message) {
        if (getContext() == null) return;
        chatMessages.add(new St23ChatMessage(message, St23ChatMessage.Sender.USER));
        chatAdapter.notifyItemInserted(chatMessages.size() - 1);
        rvChatHistory.scrollToPosition(chatMessages.size() - 1);
    }

    private void handleRecordButtonClick() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            startSpeechRecognition();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
        }
    }
    
    private void setupRecyclerView() {
        rvChatHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        chatMessages = new ArrayList<>();
        chatAdapter = new St23ChatAdapter(chatMessages);
        rvChatHistory.setAdapter(chatAdapter);
    }

    private void setupFeedbackAnimations() {
        lottieFeedbackFailed.enableMergePathsForKitKatAndAbove(true);
        Animator.AnimatorListener listener = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if(isAdded()) {
                    lottieFeedbackCheckmark.setVisibility(View.GONE);
                    lottieFeedbackFailed.setVisibility(View.GONE);
                }
            }
        };
        lottieFeedbackCheckmark.addAnimatorListener(listener);
        lottieFeedbackFailed.addAnimatorListener(listener);
    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
        super.onDestroy();
    }
}
