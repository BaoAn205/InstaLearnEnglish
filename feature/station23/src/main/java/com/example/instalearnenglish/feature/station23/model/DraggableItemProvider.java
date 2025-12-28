package com.example.instalearnenglish.feature.station23.model;

import android.R;
import java.util.ArrayList;
import java.util.List;

public class DraggableItemProvider {

    public static List<DraggableItem> getDraggableItems() {
        List<DraggableItem> items = new ArrayList<>();

        // Allowed items (using placeholder system icons)
        items.add(new DraggableItem("Laptop", R.drawable.ic_menu_camera, true, ""));
        items.add(new DraggableItem("Book", R.drawable.ic_menu_agenda, true, ""));
        items.add(new DraggableItem("Headphones", R.drawable.ic_media_play, true, ""));
        items.add(new DraggableItem("Camera", R.drawable.ic_menu_camera, true, ""));

        // Prohibited items (using placeholder system icons)
        items.add(new DraggableItem("Knife", R.drawable.ic_menu_delete, false, "Vật sắc nhọn bị cấm trong hành lý xách tay!"));
        items.add(new DraggableItem("Water Bottle > 100ml", R.drawable.ic_menu_info_details, false, "Chất lỏng trên 100ml không được phép!"));
        // THE FIX: Replaced the private 'ic_menu_cut' icon with a public one
        items.add(new DraggableItem("Scissors", R.drawable.ic_menu_close_clear_cancel, false, "Kéo là vật sắc nhọn bị cấm!"));

        return items;
    }
}
