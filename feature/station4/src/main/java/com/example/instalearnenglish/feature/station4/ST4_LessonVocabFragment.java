package com.example.instalearnenglish.feature.station4;

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

import com.example.instalearnenglish.feature.station4.adapter.VocabCategoryAdapter;
import com.example.instalearnenglish.feature.station4.model.VocabCategory;
import com.example.instalearnenglish.feature.station4.model.VocabItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ST4_LessonVocabFragment extends Fragment {

    private RecyclerView rvCategories;
    private List<VocabCategory> categoryList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.st4_fragment_lesson_vocab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvCategories = view.findViewById(R.id.rv_categories);
        rvCategories.setLayoutManager(new LinearLayoutManager(getContext()));

        ViewPager2 parentViewPager = requireActivity().findViewById(R.id.viewPager);

        loadData();

        VocabCategoryAdapter categoryAdapter = new VocabCategoryAdapter(categoryList, (item, sharedImageView) -> {
            Intent intent = new Intent(getActivity(), ST4_VocabDetailActivity.class);
            intent.putExtra(ST4_VocabDetailActivity.EXTRA_NAME, item.getName());
            intent.putExtra(ST4_VocabDetailActivity.EXTRA_PHONETIC, item.getPhonetic());
            intent.putExtra(ST4_VocabDetailActivity.EXTRA_IMAGE_RES_ID, item.getImageResId());
            intent.putExtra(ST4_VocabDetailActivity.EXTRA_VIETNAMESE, item.getVietnameseMeaning());
            intent.putExtra(ST4_VocabDetailActivity.EXTRA_EXAMPLE, item.getExampleSentence());

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

        // Category 1: Room Types
        List<VocabItem> roomTypes = Arrays.asList(
            new VocabItem("Single Room", "/ˈsɪŋɡl ruːm/", R.drawable.st4_single_room, "Phòng đơn", "I'd like to book a single room for two nights."),
            new VocabItem("Double Room", "/ˈdʌbl ruːm/", R.drawable.st4_double_room, "Phòng đôi", "The double room has a beautiful city view."),
            new VocabItem("Twin Room", "/twɪn ruːm/", R.drawable.st4_twin_room, "Phòng hai giường", "We booked a twin room for my friend and me."),
            new VocabItem("Suite", "/swiːt/", R.drawable.st4_suite, "Phòng hạng sang", "The honeymoon couple stayed in the executive suite."),
            new VocabItem("Penthouse", "/ˈpenthaʊs/", R.drawable.st4_penthouse, "Phòng áp mái", "The penthouse is the most expensive room in the hotel.")
        );
        categoryList.add(new VocabCategory("Room Types", roomTypes));

        // Category 2: Hotel Facilities
        List<VocabItem> facilities = Arrays.asList(
            new VocabItem("Reception", "/rɪˈsepʃn/", R.drawable.st4_reception, "Quầy lễ tân", "Please leave your key at the reception desk."),
            new VocabItem("Lobby", "/ˈlɒbi/", R.drawable.st4_lobby, "Sảnh chờ", "I'll meet you in the hotel lobby at 8 PM."),
            new VocabItem("Elevator", "/ˈelɪveɪtə(r)/", R.drawable.st4_elevator, "Thang máy", "The elevator is out of service, please use the stairs."),
            new VocabItem("Gym", "/dʒɪm/", R.drawable.st4_gym, "Phòng tập gym", "The hotel gym is open 24 hours a day."),
            new VocabItem("Swimming Pool", "/ˈswɪmɪŋ puːl/", R.drawable.st4_pool, "Hồ bơi", "The outdoor swimming pool is heated in winter."),
            new VocabItem("Restaurant", "/ˈrestrɒnt/", R.drawable.st4_restaurant, "Nhà hàng", "The hotel restaurant serves excellent local cuisine.")
        );
        categoryList.add(new VocabCategory("Hotel Facilities", facilities));

        // Category 3: In-room Amenities
        List<VocabItem> amenities = Arrays.asList(
            new VocabItem("Mini-bar", "/ˈmɪni bɑː(r)/", R.drawable.st4_minibar, "Tủ lạnh nhỏ", "All drinks in the mini-bar are complimentary."),
            new VocabItem("Air Conditioner", "/ˈeə kəndɪʃənə(r)/", R.drawable.st4_ac, "Máy điều hòa", "Can you show me how to use the air conditioner?"),
            new VocabItem("Safe", "/seɪf/", R.drawable.st4_safe, "Két sắt", "You should put your passport in the room safe."),
            new VocabItem("Towel", "/ˈtaʊəl/", R.drawable.st4_towel, "Khăn tắm", "Could we have some extra towels, please?"),
            new VocabItem("Slippers", "/ˈslɪpəz/", R.drawable.st4_slippers, "Dép đi trong nhà", "The hotel provides free disposable slippers."),
            new VocabItem("Hairdryer", "/ˈheədraɪə(r)/", R.drawable.st4_hairdryer, "Máy sấy tóc", "There is a hairdryer in the bathroom cabinet.")
        );
        categoryList.add(new VocabCategory("In-room Amenities", amenities));

        // Category 4: Staff & Services
        List<VocabItem> staff = Arrays.asList(
            new VocabItem("Receptionist", "/rɪˈsepʃənɪst/", R.drawable.st4_receptionist, "Nhân viên lễ tân", "The receptionist was very helpful and friendly."),
            new VocabItem("Bellhop", "/ˈbelhɒp/", R.drawable.st4_bellhop, "Nhân viên hành lý", "The bellhop will take your suitcases to your room."),
            new VocabItem("Housekeeper", "/ˈhaʊskiːpə(r)/", R.drawable.st4_housekeeper, "Nhân viên dọn phòng", "The housekeeper cleans the rooms every morning."),
            new VocabItem("Room Service", "/ruːm ˈsɜːvɪs/", R.drawable.st4_room_service, "Dịch vụ tại phòng", "We ordered breakfast through room service."),
            new VocabItem("Laundry Service", "/ˈlɔːndri ˈsɜːvɪs/", R.drawable.st4_laundry, "Dịch vụ giặt ủi", "Does the hotel offer a same-day laundry service?")
        );
        categoryList.add(new VocabCategory("Staff & Services", staff));

        // Category 5: Procedures & Payment
        List<VocabItem> procedures = Arrays.asList(
            new VocabItem("Reservation", "/ˌrezəˈveɪʃn/", R.drawable.st4_reservation, "Đặt phòng trước", "I have a reservation under the name of Nguyen."),
            new VocabItem("Check-in", "/tʃek ɪn/", R.drawable.st4_checkin, "Nhận phòng", "Check-in time starts from 2 PM."),
            new VocabItem("Check-out", "/tʃek aʊt/", R.drawable.st4_checkout, "Trả phòng", "We need to check out before 11 AM."),
            new VocabItem("Key Card", "/kiː kɑːd/", R.drawable.st4_keycard, "Thẻ từ", "Don't forget to return your key card at the desk."),
            new VocabItem("Bill", "/bɪl/", R.drawable.st4_bill, "Hóa đơn", "Could I have the bill, please? I'm ready to pay.")
        );
        categoryList.add(new VocabCategory("Procedures & Payment", procedures));
    }
}
