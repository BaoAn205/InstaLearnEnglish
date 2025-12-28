package com.example.instalearnenglish.feature.station1;

import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Color;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ST1_GameActivity extends AppCompatActivity {

    public static final String EXTRA_GAME_TYPE = "EXTRA_GAME_TYPE";
    public static final String EXTRA_GAME_TITLE = "EXTRA_GAME_TITLE";

    private int correctDrops = 0;
    private int totalItems = 0;
    private ViewGroup itemsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String gameType = getIntent().getStringExtra(EXTRA_GAME_TYPE);

        if ("DRAG_AND_DROP_LUGGAGE".equals(gameType)) {
            setContentView(R.layout.st1_fragment_drag_and_drop_luggage);
            initDragAndDropGame();
        } else {
            finish();
        }
    }

    private void initDragAndDropGame() {
        itemsContainer = findViewById(R.id.items_to_drag_container);

        findViewById(R.id.carry_on_bin_container).setOnDragListener(new DragListener());
        findViewById(R.id.checked_luggage_bin_container).setOnDragListener(new DragListener());

        setupDraggableItem(findViewById(R.id.item_laptop));
        setupDraggableItem(findViewById(R.id.item_passport));
        setupDraggableItem(findViewById(R.id.item_water));
        setupDraggableItem(findViewById(R.id.item_scissors));

        totalItems = itemsContainer.getChildCount();

        findViewById(R.id.back_button).setOnClickListener(v -> finish());
    }

    private void setupDraggableItem(View itemView) {
        itemView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                String correctBinTag = (String) v.getTag();
                ClipData.Item item = new ClipData.Item(correctBinTag);
                ClipData dragData = new ClipData(correctBinTag, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
                View.DragShadowBuilder myShadow = new View.DragShadowBuilder(v);
                v.startDragAndDrop(dragData, myShadow, v, 0);
                v.setAlpha(0.5f);
                return true;
            }
            return false;
        });
    }

    private void showWinDialog() {
        new AlertDialog.Builder(this)
            .setTitle("Congratulations!")
            .setMessage("You\'ve packed everything correctly!")
            .setPositiveButton("Play Again", (dialog, which) -> resetGame())
            .setNegativeButton("Exit", (dialog, which) -> finish())
            .setCancelable(false)
            .show();
    }

    private void resetGame() {
        correctDrops = 0;
        for (int i = 0; i < itemsContainer.getChildCount(); i++) {
            itemsContainer.getChildAt(i).setVisibility(View.VISIBLE);
            itemsContainer.getChildAt(i).setAlpha(1.0f);
        }
    }

    private class DragListener implements View.OnDragListener {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            final View draggedView = (View) event.getLocalState();
            int action = event.getAction();

            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    return true;

                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackgroundColor(Color.parseColor("#D1C4E9"));
                    return true;

                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackgroundColor(Color.TRANSPARENT);
                    return true;

                case DragEvent.ACTION_DROP:
                    v.setBackgroundColor(Color.TRANSPARENT);

                    String correctBinTag = event.getClipData().getDescription().getLabel().toString();
                    String targetBinTag = (v.getId() == R.id.carry_on_bin_container) ? "carry_on" : "checked";

                    if (correctBinTag.equals(targetBinTag)) {
                        correctDrops++;
                        draggedView.animate().alpha(0.0f).setDuration(300).withEndAction(() -> draggedView.setVisibility(View.GONE));
                        Toast.makeText(ST1_GameActivity.this, "Correct!", Toast.LENGTH_SHORT).show();
                        if (correctDrops == totalItems) {
                            showWinDialog();
                        }
                    } else {
                        final Animation shake = AnimationUtils.loadAnimation(ST1_GameActivity.this, R.anim.shake);
                        draggedView.startAnimation(shake);
                        draggedView.setAlpha(1.0f);
                        Toast.makeText(ST1_GameActivity.this, "Wrong luggage! Try again.", Toast.LENGTH_SHORT).show();
                    }
                    return true;

                case DragEvent.ACTION_DRAG_ENDED:
                    if (!event.getResult()) {
                        draggedView.setAlpha(1.0f);
                    }
                    return true;

                default:
                    return false;
            }
        }
    }
}
