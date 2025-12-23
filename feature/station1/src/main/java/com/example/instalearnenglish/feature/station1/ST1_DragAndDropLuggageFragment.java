package com.example.instalearnenglish.feature.station1;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ST1_DragAndDropLuggageFragment extends Fragment {

    private int correctDrops = 0;
    private int totalItems = 0;
    private ViewGroup itemsContainer;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.st1_fragment_drag_and_drop_luggage, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        itemsContainer = view.findViewById(R.id.items_to_drag_container);

        view.findViewById(R.id.carry_on_bin_container).setOnDragListener(new DragListener());
        view.findViewById(R.id.checked_luggage_bin_container).setOnDragListener(new DragListener());

        // Setup items
        setupDraggableItem(view.findViewById(R.id.item_laptop));
        setupDraggableItem(view.findViewById(R.id.item_passport));
        setupDraggableItem(view.findViewById(R.id.item_water));
        setupDraggableItem(view.findViewById(R.id.item_scissors));
        setupDraggableItem(view.findViewById(R.id.item_tshirt));
        setupDraggableItem(view.findViewById(R.id.item_jacket));
        setupDraggableItem(view.findViewById(R.id.item_book));
        setupDraggableItem(view.findViewById(R.id.item_medication));

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

    private void checkAndAdvanceLevel() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            showWinDialog("You\'ve packed everything correctly!");
            return;
        }

        DocumentReference userDocRef = db.collection("users").document(currentUser.getUid());
        userDocRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot != null && documentSnapshot.exists()) {
                Long currentLevel = documentSnapshot.getLong("currentLevel");
                if (currentLevel == null) currentLevel = 1L;

                // Check if the user is on the level of this station (Station 1)
                if (currentLevel == 1) {
                    // Advance the user to the next level
                    userDocRef.update("currentLevel", 2)
                        .addOnSuccessListener(aVoid -> {
                            String message = "You\'ve packed everything correctly!\n\nCongratulations, you\'ve unlocked Station 2!";
                            showWinDialog(message);
                        })
                        .addOnFailureListener(e -> showWinDialog("You\'ve packed everything correctly!")); // Still show win dialog on failure
                } else {
                    // User has already passed this level
                    showWinDialog("You\'ve packed everything correctly!");
                }
            } else {
                showWinDialog("You\'ve packed everything correctly!");
            }
        }).addOnFailureListener(e -> showWinDialog("You\'ve packed everything correctly!"));
    }

    private void showWinDialog(String message) {
        if (!isAdded() || getContext() == null) return; // Ensure fragment is still attached

        new AlertDialog.Builder(requireContext())
            .setTitle("Congratulations!")
            .setMessage(message)
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
                        Toast.makeText(getContext(), "Correct!", Toast.LENGTH_SHORT).show();
                        if (correctDrops == totalItems) {
                            // Instead of calling showWinDialog directly, call the new method
                            checkAndAdvanceLevel();
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
