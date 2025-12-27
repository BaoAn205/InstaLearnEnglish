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

        // Category 1: Containers
        List<VocabItem> containers = Arrays.asList(
            new VocabItem("Suitcase", "/ˈsuːtkeɪs/", R.drawable.vali, "Va li", "I packed my clothes in a large suitcase."),
            new VocabItem("Backpack", "/ˈbækpæk/", R.drawable.balo, "Ba lô", "He carries his laptop in a backpack."),
            new VocabItem("Carry-on", "/ˈkæri ɒn/", R.drawable.carry_on, "Hành lý xách tay", "You are allowed one carry-on bag."),
            new VocabItem("Wallet", "/ˈwɒlɪt/", R.drawable.wallet, "Ví tiền", "I keep my credit cards in my wallet."),
            new VocabItem("Pouch", "/paʊtʃ/", R.drawable.pouch, "Túi nhỏ", "She has a small pouch for her makeup."),
            new VocabItem("Duffel Bag", "/ˈdʌfl bæɡ/", R.drawable.duffel_bag, "Túi xách thể thao", "A duffel bag is great for a weekend trip."),
            new VocabItem("Trunk", "/trʌŋk/", R.drawable.trunk, "Rương, hòm", "They stored old photos in a large trunk.")
        );
        categoryList.add(new VocabCategory("Containers", containers));

        // Category 2: Documents
        List<VocabItem> documents = Arrays.asList(
            new VocabItem("Passport", "/ˈpæspɔːrt/", R.drawable.passport, "Hộ chiếu", "You need a passport to travel internationally."),
            new VocabItem("Ticket", "/ˈtɪkɪt/", R.drawable.planeticket, "Vé", "I have a plane ticket to Paris."),
            new VocabItem("Visa", "/ˈviːzə/", R.drawable.visa, "Thị thực (Visa)", "Some countries require a visa for entry."),
            new VocabItem("ID Card", "/ˌaɪˈdiː kɑːrd/", R.drawable.id_card, "Căn cước công dân", "You may need to show your ID card."),
            new VocabItem("Boarding Pass", "/ˈbɔːrdɪŋ pæs/", R.drawable.boarding_pass, "Thẻ lên máy bay", "Please present your boarding pass at the gate."),
            new VocabItem("Map", "/mæp/", R.drawable.map, "Bản đồ", "We used a map to find the hotel."),
            new VocabItem("Guidebook", "/ˈɡaɪdbʊk/", R.drawable.guide_book, "Sách hướng dẫn", "The guidebook has great restaurant recommendations."),
            new VocabItem("License", "/ˈlaɪsns/", R.drawable.license, "Bằng lái xe", "Don't forget your driver's license.")
        );
        categoryList.add(new VocabCategory("Documents", documents));

        // Category 3: Clothing
        List<VocabItem> clothing = Arrays.asList(
            new VocabItem("T-shirt", "/ˈtiː ʃɜːrt/", R.drawable.shirt, "Áo phông", "I packed three t-shirts for the trip."),
            new VocabItem("Jacket", "/ˈdʒækɪt/", R.drawable.jacket, "Áo khoác", "It might be cold, so bring a jacket."),
            new VocabItem("Trousers", "/ˈtraʊzərz/", R.drawable.trouser, "Quần dài", "He wears trousers to work."),
            new VocabItem("Socks", "/sɒks/", R.drawable.socks, "Tất (Vớ)", "I need to buy some new socks."),
            new VocabItem("Shoes", "/ʃuːz/", R.drawable.shoes, "Giày", "Wear comfortable shoes for walking."),
            new VocabItem("Scarf", "/skɑːrf/", R.drawable.scarf, "Khăn choàng", "A warm scarf is essential in winter."),
            new VocabItem("Hat", "/hæt/", R.drawable.hat, "Mũ", "Wear a hat to protect yourself from the sun."),
            new VocabItem("Gloves", "/ɡlʌvz/", R.drawable.gloves, "Găng tay", "You'll need gloves if you go skiing."),
            new VocabItem("Raincoat", "/ˈreɪnkoʊt/", R.drawable.raincoat, "Áo mưa", "The weather forecast says it will rain, pack a raincoat."),
            new VocabItem("Pajamas", "/pəˈdʒɑːməz/", R.drawable.pajamas, "Đồ ngủ", "I always pack my favorite pajamas.")
        );
        categoryList.add(new VocabCategory("Clothing", clothing));

        // Category 4: Toiletries
        List<VocabItem> toiletries = Arrays.asList(
            new VocabItem("Toothbrush", "/ˈtuːθbrʌʃ/", R.drawable.toothbrush, "Bàn chải đánh răng", "Don't forget to pack your toothbrush."),
            new VocabItem("Shampoo", "/ʃæmˈpuː/", R.drawable.shampoo, "Dầu gội", "The hotel provides soap and shampoo."),
            new VocabItem("Sunscreen", "/ˈsʌnskriːn/", R.drawable.sunscreen, "Kem chống nắng", "Apply sunscreen before you go to the beach."),
            new VocabItem("Soap", "/soʊp/", R.drawable.soap, "Xà phòng", "I washed my hands with soap and water."),
            new VocabItem("Towel", "/ˈtaʊəl/", R.drawable.towel, "Khăn tắm", "Please use a clean towel after your shower."),
            new VocabItem("Deodorant", "/diˈoʊdərənt/", R.drawable.deodorant, "Lăn khử mùi", "Using deodorant is part of daily hygiene."),
            new VocabItem("Wipes", "/waɪps/", R.drawable.wipe, "Khăn ướt", "Wet wipes are useful for cleaning your hands."),
            new VocabItem("Tissues", "/ˈtɪʃuːz/", R.drawable.tissue, "Khăn giấy", "She used a tissue to wipe her nose."),
            new VocabItem("Hand Sanitizer", "/hænd ˈsænɪtaɪzər/", R.drawable.hand_sanitizer, "Nước rửa tay khô", "It's important to use hand sanitizer.")
        );
        categoryList.add(new VocabCategory("Toiletries", toiletries));

        // Category 5: Electronics
        List<VocabItem> electronics = Arrays.asList(
            new VocabItem("Phone", "/foʊn/", R.drawable.phone, "Điện thoại", "My phone battery is low."),
            new VocabItem("Laptop", "/ˈlæptɒp/", R.drawable.laptop, "Máy tính xách tay", "I use my laptop for work and study."),
            new VocabItem("Charger", "/ˈtʃɑːrdʒər/", R.drawable.charger, "Sạc pin", "I need a charger for my phone."),
            new VocabItem("Headphones", "/ˈhedfoʊnz/", R.drawable.headphone, "Tai nghe", "I listen to music with my headphones."),
            new VocabItem("Camera", "/ˈkæmərə/", R.drawable.camera, "Máy ảnh", "She took beautiful photos with her new camera."),
            new VocabItem("Power Bank", "/ˈpaʊər bæŋk/", R.drawable.power_bank, "Sạc dự phòng", "A power bank is essential for long trips."),
            new VocabItem("Mouse", "/maʊs/", R.drawable.mouse, "Chuột máy tính", "I prefer using a mouse with my laptop.")
        );
        categoryList.add(new VocabCategory("Electronics", electronics));

        // Category 6: In-Flight Items
        List<VocabItem> inflight = Arrays.asList(
            new VocabItem("Neck Pillow", "/nek ˈpɪloʊ/", R.drawable.neck_pillow, "Gối cổ", "A neck pillow helps you sleep on the plane."),
            new VocabItem("Eye Mask", "/aɪ mæsk/", R.drawable.mask, "Mặt nạ che mắt", "An eye mask can help you block out light."),
            new VocabItem("Book", "/bʊk/", R.drawable.book, "Sách", "I always bring a book to read on long flights."),
            new VocabItem("Snacks", "/snæks/", R.drawable.snacks, "Đồ ăn vặt", "It's good to have some snacks with you."),
            new VocabItem("Water Bottle", "/ˈwɔːtər ˈbɒtl/", R.drawable.waterbottle, "Bình nước", "Remember to empty your water bottle before security."),
            new VocabItem("Earplugs", "/ˈɪərplʌɡz/", R.drawable.earplug, "Nút bịt tai", "Earplugs can help reduce engine noise."),
            new VocabItem("Journal", "/ˈdʒɜːrnl/", R.drawable.diary, "Sổ tay", "I like to write in my journal during the flight."),
            new VocabItem("Pen", "/pen/", R.drawable.pen, "Cái bút", "Can I borrow a pen?")
        );
        categoryList.add(new VocabCategory("In-Flight Items", inflight));

        // Category 7: General Items
        List<VocabItem> general = Arrays.asList(
            new VocabItem("Keys", "/kiːz/", R.drawable.key, "Chìa khóa", "Don't forget your house keys."),
            new VocabItem("Umbrella", "/ʌmˈbrelə/", R.drawable.umbrella, "Cái ô (dù)", "It might rain, so take an umbrella."),
            new VocabItem("First-aid Kit", "/ˌfɜːrst ˈeɪd kɪt/", R.drawable.first_aid, "Bộ sơ cứu", "A small first-aid kit is always useful."),
            new VocabItem("Flashlight", "/ˈflæʃlaɪt/", R.drawable.flashlight, "Đèn pin", "I have a flashlight on my phone."),
            new VocabItem("Bookmark", "/ˈbʊkmɑːrk/", R.drawable.bookmark, "Dấu trang sách", "I use a bookmark to save my page."),
            new VocabItem("Diary", "/ˈdaɪəri/", R.drawable.diary, "Nhật ký", "She writes in her diary every night."),
            new VocabItem("Reading Glasses", "/ˈriːdɪŋ ˈɡlæsɪz/", R.drawable.readingglasses, "Kính đọc sách", "He needs reading glasses to see the menu.")
        );
        categoryList.add(new VocabCategory("General Items", general));

    }
}
