package com.example.instalearnenglish.feature.station5;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.instalearnenglish.feature.station5.adapter.VocabCategoryAdapter;
import com.example.instalearnenglish.feature.station5.model.VocabCategory;
import com.example.instalearnenglish.feature.station5.model.VocabItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ST5_LessonVocabFragment extends Fragment {

    private RecyclerView rvCategories;
    private List<VocabCategory> categoryList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.st5_fragment_lesson_vocab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvCategories = view.findViewById(R.id.rv_categories);
        rvCategories.setLayoutManager(new LinearLayoutManager(getContext()));

        ViewPager2 parentViewPager = requireActivity().findViewById(R.id.viewPager);

        loadData();

        VocabCategoryAdapter categoryAdapter = new VocabCategoryAdapter(categoryList, (item, sharedImageView) -> {
            Intent intent = new Intent(getActivity(), ST5_VocabDetailActivity.class);
            intent.putExtra(ST5_VocabDetailActivity.EXTRA_NAME, item.getName());
            intent.putExtra(ST5_VocabDetailActivity.EXTRA_PHONETIC, item.getPhonetic());
            intent.putExtra(ST5_VocabDetailActivity.EXTRA_IMAGE_RES_ID, item.getImageResId());
            intent.putExtra(ST5_VocabDetailActivity.EXTRA_VIETNAMESE, item.getVietnameseMeaning());
            intent.putExtra(ST5_VocabDetailActivity.EXTRA_EXAMPLE, item.getExampleSentence());

            String transitionName = ViewCompat.getTransitionName(sharedImageView);
            intent.putExtra("EXTRA_TRANSITION_NAME", transitionName);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                requireActivity(),
                sharedImageView,
                transitionName
            );

            startActivity(intent, options.toBundle());
        }, parentViewPager);

        rvCategories.setAdapter(categoryAdapter);
    }

    private void loadData() {
        categoryList = new ArrayList<>();

        // Category 1: At the Restaurant
        List<VocabItem> dining = Arrays.asList(
            new VocabItem("Menu", "/ˈmenjuː/", R.drawable.st5_menu, "Thực đơn", "May I see the menu, please?"),
            new VocabItem("Appetizer", "/ˈæpɪtaɪzə(r)/", R.drawable.st5_appetizer, "Món khai vị", "We ordered spring rolls as an appetizer."),
            new VocabItem("Main Course", "/meɪn kɔːrs/", R.drawable.st5_main_course, "Món chính", "For the main course, I'll have the grilled salmon."),
            new VocabItem("Dessert", "/dɪˈzɜːrt/", R.drawable.st5_dessert, "Món tráng miệng", "Would you like to see the dessert menu?"),
            new VocabItem("Waiter", "/ˈweɪtər/", R.drawable.st5_waiter, "Bồi bàn", "The waiter recommended the chef's special."),
            new VocabItem("Bill / Check", "/bɪl/", R.drawable.st5_bill, "Hóa đơn", "Could we have the bill, please?")
        );
        categoryList.add(new VocabCategory("At the Restaurant", dining));

        // Category 2: Shopping for Clothes
        List<VocabItem> shopping = Arrays.asList(
            new VocabItem("Fitting Room", "/ˈfɪtɪŋ ruːm/", R.drawable.st5_fitting_room, "Phòng thử đồ", "Where are the fitting rooms?"),
            new VocabItem("Size", "/saɪz/", R.drawable.st5_size, "Kích cỡ", "Do you have this shirt in a medium size?"),
            new VocabItem("Discount", "/ˈdɪskaʊnt/", R.drawable.st5_discount, "Giảm giá", "There is a 20% discount on all winter coats."),
            new VocabItem("Receipt", "/rɪˈsiːt/", R.drawable.st5_receipt, "Biên lai", "Please keep your receipt for any returns."),
            new VocabItem("Cashier", "/kæˈʃɪər/", R.drawable.st5_cashier, "Thu ngân", "You can pay at the cashier over there."),
            new VocabItem("Refund", "/ˈriːfʌnd/", R.drawable.st5_refund, "Hoàn tiền", "Can I get a refund if the dress doesn't fit?")
        );
        categoryList.add(new VocabCategory("Shopping for Clothes", shopping));

        // Category 3: Food & Drinks
        List<VocabItem> foodDrinks = Arrays.asList(
            new VocabItem("Beverage", "/ˈbevərɪdʒ/", R.drawable.st5_beverage, "Đồ uống", "We offer a wide variety of hot and cold beverages."),
            new VocabItem("Seafood", "/ˈsiːfuːd/", R.drawable.st5_seafood, "Hải sản", "This restaurant is famous for its fresh seafood."),
            new VocabItem("Vegetarian", "/ˌvedʒəˈteriən/", R.drawable.st5_vegetarian, "Đồ chay", "Do you have any vegetarian options on the menu?"),
            new VocabItem("Spice", "/spaɪs/", R.drawable.st5_spice, "Gia vị", "I don't like too much spice in my food."),
            new VocabItem("Bakery", "/ˈbeɪkəri/", R.drawable.st5_bakery, "Tiệm bánh", "The bakery next door sells delicious croissants.")
        );
        categoryList.add(new VocabCategory("Food & Drinks", foodDrinks));
        
        // Category 4: Common Phrases
        List<VocabItem> phrases = Arrays.asList(
            new VocabItem("Special Offer", "/ˈspeʃl ˈɔːfər/", R.drawable.st5_special_offer, "Ưu đãi đặc biệt", "Check out our special offer for this week!"),
            new VocabItem("Out of Stock", "/aʊt əv stɒk/", R.drawable.st5_out_of_stock, "Hết hàng", "I'm sorry, that item is currently out of stock."),
            new VocabItem("Try on", "/traɪ ɒn/", R.drawable.st5_try_on, "Thử (đồ)", "I'd like to try on this blue jacket."),
            new VocabItem("Order", "/ˈɔːrdər/", R.drawable.st5_order, "Đặt hàng/Gọi món", "Are you ready to order now?")
        );
        categoryList.add(new VocabCategory("Common Phrases", phrases));
    }
}
