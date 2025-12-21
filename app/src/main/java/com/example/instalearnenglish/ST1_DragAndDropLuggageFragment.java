package com.example.instalearnenglish;

import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Color;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class ST1_DragAndDropLuggageFragment extends Fragment {

    private int correctDrops = 0;
    private int totalItems = 0;
    private ViewGroup itemsContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.st1_fragment_drag_and_drop_luggage, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        itemsContainer = view.findViewById(R.id.items_to_drag_container);

        view.findViewById(R.id.carry_on_bin_container).setOnDragListener(new DragListener());
        view.findViewById(R.id.checked_luggage_bin_container).setOnDragListener(new DragListener());

        setupDraggableItem(view.findViewById(R.id.item_laptop));
        setupDraggableItem(view.findViewById(R.id.item_passport));
        setupDraggableItem(view.findViewById(R.id.item_water));
        setupDraggableItem(view.findViewById(R.id.item_scissors));

        totalItems = itemsContainer.getChildCount();

        view.findViewById(R.id.back_button).setOnClickListener(v -> requireActivity().finish());
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
        new AlertDialog.Builder(requireContext())
            .setTitle("Congratulations!")
            .setMessage("You\'ve packed everything correctly!")
            .setPositiveButton("Play Again", (dialog, which) -> resetGame())
            .setNegativeButton("Exit", (dialog, which) -> requireActivity().finish())
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
                    v.setBackgroundColor(Color.parseColor("#D1C4E9")); // Highlight color
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
                        Toast.makeText(getContext(), "Correct!", Toast.LENGTH_SHORT).show();
                        if (correctDrops == totalItems) {
                            showWinDialog();
                        }
                    } else {
                        final Animation shake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
                        draggedView.startAnimation(shake);
                        draggedView.setAlpha(1.0f);
                        Toast.makeText(getContext(), "Wrong luggage! Try again.", Toast.LENGTH_SHORT).show();
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
