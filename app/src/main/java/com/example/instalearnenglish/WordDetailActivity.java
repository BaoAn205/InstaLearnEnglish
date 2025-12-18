package com.example.instalearnenglish;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.instalearnenglish.model.Definition;
import com.example.instalearnenglish.model.Meaning;
import com.example.instalearnenglish.model.Phonetic;
import com.example.instalearnenglish.model.Word;

import java.io.IOException;

public class WordDetailActivity extends AppCompatActivity {

    public static final String EXTRA_WORD_DETAIL = "com.example.instalearnenglish.EXTRA_WORD_DETAIL";

    private ImageButton btnPlayAudio;
    private String audioUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_detail);

        TextView tvWord = findViewById(R.id.tv_word);
        TextView tvPhonetic = findViewById(R.id.tv_phonetic);
        btnPlayAudio = findViewById(R.id.btn_play_audio);
        LinearLayout llMeaningsContainer = findViewById(R.id.ll_meanings_container);

        Word word = (Word) getIntent().getSerializableExtra(EXTRA_WORD_DETAIL);

        if (word != null) {
            tvWord.setText(word.getWord());

            // Find the first available phonetic text and audio URL
            if (word.getPhonetics() != null && !word.getPhonetics().isEmpty()) {
                String phoneticText = "";
                for (Phonetic p : word.getPhonetics()) {
                    if (p.getText() != null && !p.getText().isEmpty()) {
                        phoneticText = p.getText();
                    }
                    if (p.getAudio() != null && !p.getAudio().isEmpty()) {
                        audioUrl = p.getAudio();
                    }
                    if (!phoneticText.isEmpty() && audioUrl != null && !audioUrl.isEmpty()) {
                        break;
                    }
                }
                tvPhonetic.setText(phoneticText);

                if (audioUrl != null && !audioUrl.isEmpty()) {
                    btnPlayAudio.setVisibility(View.VISIBLE);
                    btnPlayAudio.setOnClickListener(v -> playAudio());
                } else {
                    btnPlayAudio.setVisibility(View.GONE);
                }
            } else {
                btnPlayAudio.setVisibility(View.GONE);
                tvPhonetic.setVisibility(View.GONE);
            }

            // Inflate and add meaning views dynamically
            if (word.getMeanings() != null) {
                LayoutInflater inflater = LayoutInflater.from(this);
                for (Meaning meaning : word.getMeanings()) {
                    TextView tvPartOfSpeech = new TextView(this);
                    tvPartOfSpeech.setText(meaning.getPartOfSpeech());
                    tvPartOfSpeech.setTextSize(20f);
                    tvPartOfSpeech.setTypeface(null, android.graphics.Typeface.BOLD);
                    llMeaningsContainer.addView(tvPartOfSpeech);

                    if (meaning.getDefinitions() != null) {
                        for (int i = 0; i < meaning.getDefinitions().size(); i++) {
                            Definition def = meaning.getDefinitions().get(i);

                            TextView tvDefinition = new TextView(this);
                            tvDefinition.setText("\t• " + def.getDefinition());
                            tvDefinition.setTextSize(16f);
                            llMeaningsContainer.addView(tvDefinition);

                            if (def.getExample() != null && !def.getExample().isEmpty()) {
                                TextView tvExample = new TextView(this);
                                tvExample.setText("\t\t\tExample: \"" + def.getExample() + "\"");
                                tvExample.setTextSize(14f);
                                tvExample.setTypeface(null, android.graphics.Typeface.ITALIC);
                                llMeaningsContainer.addView(tvExample);
                            }
                        }
                    }
                }
            }
        }
    }

    private void playAudio() {
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(MediaPlayer::start);
            mediaPlayer.setOnCompletionListener(MediaPlayer::release);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Could not play audio", Toast.LENGTH_SHORT).show();
        }
    }
}
