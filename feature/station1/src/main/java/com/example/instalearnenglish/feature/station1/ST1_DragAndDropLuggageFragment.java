package com.example.instalearnenglish.feature.station1;

import android.content.ClipData;
import android.content.ClipDescription;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ST1_DragAndDropLuggageFragment extends Fragment {

    private int correctDrops = 0;
    private int totalItems = 0;
    private List<View> draggableItems = new ArrayList<>();

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

        view.findViewById(R.id.carry_on_bin_container).setOnDragListener(new DragListener());
        view.findViewById(R.id.checked_luggage_bin_container).setOnDragListener(new DragListener());

        setupItem(view.findViewById(R.id.item_laptop), "Laptop", R.drawable.laptop, "carry_on");
        setupItem(view.findViewById(R.id.item_passport), "Passport", R.drawable.passport, "carry_on");
        setupItem(view.findViewById(R.id.item_water), "Water", R.drawable.waterbottle, "checked");
        setupItem(view.findViewById(R.id.item_scissors), "Scissors", R.drawable.scissor, "checked");
        setupItem(view.findViewById(R.id.item_tshirt), "T-Shirt", R.drawable.shirt, "checked");
        setupItem(view.findViewById(R.id.item_jacket), "Jacket", R.drawable.jacket, "carry_on");
        setupItem(view.findViewById(R.id.item_book), "Book", R.drawable.book, "carry_on");
        setupItem(view.findViewById(R.id.item_medication), "Medication", R.drawable.medicine, "carry_on");

        totalItems = draggableItems.size();

        view.findViewById(R.id.back_button).setOnClickListener(v -> requireActivity().finish());
    }

    private void setupItem(View itemView, String name, int imageRes, String correctBinTag) {
        ImageView imageView = itemView.findViewById(R.id.item_animation);
        TextView nameView = itemView.findViewById(R.id.item_name);

        imageView.setImageResource(imageRes);
        nameView.setText(name);
        itemView.setTag(correctBinTag);
        itemView.setOnTouchListener(new TouchListener());
        draggableItems.add(itemView);
    }

    private void updateGameProgress() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null || !isAdded()) {
            showWinDialog(false);
            return;
        }
        DocumentReference userDocRef = db.collection("users").document(user.getUid());

        userDocRef.update("station1_completed_games", FieldValue.arrayUnion("DRAG_AND_DROP_LUGGAGE"))
                .addOnSuccessListener(aVoid -> userDocRef.get().addOnSuccessListener(documentSnapshot -> {
                    if (!isAdded()) return;
                    if (documentSnapshot.exists()) {
                        List<String> completedGames = (List<String>) documentSnapshot.get("station1_completed_games");
                        if (completedGames != null && completedGames.size() >= 7) {
                            if (documentSnapshot.getLong("currentLevel") == 1L) {
                                userDocRef.update("currentLevel", 2L).addOnSuccessListener(aVoid1 -> showWinDialog(true));
                            } else {
                                showWinDialog(false);
                            }
                        } else {
                            showWinDialog(false);
                        }
                    }
                }))
                .addOnFailureListener(e -> showWinDialog(false));
    }

    private void showWinDialog(boolean justUnlocked) {
        if (!isAdded()) return;

        String message = "You've packed everything correctly!";
        if (justUnlocked) {
            message += "\n\nCongratulations! You have unlocked Station 2!";
        }

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
        for (View item : draggableItems) {
            item.setVisibility(View.VISIBLE);
            item.setAlpha(1.0f);
        }
    }

    private void playSoundAndShowToast(boolean isCorrect) {
        if (!isAdded()) return;
        int soundResId;
        String message;
        if (isCorrect) {
            soundResId = R.raw.right_answer;
            message = "Correct!";
        } else {
            soundResId = R.raw.wrong_answer;
            message = "Wrong luggage!";
        }

        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        MediaPlayer mp = MediaPlayer.create(getContext(), soundResId);
        mp.setOnCompletionListener(MediaPlayer::release);
        mp.start();
    }

    private static final class TouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData.Item item = new ClipData.Item((CharSequence) view.getTag());
                ClipData dragData = new ClipData((CharSequence) view.getTag(), new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
                View.DragShadowBuilder myShadow = new View.DragShadowBuilder(view);
                view.startDragAndDrop(dragData, myShadow, view, 0);
                view.setAlpha(0.4f);
                return true;
            } else {
                return false;
            }
        }
    }

    private class DragListener implements View.OnDragListener {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            if (!isAdded()) return false;
            final View draggedView = (View) event.getLocalState();

            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.animate().scaleX(1.1f).scaleY(1.1f).setDuration(200).start();
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start();
                    return true;
                case DragEvent.ACTION_DROP:
                    v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start();
                    String droppedOnTag = (String) v.getTag();
                    String itemTag = (String) draggedView.getTag();

                    if (itemTag.equals(droppedOnTag)) {
                        correctDrops++;
                        draggedView.setVisibility(View.GONE);
                        playSoundAndShowToast(true);
                        if (correctDrops == totalItems) {
                           new Handler(Looper.getMainLooper()).postDelayed(() -> updateGameProgress(), 1000);
                        }
                    } else {
                        draggedView.setAlpha(1.0f);
                        playSoundAndShowToast(false);
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
