package com.example.instalearnenglish.feature.station23.adapter;

import android.content.ClipData;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instalearnenglish.feature.station23.R;
import com.example.instalearnenglish.feature.station23.model.DraggableItem;

import java.util.List;

public class St23DraggableAdapter extends RecyclerView.Adapter<St23DraggableAdapter.ViewHolder> {

    private final Context context;
    private final List<DraggableItem> items;

    public St23DraggableAdapter(Context context, List<DraggableItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.st23_item_draggable, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DraggableItem item = items.get(position);
        holder.imageView.setImageResource(item.getImageResId());

        holder.itemView.setOnLongClickListener(v -> {
            // Create a ClipData object to hold the dragged item's information
            // We'll pass the item's position in the adapter
            ClipData.Item clipItem = new ClipData.Item(String.valueOf(position));
            ClipData dragData = new ClipData(
                    (CharSequence) v.getTag(),
                    new String[]{"text/plain"},
                    clipItem
            );

            // Instantiate the drag shadow builder
            View.DragShadowBuilder myShadow = new View.DragShadowBuilder(holder.itemView);

            // Start the drag
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                v.startDragAndDrop(dragData, myShadow, holder.itemView, 0);
            } else {
                v.startDrag(dragData, myShadow, holder.itemView, 0);
            }
            
            // Hide the original view
            v.setVisibility(View.INVISIBLE);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.st23_iv_draggable_item);
        }
    }
}
